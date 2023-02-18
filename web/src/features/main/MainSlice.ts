import {createSlice} from "@reduxjs/toolkit";
import {Repository} from "../repositories/Repository";
import {createAppAsyncThunk} from "../../app/hooks";
import {Loadable} from "../../core/models/Loadable";

export interface MainState {
    repositories: Loadable<Repository[]>,

}

const initialState: MainState = {
    repositories: {
        kind: "loading",
    },
}

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
        // TODO show errors from backend or at least log them
        builder.addCase(fetchCurrentRepositories.fulfilled, (state, action) => {
            state.repositories = {
                kind: "loaded",
                data: action.payload,
            }
        })
        builder.addCase(fetchCurrentRepositories.rejected, (state, action) => {
            state.repositories = {
                kind: "error",
                error: action.error.message!,
            }
        })
        builder.addCase(fetchCurrentRepositories.pending, (state) => {
            state.repositories = {
                kind: "loading",
            }
        })
    }
})
