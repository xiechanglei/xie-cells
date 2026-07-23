import {useEffect} from "react";

/**
 * Sets the document title to the specified value.
 */
export const useDocumentTitle = (title: string) => {
    useEffect(() => {
        const originalTitle = document.title;
        document.title = title;

        return () => {
            document.title = originalTitle;
        };
    }, [title]);
}