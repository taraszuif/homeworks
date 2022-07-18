package me.zuif.hw2.service;

import me.zuif.hw2.model.Product;
import me.zuif.hw2.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class ProductService<T extends Product> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ProductRepository<T> repository;

    public ProductService(ProductRepository<T> repository) {
        this.repository = repository;
    }

    public void createAndSaveProducts(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<T> products = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final T product = createProduct();
            products.add(product);
            logger.info(product.getClass().getSimpleName() + " {} has been saved", product.getId());
        }
        repository.saveAll(products);
    }

    protected abstract T createProduct();

    public void save(T product) {
        if (product.getCount() == 0) {
            product.setCount(-1);
        }
        repository.save(product);
    }

    protected ProductRepository<T> getRepository() {
        return repository;
    }

    public void update(T product) {
        repository.update(product);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void printAll() {
        for (T product : repository.findAll()) {
            System.out.println(product);
        }
    }
}
