import {User} from "./User";
import {createSlice} from "@reduxjs/toolkit";
import {createAppAsyncThunk} from "../../app/hooks";
import {Loadable} from "../../core/models/Loadable";

interface UserState {
    user: Loadable<User>,
}

const initState: UserState = {
    user: {
        kind: "loading",
    }
}

export const fetchCurrentUser = createAppAsyncThunk<
    User,
    void
>("users/fetch", async (arg, thunkApi) => {
    const authRepository = thunkApi.extra.appGraph.userGraph.userRepository
    return await authRepository.getUser()
})

export const userSlice = createSlice({
    initialState: initState,
    name: "user",
    reducers: {
    },
    extraReducers: builder => {
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
