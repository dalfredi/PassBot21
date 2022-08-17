package edu.school21.bots.passbot.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;
    private String name;
    private String surname;
    private String patronymic;
    private String role;
    @ToString.Exclude
    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
    private List<Order> orders;
}
