import {User} from "./User";

export interface LoginParams {
    username: string,
    password: string,
}

export interface LoginResult {
    token: string,
}

export interface UserRepository {
    loginUser(request: LoginParams): Promise<LoginResult>
    getUser(): Promise<User>
}
