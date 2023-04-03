import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RepositoryFile} from "../../RepositoryFile";
import {appGraph} from "../../../../app/dependency_injection";
import {RepositoryContent} from "./RepositoryContent";

export function RepositoryContentDestination() {
    const {userId, repositoryId, path} = useParams()
    const [repositoryFiles, setRepositoryFiles] = useState<RepositoryFile[]>([])

    useEffect(() => {
        if (userId === undefined || repositoryId === undefined || path === undefined) {
            return
        }
        const fetchFiles = async () => {
            return await appGraph.repositoriesGraph.repositoriesRepository.getRepositoryFiles(
                userId, repositoryId, path
            )
        }
        fetchFiles().then(files => setRepositoryFiles(files))

    }, [userId, repositoryId, path])

    return (
        <>
            <RepositoryContent repositoryFiles={repositoryFiles}/>
        </>
    )
}
