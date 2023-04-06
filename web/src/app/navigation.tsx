import {LoginPage} from "../features/login/LoginPage";
import {createBrowserRouter} from "react-router-dom";
import {Router as RemixRouter} from "@remix-run/router/dist/router";
import App from "../App";
import {MainPage} from "../features/main/MainPage";
import {RepositoryPage} from "../features/repositories/repository/RepositoryPage";
import {RepositoryContentDestination} from "../features/repositories/repository/content/RepositoryContentDestination";
import {
    RepositoryFileContentDestination
} from "../features/repositories/repository/content/RepositoryFileContentDestination";

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
                {
                    path: "repositories/:repositoryId/",
                    element: <RepositoryPage/>,
                    children: [
                        {
                            index: true,
                            element: <RepositoryContentDestination/>
                        },
                        {
                            path: "tree/*",
                            element: <RepositoryContentDestination/>
                        },
                        {
                            path: "content/*",
                            element: <RepositoryFileContentDestination/>,
                        }
                    ]
                }
            ],
        }
    ])
}
