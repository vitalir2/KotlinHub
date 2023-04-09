export function readSetting(name: string): string | undefined {
    const value = sessionStorage.getItem(name);
    if (value === null) {
        return undefined;
    }
    return value;
}

export function setSetting(name: string, value: string) {
    sessionStorage.setItem(name, value);
}

export function clearSetting(name: string) {
    sessionStorage.removeItem(name);
}
