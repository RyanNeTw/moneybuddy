package moneybuddy.fr.moneybuddy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import moneybuddy.fr.moneybuddy.model.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
 
     List<Task> findBySubaccountIdChild(String id);
     List<Task> findBySubaccountIdParent(String id);
     List<Task> findByAccountId(String id);

    Optional<Task> findByIdAndSubaccountIdParent(String id, String subAccountId);
}
