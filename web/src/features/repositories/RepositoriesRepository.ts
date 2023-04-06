import {Repository} from "./Repository";
import {RepositoryFile} from "./RepositoryFile";

export interface RepositoriesRepository {
    getRepositories(userId: string): Promise<Repository[]>

    getRepository(userId: string, repositoryId: string): Promise<Repository>

    getRepositoryFiles(userId: string, repositoryId: string, path: string): Promise<RepositoryFile[]>
}
