import {ReactNode} from "react";
import {Provider} from 'react-redux'
import store from '@/store'
import {useScrollToTop} from "@/hooks/dom/scroll";
import {ThemeProvider} from "@/theme";

export const App = ({children}: { children: ReactNode }) => {
    useScrollToTop()
    return (
        <Provider store={store}>
            <ThemeProvider>
                {children}
            </ThemeProvider>
        </Provider>
    );
}



