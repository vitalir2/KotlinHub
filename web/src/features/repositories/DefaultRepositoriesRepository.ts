import {GetRepositoriesResponse, RepositoriesRepository} from "./RepositoriesRepository";
import {baseApi} from "../../app/fetch";

export class DefaultRepositoriesRepository implements RepositoriesRepository {
    getRepositories(userId: number): Promise<GetRepositoriesResponse> {
        return baseApi.get<GetRepositoriesResponse>("/repositories/" + userId)
            .then(response => response.data)
    }
}
