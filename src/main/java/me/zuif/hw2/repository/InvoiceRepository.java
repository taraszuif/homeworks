package me.zuif.hw2.repository;

import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.*;

public interface InvoiceRepository {

    void save(Invoice invoice);


    Optional<Invoice> findById(String id);

    void update(Invoice invoice);
    public List<Invoice> findAllGreaterSumInvoices(double sum);
    public int getInvoiceCount();
    public Map<Double, Integer > sortBySum();
}
