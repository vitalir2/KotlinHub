import {User} from "../user/User";

export interface LoginUserParams {
    username: string,
    password: string,
}

export interface LoginUserResult {
    token: string,
}

export interface AuthRepository {
    loginUser(request: LoginUserParams): Promise<LoginUserResult>
    getUser(): Promise<User>
}
