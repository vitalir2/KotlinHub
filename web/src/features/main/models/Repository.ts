export type Repository = {
    name: string,
    accessMode: RepositoryAccessMode,
    updatedAt: string,
    description?: string,
}

export enum RepositoryAccessMode {
    PUBLIC = "Public",
    PRIVATE = "Private",
}
