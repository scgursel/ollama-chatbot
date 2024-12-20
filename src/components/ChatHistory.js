import React from "react";
import "../style/ChatHistory.css";

function ChatHistory({
  conversations,
  onConversationSelect,
  onConversationDelete,
  onNewConversation,
  isCollapsed,
  toggleCollapse,
  isDarkMode,
}) {
  return (
    <div
      className={`chat-history ${isDarkMode ? "dark" : "light"} ${
        isCollapsed ? "collapsed" : ""
      }`}
    >
      <button className="toggle-collapse-btn" onClick={toggleCollapse}>
        {isCollapsed ? ">" : "<"}
      </button>
      {!isCollapsed && (
        <>
          <button className="new-conversation-btn" onClick={onNewConversation}>
            New Conversation
          </button>
          <ul>
            {conversations.map((conversation) => (
              <li
                key={conversation.id}
                onClick={() => onConversationSelect(conversation.id)}
              >
                {conversation.title}
                <button
                  className="delete"
                  onClick={(e) => {
                    e.stopPropagation(); 
                    onConversationDelete(conversation.id);
                  }}
                >
    
                  <i className="fas fa-trash"></i>
                </button>
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
}

export default ChatHistory;
