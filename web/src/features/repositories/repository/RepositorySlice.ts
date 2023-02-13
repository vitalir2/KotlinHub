import {createSlice} from "@reduxjs/toolkit";
import {Repository} from "../Repository";
import {createAppAsyncThunk} from "../../../app/hooks";

export interface RepositoryState {
    repository?: Repository,
}

const initState: RepositoryState = {

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
        builder.addCase(fetchRepository.fulfilled, (state, action) => {
            state.repository = action.payload
        })
    },
})
