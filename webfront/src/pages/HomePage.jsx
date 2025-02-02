import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();

    return (
        <div className="min-h-screen bg-gray-100">
            {/* ✅ 헤더 */}
            <header className="flex justify-between items-center p-6 bg-white shadow-md">
                <h1 className="text-3xl font-bold text-gray-900">
                    <span className="text-gray-900">Gathe</span>
                    <span className="text-blue-600">ria</span>
                </h1>
                <button
                    className="text-gray-700 hover:underline"
                    onClick={() => navigate("/login")} // ✅ 로그인 버튼 클릭 시 이동
                >
                    로그인
                </button>
            </header>

            {/* ✅ 메인 배너 */}
            <section className="bg-blue-700 text-white text-center py-16">
                <h2 className="text-5xl font-bold mb-4">Gatheria</h2>
                <p className="text-xl">팀 관리와 성과 분석의 새로운 기준</p>
            </section>

            {/* ✅ 소개 섹션 */}
            <section className="max-w-3xl mx-auto text-center py-12">
                <h3 className="text-2xl font-bold text-gray-900 mb-4">효율적인 팀 관리와 협업</h3>
                <p className="text-gray-600">
                    Gatheria를 사용하면 팀원의 활동을 한눈에 파악하고, 참여도를 분석하여 더 나은 협업을 이끌어낼 수 있습니다.
                    교수자와 학생 모두에게 최적화된 기능으로, 팀 관리를 혁신하세요.
                </p>

                {/* ✅ 주요 특징 */}
                <ul className="mt-6 text-gray-700 text-left mx-auto max-w-md">
                    <li className="flex items-center mb-2">
                        ✅ 실시간 채팅과 파일 공유를 통해 빠르고 원활한 소통
                    </li>
                    <li className="flex items-center">
                        ✅ 기여도와 참여도를 데이터 기반으로 성과 리포트 제공
                    </li>
                </ul>

                {/* ✅ 지금 시작하기 버튼 */}
                <button
                    className="mt-6 px-6 py-3 bg-blue-600 text-white rounded-md text-lg hover:bg-blue-700"
                    onClick={() => setIsModalOpen(true)} // ✅ 모달 열기
                >
                    지금 시작하기
                </button>
            </section>

            {/* ✅ 로그인/회원가입 모달 */}
            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg text-center">
                        <h3 className="text-xl font-bold text-gray-900 mb-4">Gatheria 시작하기</h3>
                        <p className="text-gray-600 mb-6">로그인 또는 회원가입 후 이용하세요.</p>
                        <div className="space-y-4">
                            <button
                                className="w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                onClick={() => navigate("/login")} // ✅ 로그인 페이지로 이동
                            >
                                로그인
                            </button>
                            <button
                                className="w-full px-4 py-2 bg-gray-300 text-gray-900 rounded hover:bg-gray-400"
                                onClick={() => navigate("/register")} // ✅ 회원가입 페이지로 이동
                            >
                                회원가입
                            </button>
                        </div>
                        {/* ✅ 모달 닫기 버튼 */}
                        <button
                            className="mt-4 text-gray-600 hover:text-gray-900"
                            onClick={() => setIsModalOpen(false)}
                        >
                            닫기
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}