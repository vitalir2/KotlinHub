import * as process from "process";

function isProductionEnv(): boolean {
    return process.env.NODE_ENV === 'production'
}

export function isProductionBackend(): boolean {
    return process.env.REACT_APP_BACKEND_ENVIRONMENT === 'production' || isProductionEnv()
}

export const BACKEND_URL_PRODUCTION = process.env.REACT_APP_BACKEND_URL_PRODUCTION
export const BACKEND_URL_DEVELOPMENT = "http://127.0.0.1"
