package dev.scgursel.llama2.Model;

import jakarta.persistence.*;


@Entity
public class History {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL )
    private Conversation conversation;

    @Lob
    private String prompt;
    @Lob
    private String response;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public History(String prompt, String response) {
        this.prompt = prompt;
        this.response = response;
    }

    public History() {

    }

    @Override
    public String toString() {
        return String.format("""
                        `history_entry`:
                            `prompt`: %s
                        
                            `response`: %s
                        -----------------
                       \n
            """, prompt, response);
    }
}