// Tabs Component (src/components/ui/Tabs.jsx)
import { useState } from "react";

export function Tabs({ children, defaultValue }) {
    const [activeTab, setActiveTab] = useState(defaultValue);

    return (
        <div>
            <div className="flex bg-gray-200 rounded-md mb-4">
                {children.map((child) =>
                    child.props.value ? (
                        <button
                            key={child.props.value}
                            onClick={() => setActiveTab(child.props.value)}
                            className={`w-full py-2 ${
                                activeTab === child.props.value ? "bg-white font-bold" : "text-gray-500"
                            }`}
                        >
                            {child.props.label}
                        </button>
                    ) : null
                )}
            </div>
            {children.map((child) =>
                child.props.value === activeTab ? <div key={child.props.value}>{child}</div> : null
            )}
        </div>
    );
}

export function TabsTrigger({ value, label }) {
    return <div value={value} label={label}></div>;
}

export function TabsContent({ children, value }) {
    return <div value={value}>{children}</div>;
}