package me.zuif.hw2.repository;

import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;

import java.sql.ResultSet;
import java.util.Optional;

public interface InvoiceRepository {

    void save(Invoice invoice);


    Optional<Invoice> findById(String id);

    void update(Invoice invoice);

    Product setFieldsToObject(ResultSet resultSet, ProductType type);
}
