import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
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
            <Profile user={state.user}/>
            <Repositories
                repositories={state.repositories}
                isLoading={state.isLoading}
                errorText={state.error}
            />
        </Stack>
    )
}
