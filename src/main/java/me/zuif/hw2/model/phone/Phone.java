package me.zuif.hw2.model.phone;

import lombok.Getter;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Phone extends Product {
    private final String model;
    private final Manufacturer manufacturer;
    private List<String> details = new ArrayList<>();

    public Phone(String title, int count, double price, String model, Manufacturer manufacturer) {
        super(title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
    }

    public Phone(String title, int count, double price, String model, Manufacturer manufacturer, List<String> details) {
        super(title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
        this.details = details;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "manufacturer=" + manufacturer +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", model=" + model +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

}
