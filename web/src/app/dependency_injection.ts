import {RepositoriesGraph} from "../features/repositories/RepositoriesGraph";
import {DefaultRepositoriesRepository} from "../features/repositories/DefaultRepositoriesRepository";
import {AuthGraph} from "../features/login/AuthGraph";
import {DefaultAuthRepository} from "../features/login/DefaultAuthRepository";

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
export function createAppGraph() {
    const repositoriesRepository = new DefaultRepositoriesRepository()
    const repositoriesGraph = new RepositoriesGraph(repositoriesRepository)

    const authRepository = new DefaultAuthRepository()
    const authGraph = new AuthGraph(authRepository)

    return new AppGraph(repositoriesGraph, authGraph)
}
