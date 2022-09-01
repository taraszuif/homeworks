package me.zuif.hw2.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {
    private static final String URL = "jdbc:postgresql://ec2-34-235-198-25.compute-1.amazonaws.com:5432/ddd88hlsi18tn7";
    private static final String USER = "ihgfwqtptjwyli";
    private static final String PASSWORD = "a03f2586d8b5550d68fd62f29728add992e6c44e15e528a5a1b51bd24de03eea";
    private static final String SCHEMA = "hibernate_db";
    private static final String LOCATION = "db/migration";

    public static Flyway getInstance() {
        return Flyway.configure()
                .dataSource(URL, USER, PASSWORD)
                .baselineOnMigrate(true)
                .schemas(SCHEMA)
                .locations(LOCATION)
                .load();
    }
}
