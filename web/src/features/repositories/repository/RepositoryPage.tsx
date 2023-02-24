import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box, CircularProgress, Typography} from "@mui/material";
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
        return <ErrorPlaceholder error={"Unexpected error"}/>
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

interface ErrorPlaceholderProps {
    error: string,
}

function ErrorPlaceholder(props: ErrorPlaceholderProps) {
    return (
        <Box sx={{
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <Typography variant={"h6"}>
                {props.error}
            </Typography>
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
