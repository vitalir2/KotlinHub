export interface RepositoryFile {
    name: string,
    type: RepositoryFileType,
}

export enum RepositoryFileType {
    FOLDER,
    REGULAR,
    UNKNOWN,
}
