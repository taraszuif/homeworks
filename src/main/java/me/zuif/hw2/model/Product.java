package me.zuif.hw2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(schema = "hibernate_db")
public abstract class Product {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    protected String id;
    @Column
    protected ProductType type;
    @Column
    protected String title;
    @Column
    protected int count;
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    protected Invoice invoice;
    @Column
    protected double price;

    public Product(String title, int count, double price, ProductType type) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.count = count;
        this.price = price;
        this.type = type;
    }

}
