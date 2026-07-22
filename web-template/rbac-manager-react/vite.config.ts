import {defineConfig, PluginOption} from 'vite'
import react, {reactCompilerPreset} from '@vitejs/plugin-react'
import {viteMockServe} from 'vite-plugin-mock'
import inspect from 'vite-plugin-inspect'
import {resource_root} from "./src/config";
import babel from '@rolldown/plugin-babel';
// https://vite.dev/config/
// vite 插件
const plugins: PluginOption[] = []

// 使用react
plugins.push(react())

// 使用babel
plugins.push(babel({
    presets: [reactCompilerPreset()],
    plugins: [
        ['@babel/plugin-proposal-decorators', {version: "2023-11"}],
        ['@babel/plugin-transform-class-properties', {loose: true}]
    ]
}))

// inspect插件,用于调试vite的插件
plugins.push(inspect())

// 是否启用mock,用于模拟接口，启动的使用使用yarn mock
const mock = process.env.VITE_ENABLE_MOCK === 'true';
if (mock) {
    console.log("启用mock,用于模拟接口，启动的使用使用yarn mock")
    plugins.push(viteMockServe({
        mockPath: 'src/mock',
        enable: true,
        watchFiles: true,
        // 忽略util_开头的文件
        ignore: /^util_/,
    }))
}

export default defineConfig({
    plugins,
    base: resource_root,
    // alias
    resolve: {
        alias: {
            '@': '/src'
        }
    },
    server: {
        host: '0.0.0.0',
        proxy: {
            '/api': {
                target: 'http://127.0.0.1:8080', // 代理目标地址
                changeOrigin: true, // 是否改变源
            }
        }
    }
})
