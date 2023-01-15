import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchRepositories} from "./redux/slice";

export function Main() {
    const state = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchRepositories(state.userId))
    }, [dispatch, state.userId])

    return (
        <Stack direction={"row"} spacing={2} sx={{
            padding: 2,
        }}>
            <Profile/>
            <Repositories
                repositories={state.repositories}
                isLoading={state.isLoading}
                errorText={state.error}
            />
        </Stack>
    )
}
