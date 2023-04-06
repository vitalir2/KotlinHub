import {useParams} from "react-router-dom";
import {Typography} from "@mui/material";

export function RepositoryFileContentDestination() {
    const params = useParams();
    const path = params["*"];

    return (
        <>
            <Typography variant={"subtitle1"}>
                You opened file={path}
            </Typography>
        </>
    );
}
