import {AuthRepository} from "./AuthRepository";

export class AuthGraph {
    readonly authRepository: AuthRepository

    constructor(authRepository: AuthRepository) {
        this.authRepository = authRepository;
    }
}
