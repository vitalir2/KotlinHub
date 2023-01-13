import React from 'react';
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import {Main} from "./features/main/Main";

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
            <Main/>
        </ThemeProvider>
    );
}

export default App;
