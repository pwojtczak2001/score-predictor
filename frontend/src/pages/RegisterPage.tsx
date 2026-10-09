import { useState } from "react"
import type { FormEvent } from "react"
import "./AuthPage.css"
import { Link, useNavigate } from "react-router-dom"
import { saveToken } from "../services/authService"

function RegisterPage() {
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [confirmPassword, setConfirmPassword] = useState("")
    const [error, setError] = useState("")
    const [isLoading, setIsLoading] = useState(false)
    const [success, setSuccess] = useState("")
    const navigate = useNavigate()

    const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        setError("")
        setSuccess("")

        if (!username.trim()) {
            setError("Podaj nazwę użytkownika.")
            return
        }

        if (!email.trim()) {
            setError("Podaj adres e-mail.")
            return
        }

        if (!email.includes("@")) {
            setError("Podaj poprawny adres e-mail.")
            return
        }

        if (!password) {
            setError("Podaj hasło.")
            return
        }

        if (password.length < 8) {
            setError("Hasło musi mieć co najmniej 8 znaków.")
            return
        }

        if (!confirmPassword) {
            setError("Powtórz hasło.")
            return
        }

        if (password !== confirmPassword) {
            setError("Hasła nie są takie same.")
            return
        }

        try {
            setIsLoading(true)

            const response = await fetch(
                "http://localhost:8080/auth/register",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        username: username.trim(),
                        email: email.trim(),
                        password,
                    }),
                }
            )

            const data = await response.json()

            if (data.registrationStatus === "EMAIL_TAKEN") {
                setEmail("")
                setError("Podany adres e-mail jest już zajęty.")
                return
            }

            if (data.registrationStatus === "USERNAME_TAKEN") {
                setUsername("")
                setError("Podana nazwa użytkownika jest już zajęta.")
                return
            }

            if (data.registrationStatus === "INVALID_EMAIL") {
                setError("Podaj poprawny adres e-mail.")
                return
            }

            if (data.registrationStatus === "WEAK_PASSWORD") {
                setPassword("")
                setError("Hasło jest zbyt słabe.")
                return
            }

            if (data.success && data.registrationStatus === "SUCCESS") {
                saveToken(data.token)

                navigate("/app")
                return
            }

            setError("Nie udało się utworzyć konta.")
        } catch {
            setError(
                "Nie udało się połączyć z serwerem."
            )
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

                    <h1>Załóż konto</h1>

                    <p>
                        Dołącz do gry, typuj wyniki i rywalizuj z innymi.
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
                        <label htmlFor="email">
                            E-mail
                        </label>

                        <input
                            id="email"
                            type="email"
                            placeholder="Wpisz swój e-mail"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                        />
                    </div>

                    <div className="auth-page__field">
                        <label htmlFor="password">
                            Hasło
                        </label>

                        <input
                            id="password"
                            type="password"
                            placeholder="Wpisz hasło"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                        />
                    </div>

                    <div className="auth-page__field">
                        <label htmlFor="confirmPassword">
                            Powtórz hasło
                        </label>

                        <input
                            id="confirmPassword"
                            type="password"
                            placeholder="Powtórz hasło"
                            value={confirmPassword}
                            onChange={(event) => setConfirmPassword(event.target.value)}
                        />
                    </div>

                    {error && (
                        <p className="auth-page__error">
                            {error}
                        </p>
                    )}

                    {success && (
                        <p className="auth-page__success">
                            {success}
                        </p>
                    )}

                    <button
                        type="submit"
                        className="auth-page__submit"
                        disabled={isLoading}
                    >
                        {isLoading ? "TWORZENIE KONTA..." : "ZAŁÓŻ KONTO"}
                    </button>

                </form>

                <div className="auth-page__footer">
                    <span>Masz już konto?</span>

                    <Link to="/login">
                        Zaloguj się
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

export default RegisterPage