import {createSlice} from "@reduxjs/toolkit";
import {AuthState} from "./AuthState";
import {readSetting} from "../../core/settings/Settings";
import {SETTING_AUTH_TOKEN} from "../../core/settings/SettingsNames";
import {createAppAsyncThunk} from "../../app/hooks";
import {User} from "./User";
import {AppDispatch} from "../../app/store";
import {appGraph} from "../../app/dependency_injection";
import {LoginParams, RegistrationParams, SuccessfulLoginResult} from "./AuthRepository";

const getInitialState: () => AuthState = () => {
    const authToken = readSetting(SETTING_AUTH_TOKEN);
    if (authToken === undefined) {
        return {
            kind: "logged_out",
        };
    } else {
        return {
            kind: "logged_in",
            token: authToken,
            user: undefined,
        }
    }
};

export const fetchCurrentUser = createAppAsyncThunk<
    User,
    void
>("users/fetch", async (arg, thunkApi) => {
    const authRepository = thunkApi.extra.appGraph.authGraph.authRepository
    return await authRepository.getUser();
})

export const loginThunk = (params: LoginParams) => (dispatch: AppDispatch) => {
    const result = dispatch(loginUser(params)).unwrap();
    result.then(() => dispatch(fetchCurrentUser()))
};

export const registerThunk = (params: RegistrationParams) => (dispatch: AppDispatch) => {
    const result = appGraph.authGraph.authRepository.registerUser(params);
    result.then(() => dispatch(fetchCurrentUser()));
}

export const loginUser = createAppAsyncThunk<
    SuccessfulLoginResult,
    LoginParams
>("login/loginUser", async (request: LoginParams, { extra, rejectWithValue}) => {
    const authRepository = extra.appGraph.authGraph.authRepository
    const result = await authRepository.loginUser(request)
    switch (result.type) {
        case "success":
            return result
        case "error":
            return rejectWithValue(result.error)
    }
})

export const logoutThunk = () => async (dispatch: AppDispatch) => {
    const isLogoutSuccessful = await appGraph.authGraph.authRepository.logout();
    if (isLogoutSuccessful) {
        dispatch(authSlice.actions.logout())
    }
}

const authSlice = createSlice({
    name: "auth",
    initialState: getInitialState(),
    reducers: {
        logout: state => {
            if (state.kind === "logged_in") {
                return {
                    kind: "logged_out",
                };
            }

            return state;
        },
    },
    extraReducers: builder => {
        builder.addCase(fetchCurrentUser.fulfilled, (state, action) => {
            if (state.kind === "logged_in") {
                state.user = action.payload;
            }
        })
        builder.addCase(fetchCurrentUser.rejected, (state, action) => {
            state.error = action.error.message!;
        })
        builder.addCase(loginUser.fulfilled, (state, action) => {
            if (state.kind === "logged_out") {
                return {
                    kind: "logged_in",
                    token: action.payload.token,
                };
            }

            return state;
        })
    }
})

export const authReducer = authSlice.reducer;
