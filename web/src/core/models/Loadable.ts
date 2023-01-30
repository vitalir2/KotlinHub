export interface Loading {
    kind: "loading"
}
export interface Loaded<T> {
    kind: "loaded"
    data: T
}
export interface LoadingError {
    kind: "error"
    error: string
}

export type Loadable<T> = Loading | Loaded<T> | LoadingError
