import type {Theme} from '@mui/material/styles';
import {createTheme as createMuiTheme} from '@mui/material/styles';

import {components, customShadows, palette, shadows, typography} from '@/theme/core';
import {themeConfig} from './theme-config';

import type {ThemeOptions} from './types';

// ----------------------------------------------------------------------

export const baseTheme: ThemeOptions = {
  colorSchemes: {
    light: {
      palette: palette.light,
      shadows: shadows.light,
      customShadows: customShadows.light,
    },
  },
  components,
  typography,
  shape: { borderRadius: 8 },
  cssVariables: themeConfig.cssVariables,
};

// ----------------------------------------------------------------------

type CreateThemeProps = {
  themeOverrides?: ThemeOptions;
};

export function createTheme({ themeOverrides = {} }: CreateThemeProps = {}): Theme {
  return createMuiTheme(baseTheme, themeOverrides);
}
