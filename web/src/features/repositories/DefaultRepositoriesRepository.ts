import * as platformShared from "platform-shared/platform-shared"
import {baseApi, convertNullableToTypescriptModel, getDefaultHeaders} from "../../app/fetch";
import {CreateRepositoryParams, CreateRepositoryResult, RepositoriesRepository} from "./RepositoriesRepository";
import {Repository, RepositoryAccessMode} from "./Repository";
import {RepositoryFile, RepositoryFileType} from "./RepositoryFile";
import axios from "axios";

type GetRepositoriesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
type GetRepositoryResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoryResponse
type GetRepositoryFilesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoryFilesResponse
type CreateRepositoryRequest = platformShared.io.vitalir.kotlinhub.shared.feature.repository.CreateRepositoryRequest
type CreateRepositoryResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.CreateRepositoryResponse
type ApiRepository = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
type ApiRepositoryFile = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepositoryFile

export class DefaultRepositoriesRepository implements RepositoriesRepository {
    async createRepository(params: CreateRepositoryParams): Promise<CreateRepositoryResult> {
        const request = this.getCreateRepositoryRequest(params);
        try {
            const result = await baseApi.post<CreateRepositoryResponse>("/repositories", request, {
                headers: getDefaultHeaders(),
            });
            const repositoryId = result.data.repositoryName;
            return {
                kind: "success",
                repositoryId: repositoryId,
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                return {
                    kind: "error",
                    error: error.message,
                }
            }
            return {
                kind: "error",
                error: "Unknown",
            };
        }
    }

    private getCreateRepositoryRequest(params: CreateRepositoryParams): CreateRepositoryRequest {
        let accessMode: string
        switch (params.accessMode) {
            case RepositoryAccessMode.PUBLIC:
                accessMode = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PUBLIC.name;
                break;
            case RepositoryAccessMode.PRIVATE:
                accessMode = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PRIVATE.name;
                break;
        }
        return {
            name: params.name,
            accessMode: accessMode as any as platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode,
            description: params.description,
        };
    }
    getRepositoryFileContent(userId: string, repositoryId: string, path: string): Promise<string> {
        return baseApi.get<string>(
            `/repositories/${userId}/${repositoryId}/content/${path}`,
            {
                headers: getDefaultHeaders(),
            }
        )
            .then(response => response.data)
    }
    getRepositoryFiles(userId: string, repositoryId: string, path: string): Promise<RepositoryFile[]> {
        return baseApi.get<GetRepositoryFilesResponse>(`/repositories/${userId}/${repositoryId}/tree/${path}`, {
            headers: getDefaultHeaders(),
        })
            .then(response => response.data.files)
            .then(files => files.map(file => this.convertRepositoryFileTypeToLocalModel(file)))
    }
    getRepositories(userId: string): Promise<Repository[]> {
        return baseApi.get<GetRepositoriesResponse>(`/repositories/${userId}`, {
            headers: getDefaultHeaders(),
        })
            .then(response => response.data.repositories)
            .then(repositories => repositories.map(repository => this.convertToLocalModel(repository)))
    }

    getRepository(userId: string, repositoryId: string): Promise<Repository> {
        return baseApi.get<GetRepositoryResponse>(`/repositories/${userId}/${repositoryId}`, {
            headers: getDefaultHeaders(),
        })
            .then(response => {
                const repository = response.data.repository
                return this.convertToLocalModel(repository)
            })
    }

    convertToLocalModel(apiRepository: ApiRepository): Repository {
        const convertAccessMode = (apiAccessMode: string) => {
            switch (apiAccessMode) {
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PUBLIC.name:
                    return RepositoryAccessMode.PUBLIC
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PRIVATE.name:
                    return RepositoryAccessMode.PRIVATE
                default:
                    console.error(`Invalid accessMode=${apiAccessMode}`)
                    throw Error("Error converting accessMode=" + apiAccessMode)
            }
        }
        return {
            name: apiRepository.name,
            // type is broken, accessMode is deserialized to a string
            accessMode: convertAccessMode(apiRepository.accessMode as unknown as string),
            description: convertNullableToTypescriptModel(apiRepository.description),
            cloneUrl: apiRepository.httpUrl,
        }
    }

    convertRepositoryFileTypeToLocalModel(apiRepositoryFile: ApiRepositoryFile): RepositoryFile {
        const convertFileType = (apiFileType: string) => {
            switch (apiFileType) {
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepositoryFile.Type.FOLDER.name:
                    return RepositoryFileType.FOLDER
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepositoryFile.Type.REGULAR.name:
                    return RepositoryFileType.REGULAR
                default:
                    console.error(`Invalid accessMode=${apiFileType}`)
                    return RepositoryFileType.UNKNOWN
            }
        }

        return {
            name: apiRepositoryFile.name,
            type: convertFileType(apiRepositoryFile.type as unknown as string),
        };
    }
}
