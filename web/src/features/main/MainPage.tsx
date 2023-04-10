import {Stack} from "@mui/material";
import {LoadableProfileSidebar} from "./LoadableProfileSidebar";
import {LoadableRepositories} from "./LoadableRepositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchCurrentRepositories} from "./MainSlice";
import {useAuthState} from "../auth/AuthHooks";
import {ErrorPlaceholder} from "../../core/view/placeholder/ErrorPlaceholder";

export function MainPage() {
    const authState = useAuthState();
    const repositoriesState = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchCurrentRepositories())
    }, [dispatch])

    if (authState.error !== undefined) {
        return <ErrorPlaceholder error={authState.error}/>
    }

    return (
        <Stack direction={"row"} spacing={2} sx={{p: 2}}>
            <LoadableProfileSidebar user={authState.user}/>
            <LoadableRepositories loadableRepositories={repositoriesState.repositories}/>
        </Stack>
    )
}
