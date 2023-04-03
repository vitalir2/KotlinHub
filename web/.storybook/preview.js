import {CssBaseline, ThemeProvider} from "@mui/material";
import React from "react";
import {appTheme} from "../src/App";
import {MemoryRouter} from "react-router-dom";

export const parameters = {
    actions: {argTypesRegex: "^on[A-Z].*"},
    controls: {
        matchers: {
            color: /(background|color)$/i,
            date: /Date$/,
        },
    },
}

export const decorators = [
    (Story) => (
        <MemoryRouter>
            <Story/>
        </MemoryRouter>
    ),
    (Story) => (
        <ThemeProvider theme={appTheme}>
            <CssBaseline/>
            <Story/>
        </ThemeProvider>
    ),
]
