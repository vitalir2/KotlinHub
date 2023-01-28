import * as platformShared from "platform-shared/platform-shared"

import {baseApi, getDefaultHeaders} from "../../app/fetch";

export interface LoginUserParams {
    username: string,
    password: string,
}

export interface LoginUserResult {
    token: string,
}

export interface AuthRepository {
    loginUser(request: LoginUserParams): Promise<LoginUserResult>
}

type LoginUserRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse

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
}
