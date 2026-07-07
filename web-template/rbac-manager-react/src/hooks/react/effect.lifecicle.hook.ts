import {EffectCallback, useEffect} from "react";

/**
 * 组件挂载的时候执行
 * @param callback 挂载时执行的回调
 */
export const useMount = (callback: EffectCallback) => useEffect(callback, []);


/**
 * 组件卸载的时候执行
 * @param callback 卸载时执行的回调
 */
export const useUnmount = (callback: () => void) => useEffect(() => {
    return callback;
},[]);
