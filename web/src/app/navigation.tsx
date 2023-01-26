import {LoginPage} from "../features/login/LoginPage";
import {createBrowserRouter} from "react-router-dom";
import {Router as RemixRouter} from "@remix-run/router/dist/router";
import App from "../App";
import {MainPage} from "../features/main/MainPage";

export function createAppRouter(): RemixRouter {
    return createBrowserRouter([
        {
            path: "/",
            element: <App/>,
            children: [
                {
                   index: true,
                   element: <LoginPage/>,
                },
                {
                    path: "main/", // TODO
                    element: <MainPage/>,
                },
            ],
        }
    ])
}
