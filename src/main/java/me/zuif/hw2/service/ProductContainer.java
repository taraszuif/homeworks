package me.zuif.hw2.service;

import me.zuif.hw2.model.Product;

import java.util.Random;

public class ProductContainer<T extends Product> {

    private static final Random RANDOM = new Random();
    private T product;

    public T getProduct() {
        return product;
    }

    public void setProduct(T product) {
        this.product = product;
    }

    public void applyRandomDiscount() {
        int discount = RANDOM.nextInt(10, 31);
        double priceWithDiscount = (product.getPrice() - (product.getPrice() * (discount / 100)));
        product.setPrice(priceWithDiscount);
    }

    public <X extends Number> void increaseCountOfProduct(X increaseCount) {
        product.setCount(product.getCount() + increaseCount.intValue());
    }
}
