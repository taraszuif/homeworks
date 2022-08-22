package me.zuif.hw2.repository.postgres;

import lombok.SneakyThrows;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.JDBCConfig;
import me.zuif.hw2.model.phone.Manufacturer;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.repository.ProductRepository;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class PhoneRepositoryDB implements ProductRepository<Phone> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static PhoneRepositoryDB instance;

    @Autowired
    public PhoneRepositoryDB() {
    }

    public static PhoneRepositoryDB getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryDB();
        }
        return instance;
    }


    @Override
    public void save(Phone phone) {
        String sql = "INSERT INTO db.Phone (id, count, price, manufacturer, title, model) VALUES (?, ?, ?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, phone);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Phone> phones) {
        String sql = "INSERT INTO db.Phone (id, count, price, manufacturer, title, model) VALUES (?, ?, ?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            CONNECTION.setAutoCommit(false);
            for (Phone phone : phones) {
                setObjectFields(statement, phone);
                statement.addBatch();
            }
            statement.executeBatch();
            CONNECTION.commit();
            CONNECTION.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Phone> findAll() {
        final List<Phone> result = new ArrayList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM db.Phone");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @SneakyThrows
    private void setObjectFields(final PreparedStatement statement, final Phone phone) {
        statement.setString(1, phone.getId());
        statement.setInt(2, phone.getCount());
        statement.setDouble(3, phone.getPrice());
        statement.setString(4, phone.getManufacturer().name());
        statement.setString(5, phone.getTitle());
        statement.setString(6, phone.getModel());
    }

    @Override
    public boolean update(Phone phone) {
        String update = "UPDATE db.Phone SET count = ?, price = ?, manufacturer = ?, title = ?, model = ?  WHERE id = ?;";
        try (PreparedStatement statement = CONNECTION.prepareStatement(update)) {
            setObjectFields(statement, phone);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM db.Phone WHERE id = ?";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Phone setFieldsToObject(final ResultSet resultSet) {
        final String model = resultSet.getString("model");
        int count = resultSet.getInt("count");
        double price = resultSet.getDouble("price");
        String title = resultSet.getString("title");
        Manufacturer manufacturer = EnumUtils.getEnum(Manufacturer.class, resultSet.getString("manufacturer"), Manufacturer.UNKNOWN);
        Phone phone = new Phone(title, count, price, model, manufacturer);
        phone.setId(resultSet.getString("id"));
        return phone;
    }

    @Override
    public Optional<Phone> findById(String id) {
        String sql = "SELECT * FROM db.Phone WHERE id = ?";
        Optional<Phone> phone = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                phone = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return phone;
    }
}