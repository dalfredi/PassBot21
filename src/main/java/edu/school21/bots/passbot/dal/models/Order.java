package edu.school21.bots.passbot.dal.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="peer_id")
    private User peer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="guest_id")
    private User guest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="admin_id")
    private User admin;
}
