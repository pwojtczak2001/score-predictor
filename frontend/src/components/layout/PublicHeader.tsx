import { useState } from "react"
import "./PublicHeader.css"

function PublicHeader() {
    const [isMenuOpen, setIsMenuOpen] = useState(false)

    return (
        <header className="public-header">
            <div className="public-header__logo">
                <img
                    className="public-header__logo-desktop"
                    src="/ppg-logo.jpg"
                    alt="Przed Pierwszym Gwizdkiem"
                />

                <img
                    className="public-header__logo-mobile"
                    src="/ppg-logo-mobile.jpg"
                    alt="PPG"
                />
            </div>

            <nav className="public-header__actions">
                <button className="public-header__login">
                    Zaloguj
                </button>

                <button className="public-header__register">
                    Załóż konto
                </button>
            </nav>

            <button className="public-header__menu" 
                    onClick={() => setIsMenuOpen(!isMenuOpen)}>
                    {isMenuOpen ? "✕" : "☰"}
            </button>

            {isMenuOpen && (
                <nav className="public-header__mobile-menu">
                    <button className="public-header__login">
                        Zaloguj
                    </button>
                    <button className="public-header__register">
                        Załóż konto
                    </button>
                </nav>
            )}

        </header>
    )
}

export default PublicHeader