import {useParams} from "react-router-dom";
import {Repository} from "../Repository";
import {Box, CircularProgress, Stack, Typography} from "@mui/material";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../../app/hooks";
import {fetchRepository} from "./RepositorySlice";
import {User} from "../../user/User";
import {fetchCurrentUser} from "../../user/UserSlice";

export function RepositoryPage() {
    const dispatch = useAppDispatch()

    const { repositoryId } = useParams()
    const user = useAppSelector(state => state.user.user)
    const repository = useAppSelector(state => state.repository.repository)

    useEffect(() => {
        if (repositoryId === undefined) return
        dispatch(fetchRepository(repositoryId))
        dispatch(fetchCurrentUser())
    }, [repositoryId, dispatch])

    if (repository.kind === "loading" || user.kind === "loading") {
        return <LoadingPlaceholder/>
    }

    if (repositoryId === undefined || repository.kind === "error" || user.kind === "error") {
        return <ErrorPlaceholder error={"Something went wrong"} />
    }

    return <RepositoryContainer user={user.data} repository={repository.data}/>
}

function LoadingPlaceholder() {
    return (
        <Box sx={{
            width: "100%",
            height: "100vh", // TODO fix to be 100% of free space
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
            height: "100vh",
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
    user: User,
    repository: Repository,
}

function RepositoryContainer(props: RepositoryContainerProps) {
    const { user, repository } = props

    return (
        <Stack spacing={2} sx={{
            padding: "1rem",
        }}>
            {`${user.username} / ${repository.name}`}
        </Stack>
    )
}
