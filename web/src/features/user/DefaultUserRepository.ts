import * as platformShared from "platform-shared"
import {baseApi, getDefaultHeaders} from "../../app/fetch";
import {User} from "./User";
import {LoginParams, LoginResult, UserRepository} from "./UserRepository";
import axios from "axios";

type LoginRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse
type GetUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.GetUserResponse

export class DefaultUserRepository implements UserRepository {
    loginUser(request: LoginParams): Promise<LoginResult> {
        return baseApi.post<LoginResponse>(
            "/users/auth", this.mapParamsToRequest(request),
            {
                headers: getDefaultHeaders(),
            }
        )
            .then(
                response => response.data,
                error => {
                    let errorMessage: string
                    if (axios.isAxiosError(error) && error.response !== undefined) {
                        errorMessage = error.response.data()
                    } else {
                        errorMessage = "Undefined error"
                    }
                    throw Error(errorMessage)
                })
            .then(body => this.mapResponseToResult(body))
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

    private mapResponseToResult(response: LoginResponse): LoginResult {
        return {
            token: response.token,
        }
    }

    private mapResponseToUser(response: GetUserResponse): User {
        return {
            username: response.user.username,
        }
    }
}
