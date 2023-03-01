export interface Repository {
    name: string,
    accessMode: RepositoryAccessMode,
    description?: string,
    cloneUrl: string,
}

export enum RepositoryAccessMode {
    PUBLIC = "public",
    PRIVATE = "private",
}
