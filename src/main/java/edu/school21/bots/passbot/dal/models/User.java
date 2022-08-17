package edu.school21.bots.passbot.dal.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String role;
    @ToString.Exclude
    @OneToMany(
            mappedBy = "peer_id",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Order> orders;
}
