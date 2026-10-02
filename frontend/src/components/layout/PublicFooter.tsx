import "./PublicFooter.css"

function PublicFooter() {
    return (
        <footer className="public-footer">

            <nav className="public-footer__navigation">
                <button>O GRZE</button>
                <button>REGULAMIN</button>
                <button>KONTAKT</button>
            </nav>

            <p className="public-footer__copyright">
                © 2026 Przed Pierwszym Gwizdkiem
            </p>

        </footer>
    )
}

export default PublicFooter