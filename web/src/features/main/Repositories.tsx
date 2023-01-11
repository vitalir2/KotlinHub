import {Box, Divider, Stack, Typography} from "@mui/material";
import {Repository} from "./Repository";
import React from "react";

function RepositoriesPlaceholder() {
    return (
        <Box>
            <Typography variant={"body1"}>
                No repositories yet
            </Typography>
        </Box>
    );
}

interface RepositoriesItemProps {
    repository: string,
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
    repositories: string[],
}

export function Repositories(props: RepositoriesProps) {
    const {repositories} = props

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
            {repositories.map(repository => <RepositoriesItem repository={repository}/>)}
        </Stack>
    )
}
