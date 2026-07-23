import {MockMethod} from 'vite-plugin-mock'
import {buildSuccessResponse} from "./util_mock";
import {logData, userData} from "./util_data";

const logMethods: MockMethod[] = [
    // 查询日志
    {
        url: '/rbac/log/query',
        method: 'get',
        response: ({ query }) => {
            const { userId, nickName, logTitle, logPath, logAddress } = query || {};
            
            let filteredLogs = logData;
            
            if (userId) {
                filteredLogs = filteredLogs.filter(log => log.userId === userId);
            }
            
            if (nickName) {
                const user = userData.find(u => u.nickName.includes(nickName));
                if (user) {
                    filteredLogs = filteredLogs.filter(log => log.userId === user.id);
                }
            }
            
            if (logTitle) {
                filteredLogs = filteredLogs.filter(log => log.logTitle.includes(logTitle));
            }
            
            if (logPath) {
                filteredLogs = filteredLogs.filter(log => log.logPath.includes(logPath));
            }
            
            if (logAddress) {
                filteredLogs = filteredLogs.filter(log => log.logAddress.includes(logAddress));
            }
            
            // 模拟分页 - 只返回前10条记录
            return buildSuccessResponse({
                content: filteredLogs.slice(0, 10),
                totalElements: filteredLogs.length,
                totalPages: Math.ceil(filteredLogs.length / 10),
                number: 0,
                size: 10
            });
        }
    }
];

export default logMethods;