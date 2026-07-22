import {useEffect} from 'react';
import {usePathname} from '@/router/hooks';
import {ThemeProvider} from '@/theme/theme-provider';
import {ReactNode} from "react";

function useScrollToTop() {
    const pathname = usePathname();

    useEffect(() => {
        window.scrollTo(0, 0)
    }, [pathname]);

    return null;
}


export const App = ({children}: { children: ReactNode }) => {
    useScrollToTop()
    return (
        <ThemeProvider>
            {children}
        </ThemeProvider>
    );
}


