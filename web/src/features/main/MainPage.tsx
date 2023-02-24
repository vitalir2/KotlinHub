import {Stack} from "@mui/material";
import {LoadableProfileSidebar} from "./LoadableProfileSidebar";
import {LoadableRepositories} from "./LoadableRepositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchCurrentRepositories} from "./MainSlice";
import {fetchCurrentUser} from "../user/UserSlice";

export function MainPage() {
    const userState = useAppSelector(state => state.user)
    const repositoriesState = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchCurrentUser())
        dispatch(fetchCurrentRepositories())
    }, [dispatch])

    return (
        <Stack direction={"row"} spacing={2} sx={{
            padding: 2,
        }}>
            <LoadableProfileSidebar loadableUser={userState.user}/>
            <LoadableRepositories loadableRepositories={repositoriesState.repositories}/>
        </Stack>
    )
}
