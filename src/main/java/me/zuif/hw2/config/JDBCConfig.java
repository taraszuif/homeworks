package me.zuif.hw2.config;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConfig {
    private static final String URL = "jdbc:postgresql://ec2-34-235-198-25.compute-1.amazonaws.com:5432/ddd88hlsi18tn7";
    private static final String USER = "ihgfwqtptjwyli";
    private static final String PASSWORD = "a03f2586d8b5550d68fd62f29728add992e6c44e15e528a5a1b51bd24de03eea";

    private JDBCConfig() {
    }

    @SneakyThrows
    public static Connection getConnection() {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}