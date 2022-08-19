package edu.school21.passbot.prototype.kernel.service;

import edu.school21.passbot.prototype.kernel.models.Order;
import edu.school21.passbot.prototype.kernel.models.User;
import edu.school21.passbot.prototype.dal.repositories.OrdersRepository;
import edu.school21.passbot.prototype.dal.repositories.UsersRepository;
import org.apache.shiro.session.InvalidSessionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        guest.setRole("GUEST");
        usersRepository.save(guest);

        User peer = usersRepository.getUserByChatId(chatId).orElse(null);
        if (peer == null)
            throw new InvalidSessionException("Session does not contain chatid");

        Order order = new Order();
        order.setGuest(guest);
        order.setPeer(peer);
        order.setStartTime(date.atTime(LocalTime.parse("10:00")));
        order.setEndTime(date.atTime(LocalTime.parse("21:00")));
        order.setStatus("На рассмотрении");

        order = ordersRepository.save(order);
        return order;
    }

    public List<Order> getAllActive() {
        return ordersRepository.findAllByStatus("На рассмотрении");
    }

}
