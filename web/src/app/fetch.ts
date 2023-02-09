import axios from "axios";
import {readCookie} from "../core/settings/Cookies";
import {SETTING_AUTH_TOKEN} from "../core/settings/SettingsNames";

const BASE_API_PATH = "http://localhost/api/v1/"

export const baseApi = axios.create({
    baseURL: BASE_API_PATH,
    validateStatus: status => {
        return status < 400
    },
})

export function getDefaultHeaders() {
    return {
        "Authorization": `Bearer ${readCookie(SETTING_AUTH_TOKEN)}`,
    }
}

export function convertNullableToTypescriptModel<T>(value: T | null | undefined): T | undefined {
    if (value === null) {
        return undefined
    }

    return value
}
