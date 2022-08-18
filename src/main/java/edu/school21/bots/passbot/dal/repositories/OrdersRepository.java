package edu.school21.bots.passbot.dal.repositories;

import edu.school21.bots.passbot.dal.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {
}
