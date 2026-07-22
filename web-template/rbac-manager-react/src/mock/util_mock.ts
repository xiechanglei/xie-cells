export const now = new Date().getTime().toString();

export const buildSuccessResponse = (data: unknown) => {
    return {
        code: 0,
        success: true,
        msg: "success",
        data,
    }
}

export const buildErrorResponse = (msg: string, code: number = -1) => {
    return {
        code,
        success: false,
        msg,
        data: null,
    }
}