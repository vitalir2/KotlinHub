import {RepositoriesRepository} from "./RepositoriesRepository";

export class RepositoriesGraph {
    readonly repositoriesRepository: RepositoriesRepository;

    constructor(repositoriesRepository: RepositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository
    }
}
