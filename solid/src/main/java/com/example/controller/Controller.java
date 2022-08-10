package com.example.controller;

import com.example.model.Product;
import com.example.service.NotificationService;
import com.example.service.ProductFactory;
import com.example.service.ProductService;

import java.util.ArrayList;
import java.util.List;

public class Controller implements Runnable {

    private static final ProductService productService = ProductService.getInstance();
    private static final NotificationService notificationService = NotificationService.getInstance();
    private static final ProductFactory productFactory = ProductFactory.getInstance();

    @Override
    public void run() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(productFactory.generateRandomProduct());
        }
        list.forEach(productService::save);
        System.out.println("Products: " + productService.getAll());
        System.out.println("notifications sent: " + notificationService.filterNotifiableProductsAndSendNotifications());
    }

}
