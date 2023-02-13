import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box} from "@mui/material";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {fetchRepository} from "./RepositorySlice";

export function RepositoryPage() {
    const dispatch = useAppDispatch()

    const { repositoryId } = useParams()
    const repositoryState = useAppSelector(state => state.repository)

    useEffect(() => {
        if (repositoryId === undefined) return
        dispatch(fetchRepository(repositoryId))
    }, [repositoryId, dispatch])

    if (repositoryId === undefined) {
        return <Box>Error Placeholder</Box> // TODO return error placeholder
    }

    if (repositoryState.repository === undefined) {
        return <Box>Loading Placeholder</Box> // TODO return loading placeholder
    }

    return <RepositoryContainer repository={repositoryState.repository}/>
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
