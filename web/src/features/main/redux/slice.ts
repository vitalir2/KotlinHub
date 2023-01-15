import {createSlice} from "@reduxjs/toolkit";
import {initialState} from "./state";
import {Repository} from "../../repositories/Repository";
import {createAppAsyncThunk} from "../../../app/hooks";

export const fetchRepositories = createAppAsyncThunk<
    Repository[],
    number
    >("repositories/fetch", async (userId: number, thunkAPI) => {
    const repositoriesRepository = thunkAPI.extra.appGraph.repositoriesGraph.repositoriesRepository
    return await repositoriesRepository.getRepositories(userId)
})

export const repositoriesSlice = createSlice({
    name: "repositories",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchRepositories.fulfilled, (state, action) => {
            state.isLoading = false
            state.repositories = action.payload
        })
        builder.addCase(fetchRepositories.rejected, (state, action) => {
            state.isLoading = false
            state.error = action.error.message
        })
        builder.addCase(fetchRepositories.pending, (state) => {
            state.isLoading = true
            state.error = undefined
        })
    }
})
