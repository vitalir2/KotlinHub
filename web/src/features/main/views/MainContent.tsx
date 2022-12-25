import {Repository} from "../models/Repository";
import {RepositoriesView} from "./RepositoriesView";
import {RepositoriesPlaceholderView} from "./RepositoriesPlaceholderView";

export type MainContentProps = {
    repositories: Repository[],
}

export function MainContent(props: MainContentProps) {
    const {repositories} = props
    if (repositories.length === 0) {
        return (
            <main className="flex flex-col p-4">
                <RepositoriesPlaceholderView/>
            </main>
        )
    }
    return (
        <main className="flex flex-col p-4">
            <RepositoriesView repositories={repositories}/>
        </main>
    )
}
