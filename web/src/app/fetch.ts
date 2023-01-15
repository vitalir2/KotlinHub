import axios from "axios";

const BASE_API_PATH = "http://localhost/api/v1/"

export const baseApi = axios.create({
    baseURL: BASE_API_PATH,
})

export function convertNullableToTypescriptModel<T>(value: T | null | undefined): T | undefined {
    if (value === null) {
        return undefined
    }

    return value
}
