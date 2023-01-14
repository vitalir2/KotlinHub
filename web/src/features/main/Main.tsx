import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchRepositories} from "./redux/slice";

export function Main() {
    const state = useAppSelector((state) => state.repositories)
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchRepositories(state.userId))
    }, [])

    return (
        <Stack direction={"row"} spacing={2}>
            <Profile/>
            <Repositories repositories={state.repositories}/>
        </Stack>
    )
}
