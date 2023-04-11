import {Repository, RepositoryAccessMode} from "./Repository";
import {RepositoryFile} from "./RepositoryFile";

export interface CreateRepositoryParams {
    name: string,
    accessMode: RepositoryAccessMode,
    description: string,
}

export interface CreateRepositorySuccess {
    kind: "success",
    repositoryId: string,
}

export interface CreateRepositoryError {
    kind: "error",
    error: string,
}

export type CreateRepositoryResult = CreateRepositorySuccess | CreateRepositoryError;

export interface RepositoriesRepository {
    getRepositories(userId: string): Promise<Repository[]>

    getRepository(userId: string, repositoryId: string): Promise<Repository>

    getRepositoryFiles(userId: string, repositoryId: string, path: string): Promise<RepositoryFile[]>

    getRepositoryFileContent(
        userId: string,
        repositoryId: string,
        path: string,
    ): Promise<string>

    createRepository(params: CreateRepositoryParams): Promise<CreateRepositoryResult>
}
