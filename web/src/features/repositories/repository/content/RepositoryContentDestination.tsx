import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RepositoryFile, RepositoryFileType} from "../../RepositoryFile";
import {appGraph} from "../../../../app/dependency_injection";
import {RepositoryContent} from "./RepositoryContent";

export function RepositoryContentDestination() {
    const params = useParams();
    const repositoryId = params['repositoryId'];
    const path = params['*'];
    const [repositoryFiles, setRepositoryFiles] = useState<RepositoryFile[]>([])

    const currentPath = path ?? ""
    console.log("Current path = " + currentPath)
    useEffect(() => {
        if (repositoryId === undefined) {
            return
        }
        const fetchFiles = async () => {
            return await appGraph.repositoriesGraph.repositoriesRepository.getRepositoryFiles(
                "current", repositoryId, currentPath
            )
        }
        fetchFiles()
            .then(files => setRepositoryFiles(
                files.sort((lhs, rhs) => {
                    if (lhs.type === rhs.type) {
                        return lhs.name.localeCompare(rhs.name)
                    } else if (lhs.type === RepositoryFileType.FOLDER) {
                        return -1
                    } else {
                        return 1
                    }
                }))
            )

    }, [repositoryId, currentPath])

    return (
        <>
            <RepositoryContent
                repositoryFiles={repositoryFiles}
                path={currentPath}
            />
        </>
    )
}
