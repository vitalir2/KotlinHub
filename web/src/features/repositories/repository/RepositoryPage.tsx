import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box} from "@mui/material";

export function RepositoryPage() {
    const { repositoryId } = useParams()
    const repository: Repository | undefined = undefined

    if (repositoryId === undefined) {
        return <Box>Error Placeholder</Box> // TODO return error placeholder
    }

    // noinspection PointlessBooleanExpressionJS
    if (repository === undefined) {
        return <Box>Loading Placeholder</Box> // TODO return loading placeholder
    }

    return <RepositoryContainer repository={repository}/>
}

interface RepositoryContainerProps {
    repository: Repository,
}

function RepositoryContainer(props: RepositoryContainerProps) {
    const {repository} = props

    return (
        <Box>
            Repository {repository.name}
        </Box>
    )
}
