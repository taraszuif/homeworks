package com.example.repository;

import com.example.model.Product;

import java.util.List;

public interface IProductRepository<T extends Product> {
    T save(T product);

    List<T> getAll();
}
