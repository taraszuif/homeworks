package me.zuif.module2.model.invoice;

import lombok.Getter;
import lombok.Setter;
import me.zuif.module2.model.Customer;
import me.zuif.module2.model.product.Product;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class Invoice {
    private List<Product> productList;
    private Customer customer;
    private InvoiceType type;
    private LocalDateTime created;

    public Invoice(List<Product> productList, Customer customer, InvoiceType type, LocalDateTime created) {
        this.productList = productList;
        this.customer = customer;
        this.type = type;
        this.created = created;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "productList=" + productList +
                ", customer=" + customer +
                ", type=" + type +
                ", created=" + created +
                '}';
    }
}
