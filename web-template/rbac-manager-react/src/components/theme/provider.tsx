"use client"

import {ChakraProvider} from "@chakra-ui/react"
import {ColorModeProvider, type ColorModeProviderProps,} from "@/components/ui/color-mode";
import {ui_system} from "./config";

export function Provider(props: ColorModeProviderProps) {
    return (
        <ChakraProvider value={ui_system}>
            <ColorModeProvider {...props} />
        </ChakraProvider>
    )
}
