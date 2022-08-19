package edu.school21.passbot.prototype.dal.repositories;


import edu.school21.passbot.prototype.kernel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByChatId(Long chatId);
    Optional<User> getUserByLogin(String login);
}
