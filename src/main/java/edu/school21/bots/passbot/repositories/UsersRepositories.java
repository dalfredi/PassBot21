package edu.school21.bots.passbot.repositories;


import edu.school21.bots.passbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepositories extends JpaRepository<User, Long> {
//    List<Account> findAllAccountByCustomer_CustomerId(Long customerId);

    User findByUserId(Long userId);

}
