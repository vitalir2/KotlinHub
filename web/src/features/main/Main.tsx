import {Stack} from "@mui/material";
import {Profile} from "./Profile";
import {Repositories} from "./Repositories";
import React, {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../app/hooks";
import {fetchRepositories, RepositoriesState} from "./redux/repositoriesSlice";
import {Action, ThunkAction} from "@reduxjs/toolkit";

export function Main() {
    const [userId] = useState(1) // TODO move to redux state
    const repositories = useAppSelector((state) =>
        state.repositories.repositories.map(repository => repository.name)
    )
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(fetchRepositories(userId) as ThunkAction<unknown, unknown, unknown, Action>) // TODO wtf TS?
    }, [userId])

    return (
        <Stack direction={"row"} spacing={2}>
            <Profile/>
            <Repositories repositories={repositories}/>
        </Stack>
    )
}
