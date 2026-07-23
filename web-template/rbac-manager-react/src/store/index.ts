import {configureStore} from '@reduxjs/toolkit'
import langReducer from './language'

const store = configureStore({
    reducer: {
        lang: langReducer,
    },
})

export default store