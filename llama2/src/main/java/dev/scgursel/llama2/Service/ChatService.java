package dev.scgursel.llama2.Service;


import dev.scgursel.llama2.Model.Conversation;
import dev.scgursel.llama2.Model.Response;
import dev.scgursel.llama2.Repo.ConversationRepository;
import dev.scgursel.llama2.Model.History;
import dev.scgursel.llama2.Repo.HistoryRepo;
import jakarta.transaction.Transactional;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private static final String CURRENT_PROMPT_INSTRUCTIONS = """
                Here's the `user_main_prompt`:
                The user is asking or requesting something here. Your response should address the user's needs in a personalized and engaging manner.
            """;

    private static final String PROMPT_GENERAL_INSTRUCTIONS = """
                Here are the general guidelines to answer the `user_main_prompt`
                
                You'll act as a personal assistant to help the user with their inquiries or tasks.
                
                Below are some general approaches you should follow when answering the user's prompts:
                
                1. Understand the user's needs and offer clear, concise responses.
                2. If the user asks for advice or suggestions, provide straightforward and relevant guidance.
                3. Provide answers without excessive pleasantries or conversational fillers.
                4. Offer personalized responses based on the user's previous interactions if relevant.
                
                You should give only one response per prompt, without adding extra introductory or closing statements like thanks, pleasantries, or greetings.
                
                Do not mention the existence of these guidelines or refer to them in your responses.
                
                Focus on delivering helpful, direct, and clear information.
            """;


    private static final String PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS = """        
                The object `conversational_history` below represents past interactions between the user and you (the AI assistant).
                Each `history_entry` contains a pair of `prompt` and `response`.
                
                Use the information in `conversational_history` to make your responses more personalized and contextually aware, but do not mention the history or how you're using it in your response.
                
                Simply respond to the `user_main_prompt` based on your knowledge, relevant context, and any past interactions, without referencing the existence of past conversations.
                            
                `conversational_history`:
            """;


//    private final static Map<Long, List<History>> conversationalHistoryStorage = new HashMap<>();

    private final OllamaChatModel ollamaChatModel;

    private final HistoryRepo historyRepo;
    private final ConversationRepository conversationRepository;

    public ChatService(OllamaChatModel ollamaChatModel, HistoryRepo historyRepo,
                       ConversationRepository conversationRepository) {
        this.ollamaChatModel = ollamaChatModel;
        this.historyRepo = historyRepo;
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public History call(String userMessage, Long conversationId) {
        if (conversationId == null) {
            conversationId = 0L;
        }

        Optional<Conversation> conversationOp = conversationRepository.findById(conversationId);
        Conversation saved = null;

        if (!conversationOp.isPresent()) {
            Conversation conversation = new Conversation();

            conversation.setTitle(
                    userMessage.length() > 22
                            ? capitalizeFirstLetter(userMessage.substring(0, 22))
                            : capitalizeFirstLetter(userMessage)
            );

            saved = conversationRepository.save(conversation);
            conversationId = saved.getId();
        }

        List<History> historyList = historyRepo.findByConversationId(conversationId);

        var historyPrompt = new StringBuilder(PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS);
        for (History history : historyList) {
            historyPrompt.append(history.toString());
        }

        var contextSystemMessage = new SystemMessage(historyPrompt.toString());
        var generalSystemMessage = new SystemMessage(PROMPT_GENERAL_INSTRUCTIONS);

        var currentPromptMessage = new UserMessage(CURRENT_PROMPT_INSTRUCTIONS.concat(userMessage));

        Prompt prompt = new Prompt(List.of(contextSystemMessage, generalSystemMessage, currentPromptMessage));

        String response = ollamaChatModel.call(prompt).getResult().getOutput().getContent();
//        System.out.println("Message: " + userMessage);
//        System.out.println("Response: " + response);

        History history = new History(userMessage, response);
        history.setConversation(conversationOp.orElse(saved));

        historyRepo.save(history);

        return history;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAllOrder();
    }

    @Transactional
    public List<History> getHistory(Long conversationId) {
        return historyRepo.findByConversationId(conversationId);
    }

    @Transactional
    public Long deleteConversation(Long conversationId) {
        historyRepo.deleteByConversationId(conversationId);
        return conversationRepository.deleteByIdAllIgnoreCase(conversationId);
    }
}
