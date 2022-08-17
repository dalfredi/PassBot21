package edu.school21.bots.passbot.service;

import edu.school21.bots.passbot.models.User;
import edu.school21.bots.passbot.repositories.UsersRepositories;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private  final UsersRepositories usersRepositories;

    public User createUser(String name, String surname, String patronymic) {

        User user = new User();

        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        usersRepositories.save(user);

        return user;
    }

}
