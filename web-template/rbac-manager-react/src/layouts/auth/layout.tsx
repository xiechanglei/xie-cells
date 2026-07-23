import {FC, FragmentProps} from "react";

/**
 * 登录授权等页面的布局
 * @param param0
 * @param param0.children
 * @constructor
 */
export const AuthLayout:FC<FragmentProps> = ({children}) => {
    return (
        <div>
            {children}
        </div>
    );
}
