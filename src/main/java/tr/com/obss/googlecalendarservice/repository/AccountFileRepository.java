package tr.com.obss.googlecalendarservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.com.obss.googlecalendarservice.entity.GoogleAccount;

import java.util.Optional;

@Repository
public interface AccountFileRepository extends JpaRepository<GoogleAccount, String> {
    Optional<GoogleAccount> findAccountFileByAccountMail(String accountMail);
}