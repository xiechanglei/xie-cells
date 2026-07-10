import {publish, subscribe} from "pubsub-js";
import {useMount} from "@/hooks/react/effect.lifecicle.hook";

export type  MessageType = {
    variant: 'default' | 'error' | 'success' | 'warning' | 'info',
    message: string,
}

/**
 * 使用在根组件中，自己实现通知逻,比如 notistack
 *
 * import {useSnackbar} from "notistack";
 *
 * export const useNotify = () => {
 *     const {enqueueSnackbar} = useSnackbar();
 *     useMount(() => {
 *         subscribe('snack-open', (_msg, data: MessageType) => {
 *             enqueueSnackbar(data);
 *         });
 *     })
 * }
 */
export const useNotify = () => {
    useMount(() => {
        //
        subscribe('snack-open', (_msg, data: MessageType) => {
            // do something to show notification
            console.log("Notification:", _msg, data);
        });
    })
}

// 全局调用，触发通知
export const openNotify = (data: MessageType) => {
    publish("snack-open", data);
}