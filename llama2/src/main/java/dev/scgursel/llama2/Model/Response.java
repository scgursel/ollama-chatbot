package dev.scgursel.llama2.Model;

public class Response {
    public String result;

    public Conversation conversation;

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Response(String result, Conversation conversation) {
        this.result = result;
        this.conversation = conversation;
    }
}
