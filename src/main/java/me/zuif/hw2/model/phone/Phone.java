package me.zuif.hw2.model.phone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Phone extends Product {
    @Column
    private Manufacturer manufacturer;
    @Column
    private String model;
    @Transient
    private LocalDateTime creatingDate;
    @Transient
    private String currency;
    @Transient
    private OperationSystem operationSystem;
    @Transient
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

    public Phone(String title, int count, double price, String model, Manufacturer manufacturer, LocalDateTime creatingDate, String currency, OperationSystem operationSystem) {
        super(title, count, price, ProductType.PHONE);
        this.model = model;
        this.manufacturer = manufacturer;
        this.creatingDate = creatingDate;
        this.currency = currency;
        this.operationSystem = operationSystem;
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
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", example='" + model + '\'' +
                ", manufacturer=" + manufacturer +
                ", creatingDate=" + creatingDate +
                ", currency='" + currency + '\'' +
                ", operationSystem=" + operationSystem +
                ", details=" + details +
                '}';

    }

    public static class Builder {
        private final Phone newPhone;

        public Builder(double price, Manufacturer manufacturer) {
            if (manufacturer == null) {
                newPhone = new Phone("N/A", 0, price, "N/A", Manufacturer.UNKNOWN);
            } else {
                newPhone = new Phone("N/A", 0, price, "N/A", manufacturer);
            }
        }

        public Builder withTitle(String title) {
            if (title.length() > 20) {
                throw new IllegalArgumentException("Title cannot be longer than 20 characters");
            }
            newPhone.setTitle(title);
            return this;
        }

        public Builder withCount(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("Count must be greater than 0");
            }
            newPhone.setCount(count);
            return this;
        }

        public Builder withModel(String model) {
            if (model.length() > 20) {
                throw new IllegalArgumentException("Model cannot be longer than 20 characters");
            }
            newPhone.setModel(model);
            return this;
        }

        public Phone build() {
            return newPhone;
        }


    }
}
