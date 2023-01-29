import {Box, CircularProgress, Divider, Stack, Typography} from "@mui/material";
import {Repository} from "./Repository";
import React from "react";
import {RepositoriesPlaceholder} from "./RepositoriesPlaceholder";
import {Repository as RepositoryModel} from "../repositories/Repository";

interface RepositoriesItemProps {
    repository: RepositoryModel,
}

function RepositoriesItem(props: RepositoriesItemProps) {
    const {repository} = props
    return (
        <Box>
            <Repository repository={repository}/>
            <Divider sx={{
                marginTop: 1,
            }}/>
        </Box>
    )
}

export interface RepositoriesProps {
    repositories: RepositoryModel[],
    isLoading: boolean,
    errorText?: string,
}

export function Repositories(props: RepositoriesProps) {
    const {repositories, isLoading, errorText} = props

    if (isLoading) {
        return (
            <Stack spacing={0.5}>
                <Typography variant={"h6"}>Loading repositories..</Typography>
                <CircularProgress/>
            </Stack>
        )
    }

    if (errorText !== undefined) {
        return (
            <Box>
                <Typography variant={"h6"}>
                    Error occured, please try later
                </Typography>
            </Box>
        )
    }

    if (repositories.length === 0) {
        return RepositoriesPlaceholder()
    }

    return (
        <Stack
            spacing={1}
            sx={{
                width: "100%",
                height: "100%",
                padding: 3,
            }}
        >
            {repositories.map(repository => <RepositoriesItem key={repository.name} repository={repository}/>)}
        </Stack>
    )
}
