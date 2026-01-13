import { useState } from "react";

export default function App() {
  const [messages, setMessages] = useState([
    { role: "assistant", content: "Hi! Ask me anything." },
  ]);
  const [text, setText] = useState("");
  const [loading, setLoading] = useState(false);

  async function send() {
    const trimmed = text.trim();
    if (!trimmed) return;

    const nextMessages = [...messages, { role: "user", content: trimmed }];
    setMessages(nextMessages);
    setText("");
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8080/api/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ messages: nextMessages }),
      });

      if (!res.ok) {
  const text = await res.text();
  throw new Error(text || `HTTP ${res.status}`);
}
      const data = await res.json();
      setMessages([...nextMessages, { role: "assistant", content: data.reply }]);
    } catch (e) {
      setMessages([
        ...nextMessages,
        { role: "assistant", content: "Backend error. Is Spring Boot running on :8080?" },
      ]);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ maxWidth: 720, margin: "40px auto", fontFamily: "system-ui" }}>
      <h2>Chatbot (React + Spring Boot)</h2>

      <div style={{ border: "1px solid #ddd", borderRadius: 10, padding: 12, minHeight: 360 }}>
        {messages.map((m, i) => (
          <div key={i} style={{ margin: "10px 0" }}>
            <b>{m.role}:</b> {m.content}
          </div>
        ))}
        {loading && <div><b>assistant:</b> typing...</div>}
      </div>

      <div style={{ display: "flex", gap: 10, marginTop: 12 }}>
        <input
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && send()}
          placeholder="Type your message..."
          style={{ flex: 1, padding: 10, borderRadius: 10, border: "1px solid #ddd" }}
        />
        <button onClick={send} style={{ padding: "10px 16px", borderRadius: 10 }}>
          Send
        </button>
      </div>
    </div>
  );
}
