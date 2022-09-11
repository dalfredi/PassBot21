package edu.school21.passbot.repositories;


import edu.school21.passbot.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByChatId(Long chatId);

    Optional<User> getUserByLogin(String login);
}
