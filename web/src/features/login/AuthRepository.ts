import {baseApi} from "../../app/fetch";

export interface LoginUserRequest {
    username: string,
    password: string,
}
 interface LoginUserResponse {
    token: string,
}

export interface LoginUserResult {
    token: string,
}

export interface AuthRepository {
    loginUser(request: LoginUserRequest): Promise<LoginUserResult>
}

export class DefaultAuthRepository implements AuthRepository {
    loginUser(request: LoginUserRequest): Promise<LoginUserResult> {
        return baseApi.post<LoginUserResponse>("/users/auth", request)
            .then(response => response.data)
            .then(body => this.mapResponseToResult(body))
    }

    private mapResponseToResult(response: LoginUserResponse): LoginUserResult {
        return {
            token: response.token,
        }
    }
}
