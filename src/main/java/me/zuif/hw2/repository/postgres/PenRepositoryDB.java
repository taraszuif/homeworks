package me.zuif.hw2.repository.postgres;

import lombok.SneakyThrows;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.JDBCConfig;
import me.zuif.hw2.model.pen.Pen;
import me.zuif.hw2.model.pen.PenBrand;
import me.zuif.hw2.model.pen.PenColor;
import me.zuif.hw2.model.pen.PenType;
import me.zuif.hw2.repository.ProductRepository;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class PenRepositoryDB implements ProductRepository<Pen> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static PenRepositoryDB instance;

    @Autowired
    public PenRepositoryDB() {

    }

    public static PenRepositoryDB getInstance() {
        if (instance == null) {
            instance = new PenRepositoryDB();
        }
        return instance;
    }


    @Override
    public void save(Pen pen) {
        String sql = "INSERT INTO db.Pen (id, count, price, title, brand, type, color) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, pen);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Pen> pens) {
        String sql = "INSERT INTO \"db\".Pen (id, count, price, title, brand, type, color) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            CONNECTION.setAutoCommit(false);
            for (Pen pen : pens) {
                setObjectFields(statement, pen);
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
    public List<Pen> findAll() {
        final List<Pen> result = new ArrayList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"db\".Pen");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @SneakyThrows
    private void setObjectFields(final PreparedStatement statement, final Pen pen) {
        statement.setString(1, pen.getId());
        statement.setInt(2, pen.getCount());
        statement.setDouble(3, pen.getPrice());
        statement.setString(4, pen.getTitle());
        statement.setString(5, pen.getBrand().toString());
        statement.setString(6, pen.getPenType().toString());
        statement.setString(7, pen.getColor().toString());
    }

    @Override
    public boolean update(Pen pen) {
        String update = "UPDATE db.Pen SET count = ?, price = ?, title = ?, brand = ?, type = ?, color = ?, WHERE id = ?";
        try (PreparedStatement statement = CONNECTION.prepareStatement(update)) {
            setObjectFields(statement, pen);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM \"db\".Pen WHERE id = ?";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Pen setFieldsToObject(final ResultSet resultSet) {
        int count = resultSet.getInt("count");
        double price = resultSet.getDouble("price");
        String title = resultSet.getString("title");
        PenType type = EnumUtils.getEnum(PenType.class, resultSet.getString("type"), PenType.UNKNOWN);
        PenBrand brand = EnumUtils.getEnum(PenBrand.class, resultSet.getString("brand"), PenBrand.UNKNOWN);
        PenColor color = EnumUtils.getEnum(PenColor.class, resultSet.getString("color"), PenColor.UNKNOWN);
        Pen pen = new Pen(title, count, price, brand, type, color);
        pen.setId(resultSet.getString("id"));
        return pen;
    }

    @Override
    public Optional<Pen> findById(String id) {
        String sql = "SELECT * FROM \"db\".Pen WHERE id = ?";
        Optional<Pen> pen = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                pen = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pen;
    }
}