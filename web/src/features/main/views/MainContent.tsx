import {RepositoryAccessMode} from "../models/Repository";
import {RepositoriesView} from "./RepositoriesView";

export function MainContent() {
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
