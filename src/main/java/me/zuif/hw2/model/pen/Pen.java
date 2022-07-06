package me.zuif.hw2.model.pen;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw2.model.Product;

@Getter
@Setter
public class Pen extends Product {
    private final PenBrand brand;
    private final PenType type;
    private final PenColor color;

    public Pen(String title, int count, double price, PenBrand brand, PenType type, PenColor color) {
        super(title, count, price);
        this.brand = brand;
        this.type = type;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Pen{" +
                "brand=" + brand +
                ", type=" + type +
                ", color=" + color +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
