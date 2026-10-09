const TOKEN_KEY = "token"

export function getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY)
}

export function saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken(): void {
    localStorage.removeItem(TOKEN_KEY)
}

export function isTokenValid(): boolean {
    const token = getToken()

    if (!token) {
        return false
    }

    try {
        const payload = JSON.parse(
            atob(token.split(".")[1])
        )

        if (!payload.exp) {
            return false
        }

        return payload.exp * 1000 > Date.now()
    } catch {
        return false
    }
}