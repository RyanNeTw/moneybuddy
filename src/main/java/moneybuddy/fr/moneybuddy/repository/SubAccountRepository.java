package moneybuddy.fr.moneybuddy.repository;

import moneybuddy.fr.moneybuddy.model.SubAccount;
import moneybuddy.fr.moneybuddy.model.SubAccountRole;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubAccountRepository extends MongoRepository<SubAccount, String> {

    List<SubAccount> findByUserId(String userId);

    List<SubAccount> findByIsActive(boolean isActive);

    List<SubAccount> findByRole(SubAccountRole role);

    void deleteAllByUserId(String userId);
}