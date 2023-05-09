import {Box, Chip, Link, Stack, SxProps, Typography} from "@mui/material";
import { Link as RouterLink } from "react-router-dom"
import {Repository as RepositoryModel} from "../repositories/Repository";

export interface RepositoryProps {
    repository: RepositoryModel,
}

const repositoryLinkStyle: SxProps = {
    textDecoration: "none",
    color: "secondary.dark",
    ":visited": {
        color: "secondary.dark",
    },
    ":hover": {
        color: "secondary.main",
    },
}

export function Repository(props: RepositoryProps) {
    const {repository} = props

    return (
        <Stack spacing={0.5}>
            <Stack spacing={1} direction={"row"}>
                <Typography component={RouterLink}
                            sx={repositoryLinkStyle}
                            variant={"h6"}
                            to={`/repositories/${repository.name}`}
                >
                    {repository.name}
                </Typography>
                <Chip label={repository.accessMode} variant={"outlined"} color={"secondary"} size={"small"}/>
            </Stack>
            <Box>
                {repository.description?.length &&
                    <Typography variant={"body2"}>
                        {repository.description}
                    </Typography>
                }
            </Box>
        </Stack>
    )
}
