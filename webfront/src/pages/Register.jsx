import { useState } from "react";
import { Input } from "@/components/ui/Input";
import { Button } from "@/components/ui/Button";

export default function Register() {
    const [tab, setTab] = useState("student");
    const [email, setEmail] = useState("");
    const [isEmailChecked, setIsEmailChecked] = useState(false);
    const [emailMessage, setEmailMessage] = useState("");
    const [loadingEmailCheck, setLoadingEmailCheck] = useState(false);
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [name, setName] = useState("");
    const [phone, setPhone] = useState("");
    const [affiliation, setAffiliation] = useState("");
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");


    // ✅ 이메일 중복 확인 요청 (role 추가)
    const checkEmailDuplicate = async () => {
        if (!email) {
            setEmailMessage("⚠️ 이메일을 입력하세요.");
            return;
        }

        setLoadingEmailCheck(true);
        setEmailMessage("");
        setMessage("");  // ✅ 기존 회원가입 실패 메시지 초기화

        const role = tab === "instructor" ? "instructor" : "student";
        const apiUrl = `http://localhost:8080/api/member/email-check?email=${email}&role=${role}`;

        try {
            const response = await fetch(apiUrl);

            if (!response.ok) {
                throw new Error(`서버 오류: ${response.status}`);
            }

            const result = await response.json();

            if (result.available) {
                setEmailMessage("✅ 사용 가능한 이메일입니다!");
                setIsEmailChecked(true);
            } else {
                setEmailMessage("❌ 이미 사용 중인 이메일입니다!");
                setIsEmailChecked(false);
            }
        } catch (error) {
            console.error("이메일 중복 확인 오류:", error);
            setEmailMessage(`🚨 이메일 확인 실패! (${error.message})`);
        } finally {
            setLoadingEmailCheck(false);
        }
    };

    // ✅ 회원가입 요청
    const handleRegister = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setMessage("비밀번호가 일치하지 않습니다!");
            return;
        }

        if (!isEmailChecked) {
            setMessage("이메일 중복 확인을 먼저 해주세요.");
            return;
        }

        setLoading(true);
        setMessage("");  // ✅ 기존 메시지 초기화

        try {
            const apiUrl = tab === "instructor"
                ? "http://localhost:8080/api/member/instructor/register"
                : "http://localhost:8080/api/member/student/register";

            const requestData = {
                email,
                password,
                name,
                phone,
                role: tab, // 추가
                ...(tab === "instructor" && { affiliation })
            };

            const response = await fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestData),
            });

            const result = await response.json();

            if (response.ok) {
                setMessage(result.message || "회원가입 성공!");
            } else {
                setMessage("회원가입 실패! " + (result.detail || ""));
            }
        } catch (error) {
            console.error("회원가입 요청 중 오류 발생:", error);
            setMessage("회원가입 실패! 서버와 연결할 수 없습니다.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="w-full max-w-md p-6 bg-white shadow-md rounded-lg">
                <h1 className="text-4xl font-bold text-center mb-6">
                    <span className="text-gray-900">Gathe</span>
                    <span className="text-blue-600">ria</span>
                </h1>

                <div className="flex bg-gray-200 rounded-lg p-1 mb-6">
                    <button
                        className={`flex-1 text-center p-2 rounded-md transition ${tab === "instructor" ? "bg-white font-bold shadow-md" : "text-gray-500 hover:bg-gray-300"}`}
                        onClick={() => setTab("instructor")}
                    >
                        교수자
                    </button>
                    <button
                        className={`flex-1 text-center p-2 rounded-md transition ${tab === "student" ? "bg-white font-bold shadow-md" : "text-gray-500 hover:bg-gray-300"}`}
                        onClick={() => setTab("student")}
                    >
                        학생
                    </button>
                </div>

                {message && <p className="text-center text-red-500 mb-4">{message}</p>}

                <form className="space-y-4" onSubmit={handleRegister}>
                    {/* 이메일 입력 + 중복 확인 */}
                    <div>
                        <label className="block text-sm font-medium">이메일 (아이디)</label>
                        <div className="flex gap-2">
                            <Input
                                type="email"
                                placeholder="이메일 입력"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value);
                                    setIsEmailChecked(false); // 이메일 변경 시 중복 체크 다시 해야 함
                                    setEmailMessage("");
                                }}
                                className="w-full px-4"
                            />
                            <Button
                                type="button"
                                onClick={checkEmailDuplicate}
                                className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                            >
                                {loadingEmailCheck ? "확인 중..." : "중복 확인"}
                            </Button>
                        </div>
                        {/* ✅ 이메일 중복 확인 메시지 출력 */}
                        {emailMessage && (
                            <p className={`text-sm mt-1 ${emailMessage.includes("✅") ? "text-green-600" : "text-red-500"}`}>
                                {emailMessage}
                            </p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium">비밀번호</label>
                        <Input type="password" placeholder="비밀번호 입력" value={password} onChange={(e) => setPassword(e.target.value)} className="w-full px-4" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium">비밀번호 재입력</label>
                        <Input type="password" placeholder="비밀번호 재입력" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} className="w-full px-4" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium">이름</label>
                        <Input type="text" placeholder="이름" value={name} onChange={(e) => setName(e.target.value)} className="w-full px-4" />
                    </div>
                    {tab === "instructor" && (
                        <div>
                            <label className="block text-sm font-medium">소속</label>
                            <Input type="text" placeholder="소속" value={affiliation} onChange={(e) => setAffiliation(e.target.value)} className="w-full px-4" />
                        </div>
                    )}
                    <div>
                        <label className="block text-sm font-medium">휴대폰 번호</label>
                        <Input type="text" placeholder="휴대폰 번호" value={phone} onChange={(e) => setPhone(e.target.value)} className="w-full px-4" />
                    </div>

                    <Button type="submit" className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700">
                        {loading ? "회원가입 중..." : "회원가입"}
                    </Button>
                </form>
            </div>
        </div>
    );
}