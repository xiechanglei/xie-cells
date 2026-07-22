import {MockMethod} from 'vite-plugin-mock'
import {buildErrorResponse, buildSuccessResponse, now} from "@/mock/util_mock";

const user_data = [
    {
        id: "user001", userName: 'admin', userPassword: '123456', userStatus: "ENABLED", nickName: 'admin', phoneNumber: '12345678901', email: 'admin@example.com', address: 'Beijing', userSerial: 1, feature: "uuid-1", createTime: now, updateTime: now
    }
]

const login: MockMethod = {
    url: '/rbac/auth/login',
    method: 'post',
    response: ({body}) => {
        const {userName, userPassword} = body
        const user = user_data.find(u => u.userName === userName && u.userPassword === userPassword)
        if (user) {
            const token = "mock-token-" + user.id
            return buildSuccessResponse(token)
        } else {
            return buildErrorResponse("用户名或密码错误")
        }

    },
}
export default [login]