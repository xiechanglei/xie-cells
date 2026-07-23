import {createSlice} from '@reduxjs/toolkit'
import {zhCn} from "./language.zh-cn"
import {en} from "./language.en"
import {useDispatch, useSelector} from "react-redux";


/**
 * 语言配置
 */
export const supportLangs: LangInfo[] = [zhCn, en]

/**
 * 从localStorage中获取默认语言，如果不存在则使用中文
 */
const storedLang = localStorage.getItem('xie-cell-rbac-lang')
const defaultLang = storedLang ? supportLangs.find(lang => lang.value === storedLang) as LangInfo : zhCn

/**
 * 使用全局状态@reduxjs/toolkit管理语言
 */
const langSlice = createSlice({
    name: 'lang',
    initialState: {lang: defaultLang},
    reducers: {
        setLanguage: (state: { lang: LangInfo }, action: { payload: LangInfo }) => {
            state.lang = action.payload
        }
    }
})

export const useLanguage = () => {
    // @ts-ignore
    const language = useSelector((state) => state.lang.lang.dict) as LangDict
    const dispatch = useDispatch()
    const setLanguage = (lang: LangInfo) => {
        dispatch(langSlice.actions.setLanguage(lang))
        localStorage.setItem('xie-cell-rbac-lang', lang.value)
    }
    return {
        language,
        setLanguage
    }
}

export default langSlice.reducer


