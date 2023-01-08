import React from 'react';
import logo from './logo.svg';
import { Counter } from './features/counter/Counter';
import './App.css';
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";

const theme = createTheme({
  typography: {
    fontFamily: [
        'Poppins',
        'sans-serif',
    ].join(",")
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: `
        @font-face {
          font-family: 'Poppins';
          font-style: normal;
          font-display: swap;
          font-weight: 400;
          src: url('./files/poppins-devanagari-400-normal.woff2') format('woff2'), url('./files/poppins-all-400-normal.woff') format('woff');
          unicode-range: U+0900-097F,U+1CD0-1CF6,U+1CF8-1CF9,U+200C-200D,U+20A8,U+20B9,U+25CC,U+A830-A839,U+A8E0-A8FB;
        }
        `
    }
  }
});

function App() {
  return (
      <ThemeProvider theme={theme}>
        <CssBaseline/>
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <Counter />
            <p>
              Edit me or <code>src/App.tsx</code> and save to reload.
            </p>
            <span>
          <span>Learn </span>
          <a
              className="App-link"
              href="https://reactjs.org/"
              target="_blank"
              rel="noopener noreferrer"
          >
            React
          </a>
          <span>, </span>
          <a
              className="App-link"
              href="https://redux.js.org/"
              target="_blank"
              rel="noopener noreferrer"
          >
            Redux
          </a>
          <span>, </span>
          <a
              className="App-link"
              href="https://redux-toolkit.js.org/"
              target="_blank"
              rel="noopener noreferrer"
          >
            Redux Toolkit
          </a>
          ,<span> and </span>
          <a
              className="App-link"
              href="https://react-redux.js.org/"
              target="_blank"
              rel="noopener noreferrer"
          >
            React Redux
          </a>
        </span>
          </header>
        </div>
      </ThemeProvider>
  );
}

export default App;
