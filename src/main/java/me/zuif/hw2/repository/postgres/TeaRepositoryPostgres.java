package me.zuif.hw2.repository.postgres;

import lombok.SneakyThrows;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.config.JDBCConfig;
import me.zuif.hw2.model.tea.Tea;
import me.zuif.hw2.model.tea.TeaBrand;
import me.zuif.hw2.model.tea.TeaType;
import me.zuif.hw2.repository.ProductRepository;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TeaRepositoryPostgres implements ProductRepository<Tea> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static TeaRepositoryPostgres instance;

    @Autowired
    public TeaRepositoryPostgres() {

    }

    public static TeaRepositoryPostgres getInstance() {
        if (instance == null) {
            instance = new TeaRepositoryPostgres();
        }
        return instance;
    }


    @Override
    public void save(Tea tea) {
        String sql = "INSERT INTO \"db\".Tea (id, count, price, title, brand, type) VALUES (?, ?,  ?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, tea);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Tea> teas) {
        String sql = "INSERT INTO \"db\".Tea (id, count, price, title, brand, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            CONNECTION.setAutoCommit(false);
            for (Tea tea : teas) {
                setObjectFields(statement, tea);
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
    public List<Tea> findAll() {
        final List<Tea> result = new ArrayList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"db\".Tea");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @SneakyThrows
    private void setObjectFields(final PreparedStatement statement, final Tea tea) {
        statement.setString(1, tea.getId());
        statement.setInt(2, tea.getCount());
        statement.setDouble(3, tea.getPrice());
        statement.setString(4, tea.getTitle());
        statement.setString(5, tea.getBrand().toString());
        statement.setString(6, tea.getTeaType().toString());
    }

    @Override
    public boolean update(Tea tea) {
        String update = "UPDATE \"db\".Tea SET count = ?, price = ?, title = ?, brand = ?, type = ?  WHERE id = ?;";
        try (PreparedStatement statement = CONNECTION.prepareStatement(update)) {
            setObjectFields(statement, tea);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM \"db\".Tea WHERE id = ?";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private Tea setFieldsToObject(final ResultSet resultSet) {
        int count = resultSet.getInt("count");
        double price = resultSet.getDouble("price");
        String title = resultSet.getString("title");
        TeaType type = EnumUtils.getEnum(TeaType.class, resultSet.getString("type"), TeaType.UNKNOWN);
        TeaBrand brand = EnumUtils.getEnum(TeaBrand.class, resultSet.getString("brand"), TeaBrand.UNKNOWN);
        Tea tea = new Tea(title, count, price, brand, type);
        tea.setId(resultSet.getString("id"));
        return tea;
    }

    @Override
    public Optional<Tea> findById(String id) {
        String sql = "SELECT * FROM \"db\".Tea WHERE id = ?";
        Optional<Tea> tea = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tea = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tea;
    }

}