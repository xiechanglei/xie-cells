import {SignInView} from '@/sections/auth';

// ----------------------------------------------------------------------

import {useLanguage} from '@/store/language';

export default function Page() {
    const {language} = useLanguage()
    return (
        <>
            <title>{language.login}</title>
            <SignInView/>
        </>
    );
}
