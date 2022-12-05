import {Repository} from "../models/Repository";
import {RepositoryView} from "./RepositoryView";

export type RepositoriesViewProps = {
    repositories: Repository[],
}

export function RepositoriesView(props: RepositoriesViewProps) {
    const {repositories} = props
    return (
        <div className="flex flex-col gap-4">
            {repositories.map((repository) => <RepositoryView repository={repository}/>)}
        </div>
    )
}
