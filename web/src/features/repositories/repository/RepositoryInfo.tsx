import {Grid, Stack, Typography} from "@mui/material";
import {Repository} from "../Repository";
import {RepositoryMainInfo} from "./RepositoryMainInfo";

export interface RepositoryInfoProps {
    repository: Repository,
}

export function RepositoryInfo(props: RepositoryInfoProps) {
    const {repository} = props
    let description: string
    if (repository.description === undefined) {
        description = "No description"
    } else {
        description = repository.description
    }
    return (
        <Grid container spacing={2} sx={{
            height: "100%",
        }}>
            <Grid item xs={3}></Grid>
            <Grid item xs={6}>
                <RepositoryMainInfo repository={repository}/>
            </Grid>
            <Grid item xs={3}>
                <Stack spacing={1}>
                    <Typography variant={"subtitle1"}>
                        About
                    </Typography>
                    <Typography variant={"body1"}>
                        {description}
                    </Typography>
                </Stack>
            </Grid>
        </Grid>
    )
}
