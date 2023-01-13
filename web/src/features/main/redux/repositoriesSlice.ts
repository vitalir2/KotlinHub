import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import {AppGraph} from "../../../app/dependency_injection";

export interface RepositoriesState {
    repositories: Repository[],
}

export type Repository = {
    name: string,
}

const initialState: RepositoriesState = {
    repositories: [],
}

export const fetchRepositories = createAsyncThunk<
    Repository[],
    number,
    {
        extra: {
            appGraph: AppGraph
        }
    }>("repositories/fetch", async (userId: number, thunkAPI) => {
    const repositoriesRepository = thunkAPI.extra.appGraph.repositoriesGraph.repositoriesRepository
    const repositories = await repositoriesRepository.getRepositories(userId)
    return repositories.repositories.map(apiRepository => {
        return {
            name: apiRepository.name,
        }
    })
})

export const repositoriesSlice = createSlice({
    name: "repositories",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchRepositories.fulfilled, (state, action) => {
            state.repositories = action.payload
        })
        builder.addCase(fetchRepositories.rejected, () => {
            console.log("Error during the search")
        })
    }
})
