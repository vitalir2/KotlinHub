import {Repository} from "../models/Repository";

export type RepositoryViewProps = {
    repository: Repository,
}

export function RepositoryView(props: RepositoryViewProps) {
    const {repository} = props
    return (
        <div className="flex flex-col">
            <div className="flex flex-row gap-1">
                <a className="text-sky-600 font-semibold text-lg" rel="#">{repository.name}</a>
                <span className="border-2 border-slate-50 font-bold">{repository.accessMode}</span>
            </div>
            <p className="text-xs font-light text-start">{repository.description}</p>
            <p className="text-xs font-light text-start mt-1">{"updated " + repository.updatedAt}</p>
        </div>
    )
}
