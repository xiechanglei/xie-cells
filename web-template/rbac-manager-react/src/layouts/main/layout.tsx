import {FC, FragmentProps} from "react";

/**
 * 主页面布局
 * @constructor
 */




export const MainLayout: FC<FragmentProps> = ({children}) => {
    return (
        <div>
            {children}
        </div>
    );
}
