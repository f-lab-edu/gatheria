// Button Component (src/components/ui/Button.jsx)
export function Button({ children, onClick }) {
    return (
        <button
            onClick={onClick}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
        >
            {children}
        </button>
    );
}