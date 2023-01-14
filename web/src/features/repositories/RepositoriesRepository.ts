import * as platformShared from "platform-shared/platform-shared"

export type GetRepositoriesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse
export type ApiRepository = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository
export type ApiRepositoryAccessMode = platformShared.io.vitalir.kotlinhub.shared.feature.repository.ApiRepository.AccessMode

export interface RepositoriesRepository {
    getRepositories(userId: number): Promise<GetRepositoriesResponse>
}
