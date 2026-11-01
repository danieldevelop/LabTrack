package com.labtrack.config;

import com.labtrack.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DRIVER = "org.postgresql.Driver";

    private static final String HOST = PropertiesUtil.get("postgresql.db.host");
    private static final String PORT = PropertiesUtil.get("postgresql.db.port");
    private static final String DBNAME = PropertiesUtil.get("postgresql.db.name");

    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DBNAME;

    private static final String USERNAME = PropertiesUtil.get("postgresql.db.username");
    private static final String PASSWORD = PropertiesUtil.get("postgresql.db.password");

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }

    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
