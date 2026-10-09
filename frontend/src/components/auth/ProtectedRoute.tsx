import { Navigate, Outlet } from "react-router-dom"
import { isTokenValid, removeToken } from "../../services/authService"

function ProtectedRoute() {
    if (!isTokenValid()) {
        removeToken()

        return <Navigate to="/login" replace />
    }

    return <Outlet />
}

export default ProtectedRoute