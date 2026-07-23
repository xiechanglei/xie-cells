type DictName = 'login' | "account" | "account_input_placeholder" | "password" | "password_input_placeholder";
type LangDict = Record<DictName, string>;
type LangInfo = {
    value: string;
    label: string;
    icon: string;
    dict: LangDict;
};