import {Box, Divider, Stack} from "@mui/material";
import React from "react";
import {Repository as RepositoryModel} from "../repositories/Repository";
import {Repository} from "./Repository";

export interface RepositoriesProps {
    repositories: RepositoryModel[],
}


export function Repositories(props: RepositoriesProps) {
    const {repositories} = props

    return (
        <Stack spacing={1}>
            {repositories.map(repository =>
                <RepositoriesItem key={repository.name} repository={repository}/>
            )}
        </Stack>
    )
}

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
