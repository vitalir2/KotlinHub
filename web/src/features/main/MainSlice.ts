import {createSlice} from "@reduxjs/toolkit";
import {Repository} from "../repositories/Repository";
import {createAppAsyncThunk} from "../../app/hooks";
import {User} from "../user/User";
import {Loadable} from "../../core/models/Loadable";

export interface MainState {
    user: Loadable<User>,
    repositories: Loadable<Repository[]>,

}

const initialState: MainState = {
    user: {
        kind: "loading",
    },
    repositories: {
        kind: "loading",
    },
}

export const fetchCurrentUser = createAppAsyncThunk<
    User,
    void
>("users/fetch", async (arg, thunkApi) => {
    const authRepository = thunkApi.extra.appGraph.userGraph.userRepository
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
        builder.addCase(fetchCurrentUser.pending, (state) => {
            state.user = {
                kind: "loading",
            }
        })
        builder.addCase(fetchCurrentUser.fulfilled, (state, action) => {
            state.user = {
                kind: "loaded",
                data: action.payload,
            }
        })
        builder.addCase(fetchCurrentUser.rejected, (state, action) => {
            state.user = {
                kind: "error",
                error: action.error.message!,
            }
        })
    }
})
