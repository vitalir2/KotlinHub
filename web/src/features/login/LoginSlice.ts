import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {createAppAsyncThunk} from "../../app/hooks";
import {LoginParams, LoginResult, LoginResultError, SuccessfulLoginResult} from "../user/UserRepository";

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
    SuccessfulLoginResult,
    LoginParams
>("login/loginUser", async (request: LoginParams, { extra, rejectWithValue }) => {
    const authRepository = extra.appGraph.userGraph.userRepository
    const result = await authRepository.loginUser(request)
    switch (result.type) {
        case "success":
            return result
        case "error":
            return rejectWithValue(result.error)
    }
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
        builder.addCase(loginUser.rejected, (state, action: PayloadAction<LoginResultError>) => {
            state.isValidating = false

            let errorMessage: string
            switch (action.payload) {
                case LoginResultError.InvalidCredentials:
                    errorMessage = "Invalid credentials"
                    break
                case LoginResultError.Unknown:
                    errorMessage = "Unknown error"
                    break
            }
            state.password.errorMessage = errorMessage
        })
        builder.addCase(loginUser.fulfilled, (state, action) => {
            state.isValidating = false
            state.userToken = action.payload.token
        })
    }
})

export const { setLogin, setPassword, setRememberMe } = loginSlice.actions
