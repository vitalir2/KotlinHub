import {Stack, Typography} from "@mui/material";
import {Repository} from "../../Repository";
import {GetCodeButton} from "../GetCodeButton";
import {Outlet, useParams} from "react-router-dom";
import {SegmentsView} from "./SegmentsView";

export interface RepositoryMainInfoProps {
    repository: Repository,
}

export function RepositoryMainInfo(props: RepositoryMainInfoProps) {
    const params = useParams();
    const path = params['*'];
    const {repository} = props

    let currentPathSegments: string[] | undefined = undefined
    if (path !== undefined) {
        currentPathSegments = [repository.name, ...path.split('/')]
    }
    return (
        <Stack spacing={1}>
            <Stack
                direction={"row"}
                spacing={1}
                justifyContent={'start'}
                alignItems={'center'}
            >
                <Typography variant={"subtitle2"} sx={{marginRight: "auto"}}>
                    Branch: master
                </Typography>
                {currentPathSegments !== undefined &&
                    <SegmentsView segments={currentPathSegments}/>
                }
                <GetCodeButton repository={repository}/>
            </Stack>
            <Outlet/>
        </Stack>
    )
}
