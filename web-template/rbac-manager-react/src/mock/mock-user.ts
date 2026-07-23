import {MockMethod} from 'vite-plugin-mock'
import {buildSuccessResponse, buildErrorResponse} from "./util_mock";
import {userData, roleUserData, roleData} from "./util_data";

const userMethods: MockMethod[] = [
    // 查询所有用户
    {
        url: '/rbac/user/all',
        method: 'get',
        response: () => {
            return buildSuccessResponse(userData);
        }
    },
    
    // 查询用户
    {
        url: '/rbac/user/query',
        method: 'get',
        response: ({ query }) => {
            const { word } = query || {};
            let filteredUsers = userData;
            
            if (word) {
                filteredUsers = userData.filter(user => 
                    user.userName.includes(word) || 
                    user.nickName.includes(word) ||
                    user.email.includes(word)
                );
            }
            
            // 模拟分页 - 只返回前10条记录
            return buildSuccessResponse({
                content: filteredUsers.slice(0, 10),
                totalElements: filteredUsers.length,
                totalPages: Math.ceil(filteredUsers.length / 10),
                number: 0,
                size: 10
            });
        }
    },
    
    // 获取用户信息
    {
        url: '/rbac/user/get',
        method: 'get',
        response: ({ query }) => {
            const { id } = query || {};
            const user = userData.find(u => u.id === id);
            
            if (user) {
                return buildSuccessResponse(user);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 添加用户
    {
        url: '/rbac/user/add',
        method: 'post',
        response: ({ body }) => {
            const { user, userPassword } = body;
            
            // 检查用户名是否已存在
            const existingUser = userData.find(u => u.userName === user.userName);
            if (existingUser) {
                return buildErrorResponse("用户已存在");
            }
            
            // 创建新用户
            const newUser = {
                ...user,
                id: `user${userData.length + 1}`,
                userPassword: userPassword,
                userStatus: "ENABLED",
                userSerial: 1,
                feature: `uuid-${userData.length + 1}`,
                createTime: new Date().getTime().toString(),
                updateTime: new Date().getTime().toString()
            };
            
            userData.push(newUser);
            return buildSuccessResponse(null);
        }
    },
    
    // 更新用户信息
    {
        url: '/rbac/user/update',
        method: 'post',
        response: ({ body }) => {
            const { user, newUser } = body;
            
            const index = userData.findIndex(u => u.id === user.id);
            if (index !== -1) {
                userData[index] = {
                    ...userData[index],
                    nickName: newUser.nickName,
                    phoneNumber: newUser.phoneNumber,
                    email: newUser.email,
                    address: newUser.address
                };
                
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 修改用户密码
    {
        url: '/rbac/user/changePass',
        method: 'post',
        response: ({ body }) => {
            const { userPassword, userId } = body;
            
            const index = userData.findIndex(u => u.id === userId);
            if (index !== -1) {
                userData[index].userPassword = userPassword;
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 禁用用户
    {
        url: '/rbac/user/disable',
        method: 'post',
        response: ({ body }) => {
            const { userId } = body;
            
            const index = userData.findIndex(u => u.id === userId);
            if (index !== -1) {
                // 检查是否为管理员
                const userRoles = roleUserData.filter(ru => ru.userId === userId);
                const isAdmin = userRoles.some(ur => {
                    const role = roleData.find(r => r.id === ur.roleId);
                    return role && role.admin;
                });
                
                if (isAdmin) {
                    return buildErrorResponse("管理员用户不能被禁用");
                }
                
                userData[index].userStatus = "DISABLED";
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 启用用户
    {
        url: '/rbac/user/enable',
        method: 'post',
        response: ({ body }) => {
            const { userId } = body;
            
            const index = userData.findIndex(u => u.id === userId);
            if (index !== -1) {
                userData[index].userStatus = "ENABLED";
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 获取用户角色
    {
        url: '/rbac/user/roles',
        method: 'get',
        response: ({ query }) => {
            const { userId } = query || {};
            const userRoles = roleUserData
                .filter(ru => ru.userId === userId)
                .map(ru => roleData.find(r => r.id === ru.roleId))
                .filter(Boolean);
            
            return buildSuccessResponse(userRoles);
        }
    },
    
    // 删除用户
    {
        url: '/rbac/user/delete',
        method: 'post',
        response: ({ body }) => {
            const { userId } = body;
            
            // 检查是否为管理员
            const userRoles = roleUserData.filter(ru => ru.userId === userId);
            const isAdmin = userRoles.some(ur => {
                const role = roleData.find(r => r.id === ur.roleId);
                return role && role.admin;
            });
            
            if (isAdmin) {
                return buildErrorResponse("管理员用户不能被删除");
            }
            
            const index = userData.findIndex(u => u.id === userId);
            if (index !== -1) {
                // 删除用户
                userData.splice(index, 1);
                
                // 删除角色用户关系
                const roleUserIndexes = roleUserData
                    .map((ru, idx) => ru.userId === userId ? idx : -1)
                    .filter(idx => idx !== -1);
                
                for (let i = roleUserIndexes.length - 1; i >= 0; i--) {
                    roleUserData.splice(roleUserIndexes[i], 1);
                }
                
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 获取用户特征码
    {
        url: '/rbac/user/feature/get',
        method: 'get',
        response: ({ query }) => {
            const { id } = query || {};
            const user = userData.find(u => u.id === id);
            
            if (user) {
                return buildSuccessResponse(`feature_${user.feature}`);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },
    
    // 重置用户特征码
    {
        url: '/rbac/user/feature/reset',
        method: 'post',
        response: ({ body }) => {
            const { user } = body;
            
            const index = userData.findIndex(u => u.id === user.id);
            if (index !== -1) {
                userData[index].feature = `new-feature-${Date.now()}`;
                return buildSuccessResponse(`feature_${userData[index].feature}`);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    }
];

export default userMethods;