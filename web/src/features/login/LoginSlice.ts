import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {createAppAsyncThunk} from "../../app/hooks";
import {LoginUserParams, LoginUserResult} from "./AuthRepository";

export interface LoginState {
    login: TextInputData,
    password: TextInputData,
    rememberUser: boolean,
    isValidating: boolean,
    userToken?: string,
}

export interface TextInputData {
    value: string,
    errorMessage?: string,
}

const initialState: LoginState = {
    login: {
        value: "",
    },
    password: {
        value: "",
    },
    rememberUser: false,
    isValidating: false,
}

export const loginUser = createAppAsyncThunk<
    LoginUserResult,
    LoginUserParams
>("login/loginUser", async (request: LoginUserParams, thunkAPI) => {
    const authRepository = thunkAPI.extra.appGraph.authGraph.authRepository
    return await authRepository.loginUser(request)
})

export const loginSlice = createSlice({
    name: "login",
    initialState,
    reducers: {
        setLogin(state: LoginState, action: PayloadAction<string>) {
            state.login.value = action.payload
            state.login.errorMessage = undefined
        },
        setPassword(state: LoginState, action: PayloadAction<string>) {
            state.password.value = action.payload
            state.password.errorMessage = undefined
        },
        setRememberMe(state: LoginState, action: PayloadAction<boolean>) {
            state.rememberUser = action.payload
        }
    },
    extraReducers: (builder) => {
        builder.addCase(loginUser.pending, (state) => {
            state.isValidating = true
        })
        builder.addCase(loginUser.rejected, (state, action) => {
            state.isValidating = false
            state.password.errorMessage = "Error " + action.error.message // TODO add error messages to backend
        })
        builder.addCase(loginUser.fulfilled, (state, action) => {
            state.isValidating = false
            state.userToken = action.payload.token
        })
    }
})

export const { setLogin, setPassword, setRememberMe } = loginSlice.actions
