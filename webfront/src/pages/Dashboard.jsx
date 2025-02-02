import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import StudentDashboard from "./components/StudentDashboard";
import InstructorDashboard from "./components/InstructorDashboard";

export default function Dashboard() {
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const accessToken = localStorage.getItem("accessToken");

        // ✅ 로그인이 안 되어 있으면 로그인 페이지로 이동
        if (!accessToken) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        const userInfo = {
            name: localStorage.getItem("name"),
            email: localStorage.getItem("email"),
            role: localStorage.getItem("role"),
            affiliation: localStorage.getItem("affiliation"), // 교수자만 사용
            phone: localStorage.getItem("phone"),
        };

        setUser(userInfo);
    }, [navigate]);

    // ✅ user 정보가 로딩되지 않았다면 로딩 화면 표시
    if (!user) {
        return <p className="text-center text-gray-500">로딩 중...</p>;
    }

    // ✅ 역할에 따라 올바른 대시보드로 이동
    return user.role === "instructor" ? (
        <InstructorDashboard user={user} />
    ) : (
        <StudentDashboard user={user} />
    );
}