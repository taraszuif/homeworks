package me.zuif.hw2.repository;

import me.zuif.hw2.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository<T extends Product> {

    void save(T product);

    void saveAll(List<T> products);

    List<T> findAll();

    Optional<T> findById(String id);

    boolean update(T product);

    boolean delete(String id);

}

