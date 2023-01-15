import {Repository} from "./Repository";

export interface RepositoriesRepository {
    getRepositories(userId: number): Promise<Repository[]>
}
