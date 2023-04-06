import {useParams} from "react-router-dom";
import {Typography} from "@mui/material";
import {useEffect, useState} from "react";
import {appGraph} from "../../../../app/dependency_injection";
import SyntaxHighlighter from 'react-syntax-highlighter';
import {ErrorPlaceholder} from "../../../../core/view/placeholder/ErrorPlaceholder";

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
    const fileExtensionSeparatorIndex = fileName.lastIndexOf(".");
    let fileExtension: string;
    if (fileExtensionSeparatorIndex > 0) {
        fileExtension = fileName.substring(fileExtensionSeparatorIndex + 1);
    } else {
        fileExtension = "";
    }

    let contentLanguage: string;
    switch (fileExtension) {
        case "js":
        case "jsx":
            contentLanguage = "javascript";
            break;
        case "ts":
        case "tsx":
            contentLanguage = "typescript";
            break;
        case "kt":
            contentLanguage = "kotlin";
            break;
        default:
            contentLanguage = "text";
            break;
    }

    return (
        <>
            <Typography variant={"subtitle1"}>
                You opened file={path}
            </Typography>
            <SyntaxHighlighter language={contentLanguage}>
                {fileContent}
            </SyntaxHighlighter>
        </>
    );
}
