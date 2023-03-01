import {createSlice} from "@reduxjs/toolkit";
import {Repository} from "../Repository";
import {createAppAsyncThunk} from "../../../app/hooks";
import {Loadable} from "../../../core/models/Loadable";

export interface RepositoryState {
    repository: Loadable<Repository>,
}

const initState: RepositoryState = {
    repository: {
        kind: "loading",
    }
}

export const fetchRepository = createAppAsyncThunk<
    Repository,
    string
>(
    "fetchRepository", async (repositoryId, thunkApi) => {
        const repository = thunkApi.extra.appGraph.repositoriesGraph.repositoriesRepository
        return repository.getRepository("current", repositoryId)
    }
)

export const repositorySlice = createSlice({
    name: "repository",
    initialState: initState,
    reducers: {},
    extraReducers: builder => {
        builder.addCase(fetchRepository.pending, (state) => {
            state.repository = {
                kind: "loading",
            }
        })
        builder.addCase(fetchRepository.fulfilled, (state, action) => {
            state.repository = {
                kind: "loaded",
                data: action.payload,
            }
        })
        builder.addCase(fetchRepository.rejected, (state) => {
            state.repository = {
                kind: "error",
                error: "Something went wrong"
            }
        })
    },
})
