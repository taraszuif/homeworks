package me.zuif.hw2.model.tea;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Tea extends Product {
    @Column
    private TeaBrand brand;
    @Column
    private TeaType teaType;

    public Tea(String title, int count, double price, TeaBrand brand, TeaType teaType) {
        super(title, count, price, ProductType.TEA);
        this.brand = brand;
        this.teaType = teaType;
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
