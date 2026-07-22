/// <reference types="vite/client" />

// 重写 vite-mock-plugin 中MockMethod 的声明
declare module 'vite-plugin-mock' {
    declare interface MockMethod {
    url: string;
    method?: MethodType;
    timeout?: number;
    statusCode?: number;
    response?: ((this: RespThisType, opt: {
        url: Recordable;
        body: Recordable;
        query: Recordable;
        headers: Recordable;
    }) => unknown) ;
    rawResponse?: (this: RespThisType, req: IncomingMessage, res: ServerResponse) => void;
}
}
