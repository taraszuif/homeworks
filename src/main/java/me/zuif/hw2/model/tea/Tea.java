package me.zuif.hw2.model.tea;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw2.model.Product;

@Getter
@Setter
public class Tea extends Product {
    private final TeaBrand brand;
    private final TeaType type;

    public Tea(String title, int count, double price, TeaBrand brand, TeaType type) {
        super(title, count, price);
        this.brand = brand;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Tea{" +
                "brand=" + brand +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
