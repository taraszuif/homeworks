package me.zuif.module2.model.product;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public abstract class Product {
    protected final String id;
    protected final ProductType type;
    protected double price;

    public Product(ProductType type, double price) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.price = price;
    }
}
