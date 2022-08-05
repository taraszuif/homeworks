package com.example.service;

import com.example.model.NotifiableProduct;
import com.example.repository.IProductRepository;
import com.example.repository.ProductRepository;

public class NotificationService extends AbstractProductService {
    private static NotificationService instance;

    public NotificationService(IProductRepository repository) {
        super(repository);
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService((ProductRepository.getInstance()));
        }
        return instance;
    }

    public int filterNotifiableProductsAndSendNotifications() {
        return getAll().stream().filter(NotifiableProduct.class::isInstance).map(NotifiableProduct.class::cast)
                .toList().size();
    }
}
