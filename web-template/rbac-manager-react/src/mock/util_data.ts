export const now = new Date().getTime().toString();

// 模拟用户数据
export const userData = [
    {
        id: "user001", 
        userName: 'admin', 
        userPassword: '123456', // 实际应用中这是加密后的密码
        userStatus: "ENABLED", 
        nickName: 'admin', 
        phoneNumber: '12345678901', 
        email: 'admin@example.com', 
        address: 'Beijing', 
        userSerial: 1, 
        feature: "uuid-1", 
        createTime: now, 
        updateTime: now
    },
    {
        id: "user002", 
        userName: 'user1', 
        userPassword: '123456', 
        userStatus: "ENABLED", 
        nickName: '普通用户', 
        phoneNumber: '12345678902', 
        email: 'user1@example.com', 
        address: 'Shanghai', 
        userSerial: 1, 
        feature: "uuid-2", 
        createTime: now, 
        updateTime: now
    }
];

// 模拟角色数据
export const roleData = [
    {
        id: "role001",
        roleName: '管理员',
        roleStatus: "ENABLED",
        roleRemark: '系统管理员',
        createTime: now,
        updateTime: now,
        admin: true
    },
    {
        id: "role002",
        roleName: '普通用户',
        roleStatus: "ENABLED",
        roleRemark: '普通用户角色',
        createTime: now,
        updateTime: now,
        admin: false
    }
];

// 模拟权限数据
export const permissionData = [
    {
        id: "perm001",
        code: "RBAC::USER::QUERY",
        name: "查询用户",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm002",
        code: "RBAC::USER::ADD",
        name: "添加用户",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm003",
        code: "RBAC::USER::EDIT",
        name: "编辑用户",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm004",
        code: "RBAC::USER::DELETE",
        name: "删除用户",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm005",
        code: "RBAC::ROLE::QUERY",
        name: "查询角色",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm006",
        code: "RBAC::ROLE::ADD",
        name: "创建角色",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm007",
        code: "RBAC::ROLE::EDIT",
        name: "更新角色",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm008",
        code: "RBAC::ROLE::DELETE",
        name: "删除角色",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm009",
        code: "RBAC::ROLE::EDIT::PERMISSION",
        name: "更新角色权限",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm010",
        code: "RBAC::ROLE::GRANT",
        name: "用户分配角色",
        createTime: now,
        updateTime: now
    },
    {
        id: "perm011",
        code: "RBAC::LOG::QUERY",
        name: "查询日志",
        createTime: now,
        updateTime: now
    }
];

// 模拟角色用户关系数据
export const roleUserData = [
    {
        roleId: "role001",
        userId: "user001"
    },
    {
        roleId: "role002",
        userId: "user002"
    }
];

// 模拟角色权限关系数据
export const rolePermissionData = [
    {
        roleId: "role001",
        permissionCode: "RBAC::USER::QUERY"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::USER::ADD"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::USER::EDIT"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::USER::DELETE"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::QUERY"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::ADD"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::EDIT"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::DELETE"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::EDIT::PERMISSION"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::ROLE::GRANT"
    },
    {
        roleId: "role001",
        permissionCode: "RBAC::LOG::QUERY"
    },
    {
        roleId: "role002",
        permissionCode: "RBAC::USER::QUERY"
    }
];

// 模拟日志数据
export const logData = [
    {
        id: "log001",
        userId: "user001",
        logTitle: "用户登录",
        logContent: "管理员登录系统",
        logPath: "/rbac/auth/login",
        logAddress: "127.0.0.1",
        createTime: now,
        updateTime: now
    },
    {
        id: "log002",
        userId: "user002",
        logTitle: "用户查询",
        logContent: "普通用户查询用户信息",
        logPath: "/rbac/user/query",
        logAddress: "127.0.0.1",
        createTime: now,
        updateTime: now
    }
];