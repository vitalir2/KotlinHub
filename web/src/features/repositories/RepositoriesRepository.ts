import * as platformShared from "platform-shared/platform-shared"

export type GetRepositoriesResponse = platformShared.io.vitalir.kotlinhub.shared.feature.repository.GetRepositoriesResponse

export interface RepositoriesRepository {
    getRepositories(userId: number): Promise<GetRepositoriesResponse>
}
