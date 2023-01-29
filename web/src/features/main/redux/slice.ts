import {createSlice} from "@reduxjs/toolkit";
import {initialState} from "./state";
import {Repository} from "../../repositories/Repository";
import {createAppAsyncThunk} from "../../../app/hooks";
import {User} from "../../login/AuthRepository";

export const fetchCurrentUser = createAppAsyncThunk<
    User,
    void
>("users/fetch", async (arg, thunkApi) => {
    const authRepository = thunkApi.extra.appGraph.authGraph.authRepository
    return await authRepository.getUser()
})

export const fetchCurrentRepositories = createAppAsyncThunk<
    Repository[],
    void
    >("repositories/fetch", async (arg, thunkAPI) => {
    const repositoriesRepository = thunkAPI.extra.appGraph.repositoriesGraph.repositoriesRepository
    return await repositoriesRepository.getRepositories("current")
})

export const repositoriesSlice = createSlice({
    name: "repositories",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchCurrentRepositories.fulfilled, (state, action) => {
            state.isLoading = false
            state.repositories = action.payload
        })
        builder.addCase(fetchCurrentRepositories.rejected, (state, action) => {
            state.isLoading = false
            state.error = action.error.message
        })
        builder.addCase(fetchCurrentRepositories.pending, (state) => {
            state.isLoading = true
            state.error = undefined
        })
        builder.addCase(fetchCurrentUser.fulfilled, (state, action) => {
            state.user = action.payload
        })
        // TODO add error handling / loading
    }
})
