package edu.school21.passbot.service;

import edu.school21.passbot.models.User;
import edu.school21.passbot.repositories.UsersRepository;
import java.util.Optional;
import org.springframework.stereotype.Component;


@Component
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User createUser(
        Long chatId, String name, String surname, String patronymic
    ) {
        User user = new User();

        user.setChatId(chatId);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);

        user = usersRepository.save(user);
        return user;
    }

    public User getByChatId(Long chatId) {
        Optional<User> user = usersRepository.getUserByChatId(chatId);
        return user.orElse(null);
    }

    public User getByLogin(String login) {
        Optional<User> user = usersRepository.getUserByLogin(login);
        return user.orElse(null);
    }

    public void updateUser(User user) {
        usersRepository.saveAndFlush(user);
    }

    public void saveUser(User user) {
        usersRepository.save(user);
    }
}
