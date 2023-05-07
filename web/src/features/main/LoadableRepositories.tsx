import {Box, CircularProgress, Stack, SxProps, Typography} from "@mui/material";
import React, {ReactElement} from "react";
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
            body = RepositoriesLoadingPlaceholder()
            break
        case "loaded":
            const repositories = loadableRepositories.data
            if (repositories.length === 0) {
                body = RepositoriesEmptyPlaceholder()
            } else {
                body = <Repositories repositories={repositories}/>
            }
            break
        case "error":
            body = RepositoriesErrorPlaceholder()
            break
    }

    return <Box sx={{
        padding: 3,
        "@media (min-width: 780px)": {
            minWidth: "40%",
        },
    }}>
        {body}
    </Box>
}

const placeholderContainerStyle: SxProps = {
    width: "100%",
    height: "100%",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
}

function RepositoriesLoadingPlaceholder() {
    return (
        <Box sx={placeholderContainerStyle}>
            <CircularProgress/>
        </Box>
    )
}

function RepositoriesEmptyPlaceholder() {
    return (
        <Box sx={placeholderContainerStyle}>
            <Typography variant={"body1"}>
                No repositories yet
            </Typography>
        </Box>
    );
}

function RepositoriesErrorPlaceholder() {
    return (
        <Box sx={placeholderContainerStyle}>
            <Typography variant={"h6"} align={"center"}>
                Error occured, please try later
            </Typography>
        </Box>
    )
}
