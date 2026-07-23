import {MockMethod} from 'vite-plugin-mock'
import {buildSuccessResponse, buildErrorResponse} from "./util_mock";
import {roleData, roleUserData, rolePermissionData, permissionData, userData} from "./util_data";

const roleMethods: MockMethod[] = [
    // 查询角色
    {
        url: '/rbac/role/query',
        method: 'get',
        response: ({ query }) => {
            const { roleName } = query || {};
            let filteredRoles = roleData;
            
            if (roleName) {
                filteredRoles = roleData.filter(role => 
                    role.roleName.includes(roleName)
                );
            }
            
            // 模拟分页 - 只返回前10条记录
            return buildSuccessResponse({
                content: filteredRoles.slice(0, 10),
                totalElements: filteredRoles.length,
                totalPages: Math.ceil(filteredRoles.length / 10),
                number: 0,
                size: 10
            });
        }
    },
    
    // 获取所有角色
    {
        url: '/rbac/role/all',
        method: 'get',
        response: () => {
            return buildSuccessResponse(roleData);
        }
    },
    
    // 创建角色
    {
        url: '/rbac/role/add',
        method: 'post',
        response: ({ body }) => {
            const { roleName, roleRemark } = body;
            
            // 检查角色名是否已存在
            const existingRole = roleData.find(r => r.roleName === roleName);
            if (existingRole) {
                return buildErrorResponse("角色已存在");
            }
            
            // 创建新角色
            const newRole = {
                id: `role${roleData.length + 1}`,
                roleName,
                roleStatus: "ENABLED",
                roleRemark,
                createTime: new Date().getTime().toString(),
                updateTime: new Date().getTime().toString(),
                admin: false
            };
            
            roleData.push(newRole);
            return buildSuccessResponse(null);
        }
    },
    
    // 根据ID获取角色
    {
        url: '/rbac/role/get',
        method: 'get',
        response: ({ query }) => {
            const { roleId } = query || {};
            const role = roleData.find(r => r.id === roleId);
            
            if (role) {
                return buildSuccessResponse(role);
            } else {
                return buildErrorResponse("角色不存在");
            }
        }
    },
    
    // 更新角色
    {
        url: '/rbac/role/update',
        method: 'post',
        response: ({ body }) => {
            const { roleName, roleId, roleRemark } = body;
            
            const index = roleData.findIndex(r => r.id === roleId);
            if (index !== -1) {
                // 检查是否为管理员角色
                if (roleData[index].admin) {
                    return buildErrorResponse("管理员角色不能被修改");
                }
                
                // 检查新角色名是否与其他角色冲突
                const duplicateRole = roleData.find(
                    r => r.id !== roleId && r.roleName === roleName
                );
                if (duplicateRole) {
                    return buildErrorResponse("角色名已存在");
                }
                
                roleData[index].roleName = roleName;
                roleData[index].roleRemark = roleRemark;
                
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("角色不存在");
            }
        }
    },
    
    // 禁用角色
    {
        url: '/rbac/role/disable',
        method: 'post',
        response: ({ body }) => {
            const { roleId } = body;
            
            const index = roleData.findIndex(r => r.id === roleId);
            if (index !== -1) {
                // 检查是否为管理员角色
                if (roleData[index].admin) {
                    return buildErrorResponse("管理员角色不能被禁用");
                }
                
                roleData[index].roleStatus = "DISABLED";
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("角色不存在");
            }
        }
    },
    
    // 启用角色
    {
        url: '/rbac/role/enable',
        method: 'post',
        response: ({ body }) => {
            const { roleId } = body;
            
            const index = roleData.findIndex(r => r.id === roleId);
            if (index !== -1) {
                // 检查是否为管理员角色
                if (roleData[index].admin) {
                    return buildErrorResponse("管理员角色不能被启用");
                }
                
                roleData[index].roleStatus = "ENABLED";
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("角色不存在");
            }
        }
    },
    
    // 删除角色
    {
        url: '/rbac/role/delete',
        method: 'post',
        response: ({ body }) => {
            const { roleId } = body;
            
            // 检查是否有用户属于此角色
            const roleUsers = roleUserData.filter(ru => ru.roleId === roleId);
            if (roleUsers.length > 0) {
                return buildErrorResponse("角色下有用户，不能删除");
            }
            
            // 检查是否为管理员角色
            const role = roleData.find(r => r.id === roleId);
            if (role && role.admin) {
                return buildErrorResponse("管理员角色不能被删除");
            }
            
            const index = roleData.findIndex(r => r.id === roleId);
            if (index !== -1) {
                roleData.splice(index, 1);
                
                // 删除角色权限关系
                const rolePermIndexes = rolePermissionData
                    .map((rp, idx) => rp.roleId === roleId ? idx : -1)
                    .filter(idx => idx !== -1);
                
                for (let i = rolePermIndexes.length - 1; i >= 0; i--) {
                    rolePermissionData.splice(rolePermIndexes[i], 1);
                }
                
                return buildSuccessResponse(null);
            } else {
                return buildErrorResponse("角色不存在");
            }
        }
    },
    
    // 获取角色权限
    {
        url: '/rbac/role/permission/get',
        method: 'get',
        response: ({ query }) => {
            const { roleId } = query || {};
            const permissions = rolePermissionData
                .filter(rp => rp.roleId === roleId)
                .map(rp => rp.permissionCode);
            
            return buildSuccessResponse(permissions);
        }
    },
    
    // 获取所有权限
    {
        url: '/rbac/role/permission/all',
        method: 'get',
        response: () => {
            return buildSuccessResponse(permissionData);
        }
    },
    
    // 更新角色权限
    {
        url: '/rbac/role/permission/update',
        method: 'post',
        response: ({ body }) => {
            const { roleId, permissionIds } = body;
            
            // 检查角色是否存在
            const role = roleData.find(r => r.id === roleId);
            if (!role) {
                return buildErrorResponse("角色不存在");
            }
            
            // 检查是否为管理员角色
            if (role.admin) {
                return buildErrorResponse("管理员角色权限不能被修改");
            }
            
            // 删除现有权限
            const indexesToRemove = rolePermissionData
                .map((rp, idx) => rp.roleId === roleId ? idx : -1)
                .filter(idx => idx !== -1);
            
            for (let i = indexesToRemove.length - 1; i >= 0; i--) {
                rolePermissionData.splice(indexesToRemove[i], 1);
            }
            
            // 添加新权限
            if (permissionIds && Array.isArray(permissionIds)) {
                for (const permId of permissionIds) {
                    // 检查权限是否存在
                    const permExists = permissionData.some(p => p.code === permId);
                    if (permExists) {
                        rolePermissionData.push({
                            roleId,
                            permissionCode: permId
                        });
                    }
                }
            }
            
            return buildSuccessResponse(null);
        }
    },
    
    // 获取角色的用户
    {
        url: '/rbac/role/users',
        method: 'get',
        response: ({ query }) => {
            const { roleId } = query || {};
            const roleUsers = roleUserData
                .filter(ru => ru.roleId === roleId)
                .map(ru => {
                    const user = userData.find(u => u.id === ru.userId);
                    return user;
                })
                .filter(Boolean);
            
            return buildSuccessResponse({
                content: roleUsers,
                totalElements: roleUsers.length,
                totalPages: Math.ceil(roleUsers.length / 10),
                number: 0,
                size: 10
            });
        }
    },
    
    // 给用户授予角色
    {
        url: '/rbac/role/grant',
        method: 'post',
        response: ({ body }) => {
            const { userId, roleId } = body;
            
            // 检查角色是否存在
            const role = roleData.find(r => r.id === roleId);
            if (!role) {
                return buildErrorResponse("角色不存在");
            }
            
            // 检查用户是否已拥有该角色
            const existing = roleUserData.find(ru => ru.userId === userId && ru.roleId === roleId);
            if (existing) {
                return buildErrorResponse("用户已拥有该角色");
            }
            
            // 添加角色用户关系
            roleUserData.push({ userId, roleId });
            return buildSuccessResponse(null);
        }
    },
    
    // 撤销用户角色
    {
        url: '/rbac/role/revoke',
        method: 'post',
        response: ({ body }) => {
            const { userId, roleId } = body;
            
            // 检查用户是否拥有该角色
            const index = roleUserData.findIndex(ru => ru.userId === userId && ru.roleId === roleId);
            if (index === -1) {
                return buildErrorResponse("用户未拥有该角色");
            }
            
            // 检查是否为管理员角色
            const role = roleData.find(r => r.id === roleId);
            if (role && role.admin) {
                // 检查是否还有其他管理员
                const adminUsers = roleUserData.filter(ru => {
                    const r = roleData.find(role => role.id === ru.roleId);
                    return r && r.admin;
                });
                
                if (adminUsers.length <= 1) {
                    return buildErrorResponse("至少需要保留一个管理员角色用户");
                }
            }
            
            // 删除角色用户关系
            roleUserData.splice(index, 1);
            return buildSuccessResponse(null);
        }
    }
];

export default roleMethods;