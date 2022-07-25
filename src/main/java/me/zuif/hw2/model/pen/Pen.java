package me.zuif.hw2.model.pen;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

@Getter
@Setter
public class Pen extends Product {
    private final PenBrand brand;
    private final PenType penType;
    private final PenColor color;

    public Pen(String title, int count, double price, PenBrand brand, PenType penType, PenColor color) {
        super(title, count, price, ProductType.PEN);
        this.brand = brand;
        this.penType = penType;
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
