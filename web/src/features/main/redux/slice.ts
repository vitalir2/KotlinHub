import {createSlice} from "@reduxjs/toolkit";
import {initialState} from "./state";
import {Repository, RepositoryAccessMode} from "../../repositories/Repository";
import {createAppAsyncThunk} from "../../../app/hooks";
import {ApiRepository, ApiRepositoryAccessMode} from "../../repositories/RepositoriesRepository";
import {convertNullableToTypescriptModel} from "../../../app/fetch";

export const fetchRepositories = createAppAsyncThunk<
    Repository[],
    number
    >("repositories/fetch", async (userId: number, thunkAPI) => {
    const repositoriesRepository = thunkAPI.extra.appGraph.repositoriesGraph.repositoriesRepository
    const repositories = await repositoriesRepository.getRepositories(userId)
    return repositories.repositories.map(apiRepository => convertToLocalModel(apiRepository))
})

function convertToLocalModel(apiRepository: ApiRepository): Repository {
    const convertAccessMode = (apiAccessMode: ApiRepositoryAccessMode) => {
        switch (apiAccessMode.name) {
            case "PUBLIC":
                return RepositoryAccessMode.PUBLIC
            case "PRIVATE":
                return RepositoryAccessMode.PRIVATE
        }
    }
    return {
        name: apiRepository.name,
        accessMode: convertAccessMode(apiRepository.accessMode),
        description: convertNullableToTypescriptModel(apiRepository.description),
    }
}

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
