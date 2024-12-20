package dev.scgursel.llama2.Controller;


import dev.scgursel.llama2.Model.Conversation;
import dev.scgursel.llama2.Model.History;
import dev.scgursel.llama2.Model.Request;
import dev.scgursel.llama2.Model.Response;
import dev.scgursel.llama2.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class ChatController {

    private final ChatService chatService;


    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Conversation>> getAllConversations() {
        List <Conversation> conversations = chatService.getAllConversations();
        return  ResponseEntity.ok(conversations);
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<History>> getAllConversations(@PathVariable Long conversationId) {
        List <History> historyList = chatService.getHistory(conversationId);
        return  ResponseEntity.ok(historyList);
    }
    @DeleteMapping("/history/{conversationId}")
    public ResponseEntity<String> deleteConversation(@PathVariable Long conversationId) {
        Long deleted =chatService.deleteConversation(conversationId);
        return  ResponseEntity.ok(deleted+" id success deleted");
    }

    @PostMapping("/chat")
    public ResponseEntity<Response> generate(@RequestBody Request request) {
        History chatResponse = chatService.call(request.getMessage(), request.getConversationId());
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(new Response(chatResponse.getResponse(),chatResponse.getConversation())); // Cleaner syntax
    }


}

