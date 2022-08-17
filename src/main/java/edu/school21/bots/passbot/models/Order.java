package edu.school21.bots.passbot.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@EqualsAndHashCode
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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