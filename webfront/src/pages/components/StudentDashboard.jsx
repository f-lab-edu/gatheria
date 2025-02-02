export default function StudentDashboard({ user }) {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <div className="w-full max-w-md bg-white p-6 shadow-lg rounded-lg">
                <h1 className="text-4xl font-bold text-center mb-6">
                    <span className="text-gray-900">Gathe</span>
                    <span className="text-blue-600">ria</span>
                </h1>

                <h2 className="text-2xl font-bold text-center mb-4">학생 대시보드</h2>

                <div className="text-center space-y-4">
                    <p className="text-lg"><strong>이름:</strong> {user?.name}</p>
                    <p className="text-lg"><strong>이메일:</strong> {user?.email}</p>
                    <p className="text-lg"><strong>역할:</strong> 학생</p>
                    <p className="text-lg"><strong>전화번호:</strong> {user?.phone}</p>

                    <button
                        onClick={() => {
                            localStorage.clear();
                            window.location.href = "/login";
                        }}
                        className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
                    >
                        로그아웃
                    </button>
                </div>
            </div>
        </div>
    );
}