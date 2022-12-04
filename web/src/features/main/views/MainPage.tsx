function Profile() {
    return (
        <aside className="flex flex-col gap-2 p-2">
            <img className="w-48 h-48 rounded-full" src={"https://avatars.githubusercontent.com/u/35116812?v=4"} alt="Profile"/>
            <p className="font-bold text-start">Vitalir</p>
            <p className="text-base font-light text-start">Description about me</p>
        </aside>
    )
}

function MainContent() {
    const repositories = [
        {
            name: "KotlinHub",
            accessMode: RepositoryAccessMode.PUBLIC,
            updatedAt: new Date().toDateString(),
            description: "Version control hosting written in Kotlin",
        },
        {
            name: "React",
            accessMode: RepositoryAccessMode.PUBLIC,
            updatedAt: new Date().toDateString(),
            description: "A declarative, efficient, and flexible JavaScript library for building user interfaces",
        },
    ]
    return (
        <main className="flex flex-col p-4">
            <RepositoriesView repositories={repositories}/>
        </main>
    )
}

type RepositoriesViewProps = {
    repositories: Repository[],
}

type Repository = {
    name: string,
    accessMode: RepositoryAccessMode,
    updatedAt: string,
    description?: string,
}

enum RepositoryAccessMode {
    PUBLIC = "Public",
    PRIVATE = "Private",
}

function RepositoriesView(props: RepositoriesViewProps) {
    const {repositories} = props
    return (
        <div className="flex flex-col gap-4">
            {repositories.map((repository) => <RepositoryView repository={repository} />)}
        </div>
    )
}

type RepositoryViewProps = {
    repository: Repository,
}
function RepositoryView(props: RepositoryViewProps) {
    const {repository} = props
    return (
        <div className="flex flex-col">
            <div className="flex flex-row gap-1">
                <a className="text-blue-700 font-semibold text-lg" rel="#">{repository.name}</a>
                <span className="border-2 border-slate-50 font-bold">{repository.accessMode}</span>
            </div>
            <p className="text-xs font-light text-start">{repository.description}</p>
            <p className="text-xs font-light text-start mt-1">{"updated " + repository.updatedAt}</p>
        </div>
    )
}

function MainPage() {
    return (
        <div className="flex flex-row justify-center gap-2">
            <Profile/>
            <div className="border-r"/>
            <MainContent/>
        </div>
    )
}

export default MainPage;
