package edu.school21.bots.passbot.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long orderId;
    private String number;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;
    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "peer_id")
    @MapsId
    private User peer;
    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "guest_id")
    @MapsId
    private User guest;
    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "admin_id")
    @MapsId
    private User admin;
}