package edu.school21.passbot.prototype.dal.repositories;

import edu.school21.passbot.prototype.kernel.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatusAndCampus(String status, String campus);
}
