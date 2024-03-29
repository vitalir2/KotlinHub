import * as platformShared from "platform-shared"
import {baseApi, convertNullableToTypescriptModel, getDefaultHeaders} from "../../app/fetch";
import {User} from "./User";
import {
    LoginParams,
    LoginResult,
    LoginResultError,
    SuccessfulLoginResult,
    AuthRepository,
    RegistrationParams,
    RegistrationResult
} from "./AuthRepository";
import axios from "axios";
import {clearSetting, setSetting} from "../../core/settings/Settings";
import {SETTING_AUTH_TOKEN} from "../../core/settings/SettingsNames";

type LoginRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse
type RegistrationRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.RegisterUserRequest
type RegistrationResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.RegisterUserResponse
type GetUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.GetUserResponse
type ErrorResponse = platformShared.io.vitalir.kotlinhub.shared.common.ErrorResponse

export class DefaultAuthRepository implements AuthRepository {
    async registerUser(params: RegistrationParams): Promise<RegistrationResult> {
        const request = this.createRegistrationRequest(params);
        try {
            const response = await baseApi.post<RegistrationResponse>("/users", request, {
                headers: getDefaultHeaders(),
            });
            setSetting(SETTING_AUTH_TOKEN, response.data.token)
            return {
                kind: "success",
            };
        } catch (e) {
            if (axios.isAxiosError(e)) {
                // TODO handle errors from backend
                return {
                    kind: "error",
                    error: "User already exists",
                };
            } else {
                return {
                    kind: "error",
                    error: "Unknown error",
                };
            }
        }
    }

    private createRegistrationRequest(params: RegistrationParams): RegistrationRequest {
        return {
            login: params.username,
            password: params.password,
        };
    }

    logout(): Promise<Boolean> {
        clearSetting(SETTING_AUTH_TOKEN)
        return Promise.resolve(true);
    }

    loginUser(request: LoginParams): Promise<LoginResult> {
        return baseApi.post<LoginResponse>(
            "/users/auth", this.mapParamsToRequest(request),
            {
                headers: getDefaultHeaders(),
            }
        )
            .then(
                response => {
                    setSetting(SETTING_AUTH_TOKEN, response.data.token);
                    return this.mapResponseToResult(response.data);
                },
                error => {
                    let loginError: LoginResultError
                    if (axios.isAxiosError(error) && error.response !== undefined) {
                        const errorResponse: ErrorResponse = error.response.data
                        console.warn(errorResponse.message) // TODO is this safe?
                        switch (errorResponse.code) {
                            case 400:
                                loginError = LoginResultError.InvalidCredentials
                                break;
                            default:
                                loginError = LoginResultError.Unknown
                        }
                    } else {
                        loginError = LoginResultError.Unknown
                    }
                    return {
                        type: "error",
                        error: loginError,
                    }
                })
    }

    getUser(): Promise<User> {
        return baseApi.get<GetUserResponse>(
            "/users/current",
            {
                headers: getDefaultHeaders(),
            },
        )
            .then(response => response.data)
            .then(body => this.mapResponseToUser(body))
    }

    private mapParamsToRequest(params: LoginParams): LoginRequest {
        return {
            username: params.username,
            password: params.password,
        }
    }

    private mapResponseToResult(response: LoginResponse): SuccessfulLoginResult {
        return {
            type: "success",
            token: response.token
        }
    }

    private mapResponseToUser(response: GetUserResponse): User {
        const user = response.user
        return {
            username: user.username,
            firstName: convertNullableToTypescriptModel(user.firstName),
            lastName: convertNullableToTypescriptModel(user.lastName),
            email: convertNullableToTypescriptModel(user.email),
        }
    }
}
