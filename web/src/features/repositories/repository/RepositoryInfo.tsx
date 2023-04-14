import {Grid, SxProps} from "@mui/material";
import {Repository} from "../Repository";
import {RepositoryMainInfo} from "./maininfo/RepositoryMainInfo";
import {RepositoryMetaInfo} from "./RepositoryMetaInfo";
import {useLocation, useNavigation} from "react-router-dom";

export interface RepositoryInfoProps {
    repository: Repository,
}

const MAIN_INFO_SIZE_MAX = 10;
const META_INFO_SIZE = 4;

export function RepositoryInfo(props: RepositoryInfoProps) {
    const {repository} = props
    let description: string
    if (repository.description === undefined) {
        description = "No description"
    } else {
        description = repository.description
    }

    const location = useLocation();
    let mainInfoSize: number
    const isDisplayingFile = location.pathname.indexOf(`repositories/${repository.name}/content`) > 0;
    if (isDisplayingFile) {
        mainInfoSize = MAIN_INFO_SIZE_MAX - 1;
    } else {
        mainInfoSize = MAIN_INFO_SIZE_MAX - META_INFO_SIZE;
    }

    return (
        <Grid container spacing={2} columnSpacing={4} sx={repositoryInfoStyle}>
            <Grid item xs={2}>{/* Empty space */} </Grid>
            <Grid item xs={mainInfoSize}>
                <RepositoryMainInfo repository={repository}/>
            </Grid>
            {!isDisplayingFile &&
                <Grid item xs={META_INFO_SIZE}>
                    {RepositoryMetaInfo(description)}
                </Grid>
            }
        </Grid>
    )
}

const repositoryInfoStyle: SxProps = {
    height: "100%",
}
