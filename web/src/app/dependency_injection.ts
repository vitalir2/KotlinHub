import {RepositoriesGraph} from "../features/repositories/RepositoriesGraph";
import {DefaultRepositoriesRepository} from "../features/repositories/DefaultRepositoriesRepository";
import {AuthGraph} from "../features/auth/AuthGraph";
import {DefaultAuthRepository} from "../features/auth/DefaultAuthRepository";

export class AppGraph {
    readonly repositoriesGraph: RepositoriesGraph
    readonly authGraph: AuthGraph

    constructor(
        repositoriesGraph: RepositoriesGraph,
        authGraph: AuthGraph,
    ) {
        this.repositoriesGraph = repositoriesGraph;
        this.authGraph = authGraph;
    }
}
function createAppGraph() {
    const repositoriesRepository = new DefaultRepositoriesRepository()
    const repositoriesGraph = new RepositoriesGraph(repositoriesRepository)

    const authRepository = new DefaultAuthRepository()
    const userGraph = new AuthGraph(authRepository)

    return new AppGraph(repositoriesGraph, userGraph)
}

export const appGraph = createAppGraph()
