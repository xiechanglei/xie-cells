import {defineConfig} from 'vite'
import react, {reactCompilerPreset} from '@vitejs/plugin-react'
import inspect from 'vite-plugin-inspect'
import {resource_root} from "./src/config/config.ts";
import babel from '@rolldown/plugin-babel';
// https://vite.dev/config/
export default defineConfig({
    plugins: [
        react(),
        babel({
            presets: [reactCompilerPreset()],
            plugins: [
                ['@babel/plugin-proposal-decorators', {version: "2023-11"}],
                ['@babel/plugin-transform-class-properties', {loose: true}]
            ]
        }),
        inspect()
    ],
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
