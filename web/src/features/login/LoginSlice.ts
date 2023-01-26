import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {createAppAsyncThunk} from "../../app/hooks";
import {LoginUserRequest, LoginUserResult} from "./AuthRepository";

export interface LoginState {
    login: TextInputData,
    password: TextInputData,
    rememberUser: boolean,
    isValidating: boolean,
    isLoggedInSuccessfully: boolean,
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
    isLoggedInSuccessfully: false,
}

export const loginUser = createAppAsyncThunk<
    LoginUserResult,
    LoginUserRequest
>("login/loginUser", async (request: LoginUserRequest, thunkAPI) => {
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
        builder.addCase(loginUser.fulfilled, (state) => {
            state.isValidating = false
            state.isLoggedInSuccessfully = true
        })
    }
})

export const { setLogin, setPassword, setRememberMe } = loginSlice.actions
