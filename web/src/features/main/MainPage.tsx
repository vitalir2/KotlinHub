import {Stack} from "@mui/material";
import {LoadableProfileSidebar} from "./LoadableProfileSidebar";
import {LoadableRepositories} from "./LoadableRepositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchCurrentRepositories, fetchCurrentUser} from "./MainSlice";

export function MainPage() {
    const state = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchCurrentUser())
        dispatch(fetchCurrentRepositories())
    }, [dispatch])

    return (
        <Stack direction={"row"} spacing={2} sx={{
            padding: 2,
        }}>
            <LoadableProfileSidebar loadableUser={state.user}/>
            <LoadableRepositories loadableRepositories={state.repositories}/>
        </Stack>
    )
}
