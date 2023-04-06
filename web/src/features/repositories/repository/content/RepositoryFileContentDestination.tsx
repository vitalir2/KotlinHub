import {useParams} from "react-router-dom";
import {Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {appGraph} from "../../../../app/dependency_injection";

export function RepositoryFileContentDestination() {
    const params = useParams();
    const repositoryId = params["repositoryId"];
    const path = params["*"];

    const [fileContent, setFileContent]  = useState("");
    useEffect(() => {
        if (repositoryId === undefined || path === undefined) {
            return;
        }
        const fetchFileContent = async () => {
            return appGraph.repositoriesGraph.repositoriesRepository.getRepositoryFileContent(
                "current", repositoryId, path,
            )
        }
        fetchFileContent()
            .then(fileContent => setFileContent(fileContent))
    }, [repositoryId, path])

    return (
        <>
            <Typography variant={"subtitle1"}>
                You opened file={path}
            </Typography>
            <Typography variant={"body1"}>
                Content: {fileContent}
            </Typography>
        </>
    );
}
