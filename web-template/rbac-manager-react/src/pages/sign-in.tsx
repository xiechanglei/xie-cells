import {useLanguage} from '@/store/language';
import {useDocumentTitle} from "@/hooks/dom/document.title";

// ----------------------------------------------------------------------


export default function Page() {
    const {language} = useLanguage()
    useDocumentTitle(language.login);
    return (
        <>
            hello world
        </>
    );
}
