import {Grid, SxProps} from "@mui/material";
import {Repository} from "../Repository";
import {RepositoryMainInfo} from "./RepositoryMainInfo";
import {RepositoryMetaInfo} from "./RepositoryMetaInfo";

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
        <Grid container spacing={2} sx={repositoryInfoStyle}>
            <Grid item xs={3}>{/* Empty space */} </Grid>
            <Grid item xs={6}>
                <RepositoryMainInfo repository={repository}/>
            </Grid>
            <Grid item xs={3}>
                {RepositoryMetaInfo(description)}
            </Grid>
        </Grid>
    )
}

const repositoryInfoStyle: SxProps = {
    height: "100%",
}
