import {UserRepository} from "./UserRepository";

export class UserGraph {
    readonly userRepository: UserRepository

    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository;
    }
}
