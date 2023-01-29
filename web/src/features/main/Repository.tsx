import {Box, Chip, Stack, Typography} from "@mui/material";
import {Repository as RepositoryModel} from "../repositories/Repository";

export interface RepositoryProps {
    repository: RepositoryModel,
}

export function Repository(props: RepositoryProps) {
    const {repository} = props

    return (
        <Stack spacing={0.5}>
            <Stack spacing={1} direction={"row"}>
                <Typography variant={"h6"}>
                    {repository.name}
                </Typography>
                <Chip label={repository.accessMode} variant={"outlined"} color={"secondary"} size={"small"}/>
            </Stack>
            <Box>
                {(() => {
                    if (repository.description?.length === undefined) {
                        return null
                    }
                    return (
                        <Typography variant={"body2"}>
                            {repository.description}
                        </Typography>
                    )
                })()}
            </Box>
            <Box>
                <Typography variant={"body2"}>
                    updated today
                </Typography>
            </Box>
        </Stack>
    )
}
