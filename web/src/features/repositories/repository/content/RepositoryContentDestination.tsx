import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RepositoryFile, RepositoryFileType} from "../../RepositoryFile";
import {appGraph} from "../../../../app/dependency_injection";
import {RepositoryContent} from "./RepositoryContent";

function compareRepositoryFiles(lhs: RepositoryFile, rhs: RepositoryFile): number {
    if (lhs.type === rhs.type) {
        return lhs.name.localeCompare(rhs.name)
    } else if (lhs.type === RepositoryFileType.FOLDER) {
        return -1
    } else {
        return 1
    }
}

export function RepositoryContentDestination() {
    const params = useParams();
    const repositoryId = params['repositoryId'];
    const path = params['*'];
    const [repositoryFiles, setRepositoryFiles] = useState<RepositoryFile[]>([])
    const [isLoading, setLoading] = useState(true);

    const currentPath = path ?? ""
    useEffect(() => {
        if (repositoryId === undefined) {
            return
        }
        const fetchFiles = async () => {
            setLoading(true);
            const result = await appGraph.repositoriesGraph.repositoriesRepository.getRepositoryFiles(
                "current", repositoryId, currentPath
            )
            setLoading(false);
            return result;
        }
        fetchFiles()
            .then(files => setRepositoryFiles(
                files.sort((lhs, rhs) => compareRepositoryFiles(lhs, rhs)))
            )

    }, [repositoryId, currentPath])

    return (
        <>
            <RepositoryContent
                repositoryFiles={repositoryFiles}
                path={currentPath}
                isLoading={isLoading}
            />
        </>
    )
}
