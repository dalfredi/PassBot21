package edu.school21.passbot.repositories;

import edu.school21.passbot.models.Order;
import edu.school21.passbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusAndCampus(String status, String campus);
    List<Order> findAllByPeer(User user);
    List<Order> findAllByStatus(String status);
}
