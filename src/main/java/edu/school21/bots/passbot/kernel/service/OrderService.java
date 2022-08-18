package edu.school21.bots.passbot.kernel.service;

import edu.school21.bots.passbot.dal.models.Order;
import edu.school21.bots.passbot.dal.models.User;
import edu.school21.bots.passbot.dal.repositories.OrdersRepository;
import edu.school21.bots.passbot.dal.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;
    public OrderService(OrdersRepository ordersRepository, UsersRepository usersRepository) {
        this.ordersRepository = ordersRepository;
        this.usersRepository = usersRepository;
    }

//    TODO fetch actual start and end time from some config
    public Order createOrder(Long chatId,
                      String guestName,
                      String guestSurname,
                      String guestPatronymic,
                      LocalDate date) {
        User guest = new User();
        guest.setName(guestName);
        guest.setSurname(guestSurname);
        guest.setPatronymic(guestPatronymic);
        usersRepository.save(guest);

        User peer = usersRepository.getUserByChatId(chatId).get();

        Order order = new Order();
        order.setGuest(guest);
        order.setPeer(peer);
        order.setStartTime(date.atTime(LocalTime.parse("10:00")));
        order.setEndTime(date.atTime(LocalTime.parse("21:00")));
        order.setStatus("На рассмотрении");

        order = ordersRepository.save(order);
        return order;
    }
}
