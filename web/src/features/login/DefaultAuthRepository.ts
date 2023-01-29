import * as platformShared from "platform-shared/platform-shared"
import {baseApi, getDefaultHeaders} from "../../app/fetch";
import {User} from "../user/User";
import {AuthRepository, LoginUserParams, LoginUserResult} from "./AuthRepository";

type LoginRequest = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginRequest
type LoginResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.LoginResponse
type GetUserResponse = platformShared.io.vitalir.kotlinhub.shared.feature.user.GetUserResponse

export class DefaultAuthRepository implements AuthRepository {
    loginUser(request: LoginUserParams): Promise<LoginUserResult> {
        return baseApi.post<LoginResponse>(
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

    private mapParamsToRequest(params: LoginUserParams): LoginRequest {
        return {
            username: params.username,
            password: params.password,
        }
    }

    private mapResponseToResult(response: LoginResponse): LoginUserResult {
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
