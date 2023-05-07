import {createBrowserRouter} from "react-router-dom";
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
import {UserSettingsPage} from "../features/auth/settings/UserSettingsPage";

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
                    path: "settings",
                    element: <UserSettingsPage/>,
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
    return createBrowserRouter(routes);
}
