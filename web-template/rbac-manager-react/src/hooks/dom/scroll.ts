import {usePathname} from "@/hooks/router";
import {useEffect} from "react";

export const useScrollToTop = () => {
    const pathname = usePathname();

    useEffect(() => {
        window.scrollTo(0, 0)
    }, [pathname]);

    return null;
}