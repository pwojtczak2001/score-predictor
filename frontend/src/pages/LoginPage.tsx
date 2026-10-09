import { useState } from "react"
import type { FormEvent } from "react"
import { Link, useNavigate } from "react-router-dom"
import "./AuthPage.css"
import { saveToken } from "../services/authService"

function LoginPage() {
    const [username, setUsername] = useState("")
    const navigate = useNavigate()
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [isLoading, setIsLoading] = useState(false)

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    setError("")

    if (!username.trim()) {
        setError("Podaj nazwę użytkownika.")
        return
    }

    if (!password) {
        setError("Podaj hasło.")
        return
    }

    try {
        setIsLoading(true)

        const response = await fetch(
            "http://localhost:8080/auth/login",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    username: username.trim(),
                    password,
                }),
            }
        )

        const data = await response.json()

        if (!response.ok) {
            setError(
                response.status === 401
                    ? "Nieprawidłowa nazwa użytkownika lub hasło."
                    : "Wystąpił błąd podczas logowania."
            )
            return
        }

        if (data.token) {
            saveToken(data.token)
            navigate("/app")
            return
        }

        setError("Logowanie nie powiodło się.")
    } catch {
        setError("Nie udało się połączyć z serwerem.")
    } finally {
        setIsLoading(false)
    }
}

    return (
        <main className="auth-page">
            <div className="auth-page__card">

                <div className="auth-page__header">
                    <img
                        className="auth-page__logo"
                        src="/ppg.png"
                        alt="Przed Pierwszym Gwizdkiem"
                    />

                    <h1>Zaloguj się</h1>

                    <p>
                        Zaloguj się, aby typować wyniki i rywalizować z innymi.
                    </p>
                </div>

                <form
                    className="auth-page__form"
                    onSubmit={handleSubmit}
                >

                    <div className="auth-page__field">
                        <label htmlFor="username">
                            Nazwa użytkownika
                        </label>

                        <input
                            id="username"
                            type="text"
                            placeholder="Wpisz nazwę użytkownika"
                            value={username}
                            onChange={(event) => setUsername(event.target.value)}
                        />
                    </div>

                    <div className="auth-page__field">
                        <label htmlFor="password">
                            Hasło
                        </label>

                        <input
                            id="password"
                            type="password"
                            placeholder="Wpisz swoje hasło"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                        />
                    </div>

                    {error && (
                        <p className="auth-page__error">
                            {error}
                        </p>
                    )}

                    <button
                        type="submit"
                        className="auth-page__submit"
                        disabled={isLoading}
                    >
                        {isLoading ? "LOGOWANIE..." : "ZALOGUJ SIĘ"}
                    </button>

                </form>

                <div className="auth-page__footer">
                    <span>Nie masz jeszcze konta?</span>

                    <Link to="/register">
                        Załóż konto
                    </Link>
                </div>

                <Link
                    to="/"
                    className="auth-page__back"
                >
                    ← Wróć na stronę główną
                </Link>

            </div>
        </main>
    )
}

export default LoginPage