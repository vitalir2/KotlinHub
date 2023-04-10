export interface User {
    username: string,
    firstName?: string,
    lastName?: string,
    email?: string,
}

export function getFullName(user: User): string | undefined {
    if (user.firstName === undefined) {
        return undefined
    } else if (user.lastName === undefined) {
        return user.firstName
    } else {
        return `${user.firstName} ${user.lastName}`
    }
}
