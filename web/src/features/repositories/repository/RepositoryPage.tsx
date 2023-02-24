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
        // TODO place error text to the reducer
        return <ErrorPlaceholder error={"Something went wrong"}/>
    }

    const repository = repositoryState.repository
    switch (repository.kind) {
        case "loading":
            return LoadingPlaceholder()
        case "loaded":
            return <RepositoryContainer repository={repository.data}/>
        case "error":
            return <ErrorPlaceholder error={repository.error}/>
    }
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
