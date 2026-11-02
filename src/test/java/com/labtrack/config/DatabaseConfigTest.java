package com.labtrack.config;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConfigTest {

    @Test
    void shouldConnectToPostgreSQL() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            assertNotNull(conn, "Connection must not be null");
            assertTrue(conn.isValid(2), "Connection must be valid");

            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Connected to: " + meta.getDatabaseProductName()
                    + " " + meta.getDatabaseProductVersion());
            System.out.println("URL: " + meta.getURL());
            System.out.println("User: " + meta.getUserName());
        }
    }
}