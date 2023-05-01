import {createBrowserRouter, createHashRouter} from "react-router-dom";
import {Router as RemixRouter} from "@remix-run/router/dist/router";
import App from "../App";
import {RepositoryPage} from "../features/repositories/repository/RepositoryPage";
import {RepositoryContentDestination} from "../features/repositories/repository/content/RepositoryContentDestination";
import {
    RepositoryFileContentDestination
} from "../features/repositories/repository/content/RepositoryFileContentDestination";
import {RootPage} from "../features/root/RootPage";
import {CreateRepositoryPage} from "../features/repositories/repository/create/CreateRepositoryPage";
import {RegistrationPage} from "../features/auth/registration/RegistrationPage";
import {isProductionBackend, isProductionEnv} from "./environment";

export function createAppRouter(): RemixRouter {
    const routes = [
        {
            path: "/",
            element: <App/>,
            children: [
                {
                    index: true,
                    element: <RootPage/>,
                },
                {
                    path: "register",
                    element: <RegistrationPage/>,
                },
                {
                    path: "repositories/create",
                    element: <CreateRepositoryPage/>,
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
    ];
    if (isProductionBackend()) {
        return createHashRouter(routes);
    } else {
        return createBrowserRouter(routes);
    }
}
