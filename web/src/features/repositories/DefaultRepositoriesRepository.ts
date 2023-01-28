import * as platformShared from "platform-shared/platform-shared"
import {baseApi, convertNullableToTypescriptModel, getDefaultHeaders} from "../../app/fetch";
import {RepositoriesRepository} from "./RepositoriesRepository";
import {Repository, RepositoryAccessMode} from "./Repository";

type GetRepositoriesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
type ApiRepository = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
type ApiRepositoryAccessMode = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode

export class DefaultRepositoriesRepository implements RepositoriesRepository {
    getRepositories(userId: string): Promise<Repository[]> {
        return baseApi.get<GetRepositoriesResponse>(`/repositories/${userId}`, {
            headers: getDefaultHeaders(),
        })
            .then(response => response.data.repositories)
            .then(repositories => repositories.map(repository => this.convertToLocalModel(repository)))
    }

    convertToLocalModel(apiRepository: ApiRepository): Repository {
        const convertAccessMode = (apiAccessMode: ApiRepositoryAccessMode) => {
            switch (apiAccessMode) {
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PUBLIC:
                    return RepositoryAccessMode.PUBLIC
                case platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode.PRIVATE:
                    return RepositoryAccessMode.PRIVATE
                default:
                    throw Error("Error converting accessMode=" + apiAccessMode)
            }
        }
        return {
            name: apiRepository.name,
            accessMode: convertAccessMode(apiRepository.accessMode),
            description: convertNullableToTypescriptModel(apiRepository.description),
        }
    }
}
