package com.example.service;

import com.example.model.NotifiableProduct;
import com.example.model.Product;
import com.example.model.ProductBundle;

import java.util.Random;

public class ProductFactory {
    private static final Random RANDOM = new Random();
    private static ProductFactory instance;

    private ProductFactory() {
    }

    public static ProductFactory getInstance() {
        if (instance == null) {
            instance = new ProductFactory();
        }

        return instance;
    }

    public Product generateRandomProduct() {
        if (RANDOM.nextBoolean()) {
            ProductBundle productBundle = new ProductBundle();
            productBundle.setAmount(RANDOM.nextInt(15));
            productBundle.setAvailable(RANDOM.nextBoolean());
            productBundle.setPrice(RANDOM.nextDouble());
            productBundle.setId(RANDOM.nextLong());
            productBundle.setTitle(RANDOM.nextFloat() + "" + RANDOM.nextDouble());
            return productBundle;
        } else {
            NotifiableProduct product = new NotifiableProduct();
            product.setId(RANDOM.nextLong());
            product.setTitle(RANDOM.nextFloat() + "" + RANDOM.nextDouble());
            product.setAvailable(RANDOM.nextBoolean());
            product.setChannel(RANDOM.nextBoolean() + "" + RANDOM.nextDouble());
            product.setPrice(RANDOM.nextDouble());
            return product;
        }
    }
}
