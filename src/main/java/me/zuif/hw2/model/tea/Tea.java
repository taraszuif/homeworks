package me.zuif.hw2.model.tea;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

@Getter
@Setter
public class Tea extends Product {
    private final TeaBrand brand;
    private final TeaType teaType;

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
