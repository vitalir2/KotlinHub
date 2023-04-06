import {RepositoriesGraph} from "../features/repositories/RepositoriesGraph";
import {DefaultRepositoriesRepository} from "../features/repositories/DefaultRepositoriesRepository";
import {UserGraph} from "../features/user/UserGraph";
import {DefaultUserRepository} from "../features/user/DefaultUserRepository";

export class AppGraph {
    readonly repositoriesGraph: RepositoriesGraph
    readonly userGraph: UserGraph

    constructor(
        repositoriesGraph: RepositoriesGraph,
        authGraph: UserGraph,
    ) {
        this.repositoriesGraph = repositoriesGraph;
        this.userGraph = authGraph;
    }
}
function createAppGraph() {
    const repositoriesRepository = new DefaultRepositoriesRepository()
    const repositoriesGraph = new RepositoriesGraph(repositoriesRepository)

    const userRepository = new DefaultUserRepository()
    const userGraph = new UserGraph(userRepository)

    return new AppGraph(repositoriesGraph, userGraph)
}

export const appGraph = createAppGraph()
