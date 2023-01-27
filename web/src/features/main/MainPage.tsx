import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchRepositories} from "./redux/slice";
import {readCookie} from "../../core/settings/Cookies";
import {SETTING_AUTH_TOKEN} from "../../core/settings/SettingsNames";

export function MainPage() {
    const state = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        const userToken = readCookie(SETTING_AUTH_TOKEN)
        if (userToken != null) {
            dispatch(fetchRepositories())
        }
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
