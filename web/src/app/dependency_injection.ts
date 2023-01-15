import {RepositoriesGraph} from "../features/repositories/RepositoriesGraph";
import {DefaultRepositoriesRepository} from "../features/repositories/DefaultRepositoriesRepository";

export class AppGraph {
    readonly repositoriesGraph: RepositoriesGraph

    constructor(repositoriesGraph: RepositoriesGraph) {
        this.repositoriesGraph = repositoriesGraph;
    }
}
export function createAppGraph() {
    const repositoriesRepository = new DefaultRepositoriesRepository()
    const repositoriesGraph = new RepositoriesGraph(repositoriesRepository)
    return new AppGraph(repositoriesGraph)
}
