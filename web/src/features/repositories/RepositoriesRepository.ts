import {Repository} from "./Repository";

export interface RepositoriesRepository {
    getRepositories(userId: string): Promise<Repository[]>
}
