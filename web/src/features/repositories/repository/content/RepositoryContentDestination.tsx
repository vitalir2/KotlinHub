import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RepositoryFile} from "../../RepositoryFile";
import {appGraph} from "../../../../app/dependency_injection";
import {RepositoryContent} from "./RepositoryContent";

export function RepositoryContentDestination() {
    const {repositoryId, path} = useParams()
    const [repositoryFiles, setRepositoryFiles] = useState<RepositoryFile[]>([])

    useEffect(() => {
        if (repositoryId === undefined) {
            return
        }
        const fetchFiles = async () => {
            return await appGraph.repositoriesGraph.repositoriesRepository.getRepositoryFiles(
                "current", repositoryId, path ?? ""
            )
        }
        fetchFiles().then(files => setRepositoryFiles(files))

    }, [repositoryId, path])

    return (
        <>
            <RepositoryContent repositoryFiles={repositoryFiles}/>
        </>
    )
}
