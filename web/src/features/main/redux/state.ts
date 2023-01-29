import {Repository} from "../../repositories/Repository";
import {User} from "../../login/AuthRepository";

export interface MainState {
    user?: User,
    repositories: Repository[],
    isLoading: boolean,
    error?: string,

}
export const initialState: MainState = {
    repositories: [],
    isLoading: true,
}
