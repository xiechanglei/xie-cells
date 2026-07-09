# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react/README.md) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type aware lint rules:

- Configure the top-level `parserOptions` property like this:

```js
export default tseslint.config({
  languageOptions: {
    // other options...
    parserOptions: {
      project: ['./tsconfig.node.json', './tsconfig.app.json'],
      tsconfigRootDir: import.meta.dirname,
    },
  },
})
```

- Replace `tseslint.configs.recommended` to `tseslint.configs.recommendedTypeChecked` or `tseslint.configs.strictTypeChecked`
- Optionally add `...tseslint.configs.stylisticTypeChecked`
- Install [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react) and update the config:

```js
// eslint.config.js
import react from 'eslint-plugin-react'

export default tseslint.config({
  // Set the react version
  settings: { react: { version: '18.3' } },
  plugins: {
    // Add the react plugin
    react,
  },
  rules: {
    // other rules...
    // Enable its recommended rules
    ...react.configs.recommended.rules,
    ...react.configs['jsx-runtime'].rules,
  },
})
```



## project ui framework: chakra-ui

Chakra UI works in your favorite framework. We've put together step-by-step guides for these frameworks

### installation:

To manually set up Chakra UI in your project, follow the steps below.

```bash   
# Install @chakra-ui/react
yarn add @chakra-ui/react @emotion/react

# add snippets
npx @chakra-ui/cli snippet add
```

> Snippets are pre-built components that you can use to build your UI faster. Using the @chakra-ui/cli you can add snippets to your project.

###  Setup provider

Wrap your application with the Provider component generated in the components/ui/provider component at the root of your application.

This provider composes the following:

- ChakraProvider from @chakra-ui/react for the styling system
- ThemeProvider from next-themes for color mode

```typescript jsx
import { Provider } from "@/components/ui/provider"

function App({ Component, pageProps }) {
  return (
    <Provider>
      <Component {...pageProps} />
    </Provider>
  )
}
```

### Update tsconfig

If you're using TypeScript, you need to update the compilerOptions in the tsconfig file to include the following options:

```json
{
  "compilerOptions": {
    "module": "ESNext",
    "moduleResolution": "Bundler",
    "skipLibCheck": true,
    "paths": {
      "@/*": ["./src/*"]
    }
  }
}
```

> If you're using JavaScript, create a jsconfig.json file and add the above code to the file.

### Enjoy!

With the power of the snippets and the primitive components from Chakra UI, you can build your UI faster.

```typescript jsx
import { Button, HStack } from "@chakra-ui/react"

const Demo = () => {
  return (
    <HStack>
      <Button>Click me</Button>
      <Button>Click me</Button>
    </HStack>
  )
}
```