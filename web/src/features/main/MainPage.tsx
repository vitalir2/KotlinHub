import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchCurrentRepositories} from "./redux/slice";

export function MainPage() {
    const state = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchCurrentRepositories())
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
