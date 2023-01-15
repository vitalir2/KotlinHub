import {Repository} from "../../repositories/Repository";

export interface MainState {
    userId: number,
    repositories: Repository[],
    isLoading: boolean,
    error?: string,

}
export const initialState: MainState = {
    userId: 1, // TODO
    repositories: [],
    isLoading: true,
    error: undefined,
}
