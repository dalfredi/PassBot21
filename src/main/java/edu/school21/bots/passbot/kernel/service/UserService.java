package edu.school21.bots.passbot.kernel.service;

import edu.school21.bots.passbot.dal.models.User;
import edu.school21.bots.passbot.dal.repositories.UsersRepository;
import org.springframework.stereotype.Component;


//@RequiredArgsConstructor
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
}
