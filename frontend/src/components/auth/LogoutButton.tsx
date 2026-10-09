import { useNavigate } from "react-router-dom"
import { removeToken } from "../../services/authService"

function LogoutButton() {
    const navigate = useNavigate()

    const handleLogout = () => {
        removeToken()
        navigate("/")
    }

    return (
        <button onClick={handleLogout}>
            Wyloguj
        </button>
    )
}

export default LogoutButton