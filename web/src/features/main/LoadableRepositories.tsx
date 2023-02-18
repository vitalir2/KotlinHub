import {Box, CircularProgress, Stack, Typography} from "@mui/material";
import React from "react";
import {RepositoriesPlaceholder} from "./RepositoriesPlaceholder";
import {Repository as RepositoryModel} from "../repositories/Repository";
import {Loadable} from "../../core/models/Loadable";
import {Repositories} from "./Repositories";

export interface LoadableRepositoriesProps {
    loadableRepositories: Loadable<RepositoryModel[]>,
}

export function LoadableRepositories(props: LoadableRepositoriesProps) {
    const {loadableRepositories} = props

    switch (loadableRepositories.kind) {
        case "loading":
            return (
                <Stack spacing={0.5}>
                    <CircularProgress/>
                </Stack>
            )
        case "loaded":
            const repositories = loadableRepositories.data

            if (repositories.length === 0) {
                return RepositoriesPlaceholder()
            } else {
                return <Repositories repositories={repositories}/>
            }
        case "error":
            return (
                <Box>
                    <Typography variant={"h6"}>
                        Error occured, please try later
                    </Typography>
                </Box>
            )
    }
}
