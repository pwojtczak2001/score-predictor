import { useState } from "react"
import { Link } from "react-router-dom"
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
                <Link to="/login" className="public-header__login">
                    Zaloguj
                </Link>

                <Link to="/register" className="public-header__register">
                    Załóż konto
                </Link>
            </nav>

            <button className="public-header__menu" 
                    onClick={() => setIsMenuOpen(!isMenuOpen)}>
                    {isMenuOpen ? "✕" : "☰"}
            </button>

            {isMenuOpen && (
                <nav className="public-header__mobile-menu">
                    <Link to="/login" className="public-header__login">
                        Zaloguj
                    </Link>
                    <Link to="/register" className="public-header__register">
                        Załóż konto
                    </Link>
                </nav>
            )}

        </header>
    )
}

export default PublicHeader