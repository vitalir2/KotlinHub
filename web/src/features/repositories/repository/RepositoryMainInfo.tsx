import {Stack, Typography} from "@mui/material";
import {Repository} from "../Repository";
import {GetCodeButton} from "./GetCodeButton";

export interface RepositoryMainInfoProps {
    repository: Repository,
}

export function RepositoryMainInfo(props: RepositoryMainInfoProps) {
    const {repository} = props

    return (
        <Stack spacing={1}>
            <Stack direction={"row"} spacing={3}>
                <Typography variant={"subtitle2"}>
                    Branch: master
                </Typography>
                {GetCodeButton(repository)}
            </Stack>
        </Stack>
    )
}
