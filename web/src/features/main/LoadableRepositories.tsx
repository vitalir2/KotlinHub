import {Box, CircularProgress, Stack, Typography} from "@mui/material";
import React, {ReactElement} from "react";
import {RepositoriesPlaceholder} from "./RepositoriesPlaceholder";
import {Repository as RepositoryModel} from "../repositories/Repository";
import {Loadable} from "../../core/models/Loadable";
import {Repositories} from "./Repositories";

export interface LoadableRepositoriesProps {
    loadableRepositories: Loadable<RepositoryModel[]>,
}

export function LoadableRepositories(props: LoadableRepositoriesProps) {
    const {loadableRepositories} = props

    let body: ReactElement
    switch (loadableRepositories.kind) {
        case "loading":
                body = <Box sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    width: "100%",
                    height: "100vh",
                }}>
                    <CircularProgress/>
                </Box>
            break
        case "loaded":
            const repositories = loadableRepositories.data

            if (repositories.length === 0) {
                body = RepositoriesPlaceholder()
            } else {
                body = <Repositories repositories={repositories}/>
            }
            break
        case "error":
            body = (
                <Box>
                    <Typography variant={"h6"}>
                        Error occured, please try later
                    </Typography>
                </Box>
            )
            break
    }

    return <Box sx={{
        width: "100%",
        height: "100%",
        padding: 3,
    }}>
        {body}
    </Box>
}
