package dev.scgursel.llama2.Repo;

import dev.scgursel.llama2.Model.Conversation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends ListCrudRepository<Conversation, Long> {

    Optional<Conversation> findById(Long id);

    @Query("SELECT c FROM Conversation c ORDER BY c.id DESC")
    List<Conversation> findAllOrder();

    long deleteByIdAllIgnoreCase(Long id);
}