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

export interface RegistrationParams {
    username: string,
    password: string,
}

export interface SuccessfulRegistration {
    kind: "success",
}

export interface RegistrationError {
    kind: "error",
    error: string,
}

export type RegistrationResult = SuccessfulRegistration | RegistrationError;

export interface AuthRepository {
    loginUser(request: LoginParams): Promise<LoginResult>
    logout(): Promise<Boolean>
    getUser(): Promise<User>

    registerUser(params: RegistrationParams): Promise<RegistrationResult>
}
