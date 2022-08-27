package me.zuif.hw2.model.pen;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Pen extends Product {
    @Column
    private PenBrand brand;
    @Column
    private PenType penType;
    @Column
    private PenColor color;

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
