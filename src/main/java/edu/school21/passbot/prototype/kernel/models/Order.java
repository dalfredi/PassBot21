package edu.school21.passbot.prototype.kernel.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "orders", schema = "bot")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="peer_id")
    @ToString.Exclude
    private User peer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="guest_id")
    @ToString.Exclude
    private User guest;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="admin_id")
    @ToString.Exclude
    private User admin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String toMarkdownPrettyString() {
        return String.format(
                "*Заявка №%d*\n" +
                        "Логин: %s\n" +
                        "ФИО пира: %s %s %s\n" +
                        "ФИО гостя: %s %s %s\n" +
                        "Дата посещения: %s\n" +
                        "Статус: %s\n\n",
                getId(),
                peer.getLogin(),
                peer.getSurname(), peer.getName(), peer.getPatronymic(),
                guest.getSurname(), guest.getName(), guest.getPatronymic(),
                getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                getStatus()
        );
    }
}