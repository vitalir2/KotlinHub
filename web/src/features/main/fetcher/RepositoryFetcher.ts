import {Repository, RepositoryAccessMode} from "../models/Repository";

const x = require("platform-shared").io.vitalir.kotlinhub.shared.common.kek()

const stubRepositories: Repository[] = [
    {
        name: "KotlinHub" + x,
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

export async function fetchRepositories(): Promise<Repository[]> {
    return new Promise((resolve) => resolve(stubRepositories))
}
