package com.example.service;

import com.example.model.Product;
import com.example.repository.IProductRepository;

import java.util.List;

public abstract class AbstractProductService<T extends Product> {
    private final IProductRepository<T> repository;

    public AbstractProductService(IProductRepository<T> repository) {
        this.repository = repository;
    }

    public void save(T product) {
        repository.save(product);
    }

    public List<T> getAll() {
        return repository.getAll();
    }
}
