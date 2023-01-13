import axios from "axios";

const BASE_API_PATH = "http://localhost:8080/api/v1/"

export const baseApi = axios.create({
    baseURL: BASE_API_PATH,
})
