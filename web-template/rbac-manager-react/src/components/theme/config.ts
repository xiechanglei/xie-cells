"use client"

import {defaultConfig, defineConfig, createSystem} from "@chakra-ui/react"

/**
 * This is the configuration for the Chakra UI theme.
 */
const config = defineConfig({
    theme: {
        tokens: {
            colors: {
                // primary: { value: "#ff0000" },
                // secondary: { value: "#EE0F0F" },
            },
            fonts: {
                // body: { value: "Inter, sans-serif" },
            },
        },
    },
})

export const ui_system = createSystem(defaultConfig, config)