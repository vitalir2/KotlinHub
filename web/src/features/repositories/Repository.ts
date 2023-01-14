export interface Repository {
    name: string,
    accessMode: RepositoryAccessMode,
    description?: string,
}

export enum RepositoryAccessMode {
    PUBLIC = "public",
    PRIVATE = "private",
}
