import React from 'react';
import {createTheme, CssBaseline, responsiveFontSizes, Stack, ThemeProvider} from "@mui/material";
import {Outlet} from "react-router-dom";
import {Provider} from "react-redux";
import {store} from "./app/store";
import {KotlinHubToolbarRedux} from "./core/view/toolbar/KotlinHubToolbar";

let theme = createTheme({
    palette: {
        primary: {
            main: '#ffcf52',
        },
        secondary: {
            main: '#2373BC',
        },
    },
});
export const appTheme = responsiveFontSizes(theme);

function App() {
    return (
        <Provider store={store}>
            <ThemeProvider theme={appTheme}>
                <CssBaseline/>
                <Stack direction={"column"}>
                    <KotlinHubToolbarRedux/>
                    <Outlet/>
                </Stack>
            </ThemeProvider>
        </Provider>
    );
}

export default App;
