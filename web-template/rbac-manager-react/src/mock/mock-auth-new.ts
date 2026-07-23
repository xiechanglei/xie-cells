import {MockMethod} from 'vite-plugin-mock'
import {buildErrorResponse, buildSuccessResponse} from "./util_mock";
import {userData, roleUserData, roleData, rolePermissionData} from "./util_data";

const authMethods: MockMethod[] = [
    // 用户登录接口
    {
        url: '/rbac/auth/login',
        method: 'post',
        response: ({body}) => {
            const {userName, userPassword} = body;
            // 模拟密码加密比较（在实际应用中密码是加密存储的）
            const user = userData.find(u => u.userName === userName);

            if (user && user.userPassword === userPassword) {
                const token = "mock-token-" + user.id;
                return buildSuccessResponse(token);
            } else {
                return buildErrorResponse("用户名或密码错误");
            }
        },
    },

    // 修改当前用户密码
    {
        url: '/rbac/auth/changePass',
        method: 'post',
        response: ({body}) => {
            const {newPass, user} = body;

            // 在实际应用中这里会验证旧密码，但mock中我们直接更新
            const index = userData.findIndex(u => u.id === user.id);
            if (index !== -1) {
                userData[index].userPassword = newPass;
                userData[index].userSerial += 1; // 更新序列号

                // 返回新的token
                const newToken = `mock-token-${userData[index].id}-${Date.now()}`;
                return buildSuccessResponse(newToken);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },

    // 获取当前用户信息
    {
        url: '/rbac/auth/detail',
        method: 'get',
        response: ({query}) => {
            const {id} = query || {};
            const user = userData.find(u => u.id === id);

            if (user) {
                // 获取用户角色
                const userRoles = roleUserData
                    .filter(ru => ru.userId === id)
                    .map(ru => roleData.find(r => r.id === ru.roleId))
                    .filter(Boolean);

                return buildSuccessResponse({
                    user,
                    roles: userRoles
                });
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },

    // 获取当前用户权限
    {
        url: '/rbac/auth/permission',
        method: 'get',
        response: ({query}) => {
            const {userId} = query || {};

            // 检查是否为管理员
            const userRoles = roleUserData.filter(ru => ru.userId === userId);
            const isAdmin = userRoles.some(ur => {
                const role = roleData.find(r => r.id === ur.roleId);
                return role && role.admin;
            });

            if (isAdmin) {
                return buildSuccessResponse({
                    admin: true,
                    permissionCode: null
                });
            } else {
                // 获取普通用户权限
                const permissions = rolePermissionData
                    .filter(rp => userRoles.some(ur => ur.roleId === rp.roleId))
                    .map(rp => rp.permissionCode);

                return buildSuccessResponse({
                    admin: false,
                    permissionCode: [...new Set(permissions)] // 去重
                });
            }
        }
    },

    // 更新当前用户信息
    {
        url: '/rbac/auth/update',
        method: 'post',
        response: ({body}) => {
            const {user, newUser} = body;

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

    // 获取当前用户特征码
    {
        url: '/rbac/auth/feature/get',
        method: 'get',
        response: ({query}) => {
            const {id} = query || {};
            const user = userData.find(u => u.id === id);

            if (user) {
                if (!user.feature) {
                    // 如果没有特征码则重置
                    return buildSuccessResponse(`feature_new-${Date.now()}`);
                }
                return buildSuccessResponse(`feature_${user.feature}`);
            } else {
                return buildErrorResponse("用户不存在");
            }
        }
    },

    // 重置当前用户特征码
    {
        url: '/rbac/auth/feature/reset',
        method: 'post',
        response: ({body}) => {
            const {user} = body;

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

export default authMethods;