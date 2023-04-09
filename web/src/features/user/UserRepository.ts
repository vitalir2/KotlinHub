import {User} from "./User";

export interface LoginParams {
    username: string,
    password: string,
}

export interface SuccessfulLoginResult {
    type: "success",
    token: string,
}

export interface ErrorLoginResult {
    type: "error",
    error: LoginResultError,
}

export enum LoginResultError {
    InvalidCredentials,
    Unknown,
}

export type LoginResult = SuccessfulLoginResult | ErrorLoginResult

export interface UserRepository {
    loginUser(request: LoginParams): Promise<LoginResult>
    logout(): Promise<Boolean>
    getUser(): Promise<User>
}
