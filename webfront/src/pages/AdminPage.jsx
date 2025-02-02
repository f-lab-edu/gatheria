import { useState, useEffect } from "react";

export default function AdminPage() {
    const [pendingInstructors, setPendingInstructors] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const size = 10; // 한 페이지당 10명 표시

    // ✅ 교수자 목록 가져오기 (페이지네이션 적용 & 최신화 지원)
    const fetchPendingInstructors = () => {
        fetch(`http://localhost:8080/api/admin/pending-instructors?page=${currentPage}&size=${size}`)
            .then((response) => response.json())
            .then((data) => {
                setPendingInstructors(data.instructors);
                setTotalPages(data.totalPages);
            })
            .catch((error) => console.error("교수자 목록 불러오기 실패:", error));
    };

    // ✅ 컴포넌트 로드 및 페이지 변경 시 호출
    useEffect(() => {
        fetchPendingInstructors();
    }, [currentPage]);

    // ✅ 새로고침 버튼 클릭 시 최신 데이터 불러오기
    const handleRefresh = () => {
        fetchPendingInstructors();
    };

    // ✅ 교수자 승인 요청
    const approveInstructor = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/approve-instructor/${id}`, {
                method: "POST",
            });

            if (!response.ok) {
                throw new Error("승인 실패");
            }

            alert("교수자가 승인되었습니다!");
            setPendingInstructors((prev) => prev.filter((instructor) => instructor.id !== id));
        } catch (error) {
            console.error("승인 오류:", error);
            alert("승인 중 오류 발생!");
        }
    };

    return (
        <div className="p-6 bg-gray-100 min-h-screen flex flex-col items-center">
            {/* ✅ 프로젝트 이름 추가 */}
            <h1 className="text-4xl font-bold text-gray-900 mb-6">
                Gatheria <span className="text-blue-600">Admin</span>
            </h1>

            <div className="w-full max-w-4xl bg-white p-6 shadow-md rounded-lg">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-bold text-center">교수자 승인 관리</h2>
                    {/* ✅ 새로고침 버튼 추가 */}
                    <button
                        onClick={handleRefresh}
                        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                        🔄 새로고침
                    </button>
                </div>

                <table className="w-full border-collapse border border-gray-300">
                    <thead>
                    <tr className="bg-gray-200">
                        <th className="border p-2">이름</th>
                        <th className="border p-2">이메일</th>
                        <th className="border p-2">소속</th>
                        <th className="border p-2">승인</th>
                    </tr>
                    </thead>
                    <tbody>
                    {pendingInstructors.length > 0 ? (
                        pendingInstructors.map((instructor) => (
                            <tr key={instructor.id} className="text-center">
                                <td className="border p-2">{instructor.name}</td>
                                <td className="border p-2">{instructor.email}</td>
                                <td className="border p-2">{instructor.affiliation}</td>
                                <td className="border p-2">
                                    <button
                                        className="px-3 py-1 bg-green-500 text-white rounded hover:bg-green-600"
                                        onClick={() => approveInstructor(instructor.id)}
                                    >
                                        승인
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="4" className="border p-4 text-center text-gray-500">
                                승인 대기 중인 교수자가 없습니다.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>

                {/* ✅ 페이지네이션 버튼 (마지막 페이지일 경우 "다음" 비활성화) */}
                <div className="flex justify-center mt-4 space-x-2">
                    <button
                        disabled={currentPage === 1}
                        onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                        className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 disabled:opacity-50"
                    >
                        이전
                    </button>
                    <span className="px-4 py-2">페이지 {currentPage} / {totalPages}</span>
                    <button
                        disabled={currentPage >= totalPages} // ✅ 마지막 페이지일 경우 비활성화
                        onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                        className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400 disabled:opacity-50"
                    >
                        다음
                    </button>
                </div>
            </div>
        </div>
    );
}