import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {appGraph} from "../../../../app/dependency_injection";
import {ErrorPlaceholder} from "../../../../core/view/placeholder/ErrorPlaceholder";
import {RepositoryFileContent} from "./RepositoryFileContent";

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

    if (path === undefined) {
        return (
            <>
                <ErrorPlaceholder error={"Unknown error"}/>
            </>
        );
    }

    const fileName = path.substring(path.lastIndexOf("/") + 1);
    return (
        <RepositoryFileContent fileName={fileName} fileContent={fileContent}/>
    );
}
