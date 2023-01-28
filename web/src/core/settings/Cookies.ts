// https://www.quirksmode.org/js/cookies.html
export function readCookie(name: string): string | undefined {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');
    for (let c of ca) {
        while (c.charAt(0) === ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return undefined;
}
