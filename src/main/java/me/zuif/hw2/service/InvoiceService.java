package me.zuif.hw2.service;

import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.JDBCConfig;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.repository.InvoiceRepository;
import me.zuif.hw2.repository.postgres.InvoiceRepositoryDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class InvoiceService {
    private static final Connection CONNECTION = JDBCConfig.getConnection();
    private static InvoiceService instance;
    private final InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService(InvoiceRepository repository) {
        this.invoiceRepository = repository;
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService(InvoiceRepositoryDB.getInstance());
        }
        return instance;
    }


    public List<Invoice> findAllGreaterSumInvoices(double sum) {
        String select = """
                   SELECT db.invoice.*,
                                
                phone.id AS phone_id,
                phone.title AS phone_title,
                phone.count AS phone_count,
                phone.price AS phone_price,
                phone.manufacturer AS phone_manufacturer,
                phone.model AS phone_model,
                                
                tea.id AS tea_id,
                tea.title AS tea_title,
                tea.price AS tea_price,
                tea.count AS tea_count,
                tea.brand AS tea_brand,
                tea.type AS tea_type,
                                
                pen.id AS pen_id,
                pen.title AS pen_title,
                pen.price AS pen_price,
                pen.count AS pen_count,
                pen.brand AS pen_brand,
                pen.type AS pen_type,
                pen.color AS pen_color
                                
                FROM db.invoice
                                
                LEFT JOIN db.Phone ON phone.invoice_id = invoice.id
                LEFT JOIN db.Pen ON pen.invoice_id = invoice.id
                LEFT JOIN db.Tea ON tea.invoice_id = invoice.id
                WHERE sum > ? ORDER BY invoice.id;""";
        try (PreparedStatement statement = CONNECTION.prepareStatement(select)) {
            statement.setDouble(1, sum);
            ResultSet resultSet = statement.executeQuery();
            Map<String, Invoice> id_invoice = new HashMap<>();
            while (resultSet.next()) {
                String id1 = resultSet.getString("id");
                if (!id_invoice.containsKey(id1)) {
                    Invoice invoice = new Invoice();
                    invoice.setId(id1);
                    invoice.setTime(resultSet.getDate("time").toLocalDate().atTime(LocalTime.of(0, 0)));
                    invoice.setSum(resultSet.getDouble("sum"));
                    invoice.setProducts(new ArrayList<>());
                    id_invoice.put(invoice.getId(), invoice);
                }
                List<Product> products = id_invoice.get(id1).getProducts();
                if (resultSet.getString("tea_id") != null) {
                    Tea tea = (Tea) invoiceRepository.setFieldsToObject(resultSet, ProductType.TEA);
                    if (!products.contains(tea)) {
                        products.add(tea);
                    }
                }
                if (resultSet.getString("phone_id") != null) {
                    Phone phone = (Phone) invoiceRepository.setFieldsToObject(resultSet, ProductType.PHONE);
                    if (!products.contains(phone)) {
                        products.add(phone);
                    }
                }
                if (resultSet.getString("pen_id") != null) {
                    Pen pen = (Pen) invoiceRepository.setFieldsToObject(resultSet, ProductType.PEN);
                    if (!products.contains(pen)) {
                        products.add(pen);
                    }
                }
            }

            return id_invoice.values().stream().toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInvoiceCount() {
        String count = "SELECT count(id) AS count FROM db.invoice";

        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(count);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createFromProducts(List<Product> invoiceProducts) {
        Invoice invoice = new Invoice();

        invoice.setTime(LocalDateTime.now());
        invoice.setSum(invoiceProducts.stream().mapToDouble(Product::getPrice).sum());
        invoice.setProducts(new ArrayList<>(invoiceProducts));

        invoiceRepository.save(invoice);
    }

    public void updateDate(LocalDateTime newDate, String id) {
        invoiceRepository.findById(id).ifPresentOrElse(invoice -> {
            invoice.setTime(newDate);
            invoiceRepository.update(invoice);
        }, () -> {
            throw new IllegalArgumentException("Unable to update date, invoice with id=" + id + " does not exist!");
        });
    }

    public Map<Integer, Double> sortBySum() {
        Map<Integer, Double> count_sum = new HashMap<>();
        String sortBySum = "SELECT count(id) AS count, invoice.sum FROM db.invoice GROUP BY invoice.sum;";

        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sortBySum);
            while (resultSet.next()) {
                double sum = resultSet.getDouble("sum");
                int count = resultSet.getInt("count");
                count_sum.put(count, sum);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count_sum;
    }

}
