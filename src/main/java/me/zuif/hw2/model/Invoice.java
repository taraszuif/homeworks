package me.zuif.hw2.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class Invoice {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column
    private double sum;
    @OneToMany(mappedBy = "invoice",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER)
    private List<Product> products;
    @Column
    private LocalDateTime time;

    public Invoice() {
        id = UUID.randomUUID().toString();
    }
}