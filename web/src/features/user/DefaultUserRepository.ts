import * as platformShared from "platform-shared"
import {baseApi, getDefaultHeaders} from "../../app/fetch";
import {User} from "./User";
import {LoginParams, LoginResult, LoginResultError, SuccessfulLoginResult, UserRepository} from "./UserRepository";
import axios from "axios";

type LoginRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse
type GetUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.GetUserResponse
type ErrorResponse = platformShared.io.vitalir.kotlinhub.shared.common.ErrorResponse

export class DefaultUserRepository implements UserRepository {
    loginUser(request: LoginParams): Promise<LoginResult> {
        return baseApi.post<LoginResponse>(
            "/users/auth", this.mapParamsToRequest(request),
            {
                headers: getDefaultHeaders(),
            }
        )
            .then(
                response => this.mapResponseToResult(response.data),
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
        return {
            username: response.user.username,
        }
    }
}
