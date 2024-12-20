package dev.scgursel.llama2.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Request {

    @JsonProperty("message")
    String message;

    @JsonProperty("conversation_id")
    Long conversationId;

    public Request() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
