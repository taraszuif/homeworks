package me.zuif.hw2.service;

import me.zuif.hw2.model.ProductType;

import java.util.Random;

public class ProductFactory {
    private static final Random RANDOM = new Random();

    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final TeaService TEA_SERVICE = TeaService.getInstance();
    private static final PenService PEN_SERVICE = PenService.getInstance();

    private ProductFactory() {
    }

    public static void createAndSave(ProductType type) {
        switch (type) {
            case PHONE -> PHONE_SERVICE.createAndSaveProducts(1);
            case PEN -> PEN_SERVICE.createAndSaveProducts(1);
            case TEA -> TEA_SERVICE.createAndSaveProducts(1);
            default -> throw new IllegalArgumentException("Unknown Product type: " + type);
        }
    }

}
