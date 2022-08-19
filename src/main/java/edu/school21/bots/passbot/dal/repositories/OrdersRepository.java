package edu.school21.bots.passbot.dal.repositories;

import edu.school21.bots.passbot.dal.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(String status);
}
