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
import java.util.Optional;

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
                             String guestSurname,
                             String guestName,
                             String guestPatronymic,
                             LocalDate date) {
        User guest = new User();
        guest.setSurname(guestSurname);
        guest.setName(guestName);
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
        order.setCampus(peer.getCampus());

        order = ordersRepository.save(order);
        return order;
    }

    public List<Order> getAllActiveForCampus(String campus) {
        return ordersRepository.findAllByStatusAndCampus("На рассмотрении", campus);
    }

    public List<Order> getAllActive() {
        return ordersRepository.findAllByStatus("На рассмотрении");
    }

    public Order changeStatus(Long orderId, String newStatus) {
        Optional<Order> optional = ordersRepository.findById(orderId);
        Order order = null;
        if (optional.isPresent()) {
            order = optional.get();
            order.setStatus(newStatus);
            ordersRepository.saveAndFlush(order);
        }
        return order;
    }

    public List<Order> getAllByUserId(User user) {
        return ordersRepository.findAllByPeer(user);
    }
}
