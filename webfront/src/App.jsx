import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Register from "./pages/Register";
import AdminPage from "./pages/AdminPage"; // 관리자 페이지 추가
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage.jsx";
import Dashboard from "./pages/Dashboard";
import StudentDashboard from "./pages/components/StudentDashboard.jsx";
import InstructorDashboard from "./pages/components/InstructorDashboard.jsx";

import "./App.css";


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} /> {/* 기본 홈 화면 */}
                <Route path="/register" element={<Register />} /> {/* 회원가입 페이지 추가 */}
                <Route path="/admin" element={<AdminPage />} /> {/* 관리자 페이지 추가 */}
                <Route path="/login" element={<LoginPage />} /> {/* 관리자 페이지 추가 */}
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/dashboard/student" element={<StudentDashboard />} />
                <Route path="/dashboard/instructor" element={<InstructorDashboard />} />
            </Routes>
        </Router>
    );
}

export default App;