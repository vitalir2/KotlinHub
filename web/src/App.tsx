import React from 'react';
import {Box, createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {LoginPage} from "./features/login/LoginPage";

const theme = createTheme({
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
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <LoginPage/>
        </ThemeProvider>
    );
}

export default App;
