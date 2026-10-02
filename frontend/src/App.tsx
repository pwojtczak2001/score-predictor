import { BrowserRouter, Routes, Route } from "react-router-dom"
import PublicLayout from "./components/layout/PublicLayout"
import LoginPage from "./pages/LoginPage"
import RegisterPage from "./pages/RegisterPage"
import NotFoundPage from "./pages/NotFoundPage"

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<PublicLayout />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="*" element={<NotFoundPage />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App