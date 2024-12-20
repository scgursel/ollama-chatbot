import React from "react";
import "../style/ChatWindow.css";

function ChatWindow({
  messages,
  currentMessage,
  setCurrentMessage,
  sendMessage,
  isTyping,
  isCollapsed,
  isDarkMode,
}) {
  return (
    <div
      className={`chat-window ${isDarkMode ? "dark" : "light"} ${
        isCollapsed ? "expanded" : ""
      }`}
    >
      <div className="messages">
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.sender}`}>
            {msg.text}
          </div>
        ))}
        {isTyping && <div className="typing">Typing</div>}
      </div>

      <div className="input-area">
        <textarea
          type="text"
          value={currentMessage}
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault();
              sendMessage();
              
            }
          }}
          onChange={(e) => setCurrentMessage(e.target.value)}
          placeholder="Type your message..."
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}

export default ChatWindow;
