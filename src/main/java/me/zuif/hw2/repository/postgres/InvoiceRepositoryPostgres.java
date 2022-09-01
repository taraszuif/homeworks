package me.zuif.hw2.repository.postgres;

import lombok.SneakyThrows;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.JDBCConfig;
import me.zuif.hw2.model.Invoice;
import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.ProductType;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.InvoiceRepository;
import org.apache.commons.lang3.EnumUtils;

import java.sql.Date;
import java.sql.*;
import java.time.LocalTime;
import java.util.*;

@Singleton
public class InvoiceRepositoryPostgres implements InvoiceRepository {

    private static final Connection CONNECTION = JDBCConfig.getConnection();
    private static InvoiceRepositoryPostgres instance;

    @Autowired
    public InvoiceRepositoryPostgres() {
    }

    public static InvoiceRepositoryPostgres getInstance() {
        if (instance == null) {
            instance = new InvoiceRepositoryPostgres();
        }
        return instance;
    }


    @Override
    public void save(Invoice invoice) {
        String insert = "INSERT INTO db.invoice (id, sum, time) VALUES (?, ?, ?);";
        String phone = "UPDATE db.phone SET invoice_id = ? WHERE id = ?;";
        String tea = "UPDATE db.tea SET invoice_id = ? WHERE id = ?;";
        String pen = "UPDATE db.pen SET invoice_id = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(insert)) {
            CONNECTION.setAutoCommit(false);

            preparedStatement.setString(1, invoice.getId());
            preparedStatement.setDouble(2, invoice.getSum());
            preparedStatement.setDate(3, Date.valueOf(invoice.getTime().toLocalDate()));
            preparedStatement.execute();
            List<Product> products = invoice.getProducts();
            products.forEach(product -> {
                if (product instanceof Phone) {
                    updateProduct(phone, invoice, product);
                }
                if (product instanceof Tea) {
                    updateProduct(tea, invoice, product);
                }
                if (product instanceof Pen) {
                    updateProduct(pen, invoice, product);
                }
            });

            CONNECTION.commit();
            CONNECTION.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }


    private void updateProduct(String query, Invoice invoice, Product product) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(query)) {
            statement.setString(1, invoice.getId());
            statement.setString(2, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @SneakyThrows
    public Product setFieldsToObject(final ResultSet resultSet, ProductType productType) {
        return switch (productType) {
            case PHONE -> {
                final String model = resultSet.getString("phone_model");
                int count = resultSet.getInt("phone_count");
                double price = resultSet.getDouble("phone_price");
                String title = resultSet.getString("phone_title");
                Manufacturer manufacturer = EnumUtils.getEnum(Manufacturer.class, resultSet.getString("phone_manufacturer"), Manufacturer.UNKNOWN);
                Phone phone = new Phone(title, count, price, model, manufacturer);
                phone.setId(resultSet.getString("phone_id"));
                yield phone;
            }
            case PEN -> {
                int count = resultSet.getInt("pen_count");
                double price = resultSet.getDouble("pen_price");
                String title = resultSet.getString("pen_title");
                PenType type = EnumUtils.getEnum(PenType.class, resultSet.getString("pen_type"), PenType.UNKNOWN);
                PenBrand brand = EnumUtils.getEnum(PenBrand.class, resultSet.getString("pen_brand"), PenBrand.UNKNOWN);
                PenColor color = EnumUtils.getEnum(PenColor.class, resultSet.getString("pen_color"), PenColor.UNKNOWN);
                Pen pen = new Pen(title, count, price, brand, type, color);
                pen.setId(resultSet.getString("pen_id"));
                yield pen;
            }
            case TEA -> {
                int count = resultSet.getInt("tea_count");
                double price = resultSet.getDouble("tea_price");
                String title = resultSet.getString("tea_title");
                TeaType type = EnumUtils.getEnum(TeaType.class, resultSet.getString("tea_type"), TeaType.UNKNOWN);
                TeaBrand brand = EnumUtils.getEnum(TeaBrand.class, resultSet.getString("tea_brand"), TeaBrand.UNKNOWN);
                Tea tea = new Tea(title, count, price, brand, type);
                tea.setId(resultSet.getString("tea_id"));
                yield tea;
            }
        };
    }


    @Override
    public Optional<Invoice> findById(String id) {
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
                pen.color AS pen_color,
                                
                FROM db.invoice
                                
                LEFT JOIN db.Phone ON phone.invoice_id = invoice.id
                LEFT JOIN db.Pen ON pen.invoice_id = invoice.id
                LEFT JOIN db.Tea ON tea.invoice_id = invoice.id
                                
                WHERE invoice.id = ? ORDER BY invoice.id;""";
        try (PreparedStatement statement = CONNECTION.prepareStatement(select)) {

            statement.setString(1, id);
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
                    Tea tea = (Tea) setFieldsToObject(resultSet, ProductType.TEA);
                    if (!products.contains(tea)) {
                        products.add(tea);
                    }
                }
                if (resultSet.getString("phone_id") != null) {
                    Phone phone = (Phone) setFieldsToObject(resultSet, ProductType.PHONE);
                    if (!products.contains(phone)) {
                        products.add(phone);
                    }
                }
                if (resultSet.getString("pen_id") != null) {
                    Pen pen = (Pen) setFieldsToObject(resultSet, ProductType.PEN);
                    if (!products.contains(pen)) {
                        products.add(pen);
                    }
                }
            }
            return id_invoice.values().stream().findFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                    Tea tea = (Tea) setFieldsToObject(resultSet, ProductType.TEA);
                    if (!products.contains(tea)) {
                        products.add(tea);
                    }
                }
                if (resultSet.getString("phone_id") != null) {
                    Phone phone = (Phone) setFieldsToObject(resultSet, ProductType.PHONE);
                    if (!products.contains(phone)) {
                        products.add(phone);
                    }
                }
                if (resultSet.getString("pen_id") != null) {
                    Pen pen = (Pen) setFieldsToObject(resultSet, ProductType.PEN);
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

    public Map<Double, Integer> sortBySum() {
        Map<Double, Integer> count_sum = new HashMap<>();
        String sortBySum = "SELECT count(id) AS count, invoice.sum FROM db.invoice GROUP BY invoice.sum;";

        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sortBySum);
            while (resultSet.next()) {
                double sum = resultSet.getDouble("sum");
                int count = resultSet.getInt("count");
                count_sum.put(sum, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count_sum;
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

    @Override
    public void update(Invoice invoice) {
        String update = "UPDATE db.invoice SET sum = ?, time = ? WHERE id = ?;";
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(update)) {
            preparedStatement.setDouble(1, invoice.getSum());
            preparedStatement.setDate(2, Date.valueOf(invoice.getTime().toLocalDate()));
            preparedStatement.setString(3, invoice.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
