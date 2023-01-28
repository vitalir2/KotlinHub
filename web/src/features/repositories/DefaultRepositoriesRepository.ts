import * as platformShared from "platform-shared/platform-shared"
import {baseApi, convertNullableToTypescriptModel, getDefaultHeaders} from "../../app/fetch";
import {RepositoriesRepository} from "./RepositoriesRepository";
import {Repository, RepositoryAccessMode} from "./Repository";

type GetRepositoriesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
type ApiRepository = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
export class DefaultRepositoriesRepository implements RepositoriesRepository {
    getRepositories(userId: string): Promise<Repository[]> {
        return baseApi.get<GetRepositoriesResponse>(`/repositories/${userId}`, {
            headers: getDefaultHeaders(),
        })
            .then(response => response.data.repositories)
            .then(repositories => repositories.map(repository => this.convertToLocalModel(repository)))
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
        }
    }
}
