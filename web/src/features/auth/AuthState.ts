import {User} from "../user/User";

export interface LoggedIn {
    kind: "logged_in",
    error?: string,
    token: string,
    user?: User,
}

export interface LoggedOut {
    kind: "logged_out",
    error?: string,
    user?: undefined,
}

export type AuthState = LoggedIn | LoggedOut;
