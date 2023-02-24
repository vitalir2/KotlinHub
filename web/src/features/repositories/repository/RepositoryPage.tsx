import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box, CircularProgress} from "@mui/material";
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
        return LoadingPlaceholder()
    }

    return <RepositoryContainer repository={repositoryState.repository}/>
}

function LoadingPlaceholder() {
    return (
        <Box sx={{
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <CircularProgress/>
        </Box>
    )
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
