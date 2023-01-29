import * as platformShared from "platform-shared/platform-shared"

import {baseApi, getDefaultHeaders} from "../../app/fetch";

export interface LoginUserParams {
    username: string,
    password: string,
}

export interface LoginUserResult {
    token: string,
}

// TODO add more data mapping from backend and on backend too
export interface User {
    username: string
}

export interface AuthRepository {
    loginUser(request: LoginUserParams): Promise<LoginUserResult>
    getUser(): Promise<User>
}

type LoginUserRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse
type GetUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.GetUserResponse

export class DefaultAuthRepository implements AuthRepository {
    loginUser(request: LoginUserParams): Promise<LoginUserResult> {
        return baseApi.post<LoginUserResponse>(
            "/users/auth", this.mapParamsToRequest(request),
            {
                headers: getDefaultHeaders(),
            }
        )
            .then(response => response.data)
            .then(body => this.mapResponseToResult(body))
    }

    getUser(): Promise<User> {
        return baseApi.get<GetUserResponse>(
            // TODO fix on backend the default type
            "/users/current?type=id",
            {
                headers: getDefaultHeaders(),
            },
        )
            .then(response => response.data)
            .then(body => this.mapResponseToUser(body))
    }

    private mapParamsToRequest(params: LoginUserParams): LoginUserRequest {
        return {
            username: params.username,
            password: params.password,
        }
    }

    private mapResponseToResult(response: LoginUserResponse): LoginUserResult {
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
