package dev.scgursel.llama2.Repo;


import dev.scgursel.llama2.Model.History;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepo extends ListCrudRepository<History, Long> {

    public List<History> findByConversationId(Long conversationId);
    void deleteByConversationId(Long conversationId);

}
