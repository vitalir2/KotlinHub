import React from 'react';
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {Outlet} from "react-router-dom";
import {Provider} from "react-redux";
import {store} from "./app/store";

export const appTheme = createTheme({
    palette: {
        primary: {
            main: '#ffcf52',
        },
        secondary: {
            main: '#2373BC',
        },
    },
});

function App() {
    return (
        <Provider store={store}>
            <ThemeProvider theme={appTheme}>
                <CssBaseline/>
                <Outlet/>
            </ThemeProvider>
        </Provider>
    );
}

export default App;
