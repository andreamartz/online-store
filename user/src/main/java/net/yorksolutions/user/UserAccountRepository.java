package net.yorksolutions.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsernameAndPassword(String username, String password);

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByUserType(String userType);
}
