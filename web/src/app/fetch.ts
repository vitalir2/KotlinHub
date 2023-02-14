import axios from "axios";
import {readCookie} from "../core/settings/Cookies";
import {SETTING_AUTH_TOKEN} from "../core/settings/SettingsNames";
import {BACKEND_URL_DEVELOPMENT, BACKEND_URL_PRODUCTION, isProductionBackend} from "./environment";

const API_PATH = "/api/v1"

function createBaseUrl(): string {
    let hostPath: string
    if (isProductionBackend() && BACKEND_URL_PRODUCTION !== undefined) {
        hostPath = BACKEND_URL_PRODUCTION
    } else {
        hostPath = BACKEND_URL_DEVELOPMENT
    }
    return `${hostPath}${API_PATH}`
}

export const baseApi = axios.create({
    baseURL: createBaseUrl(),
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
