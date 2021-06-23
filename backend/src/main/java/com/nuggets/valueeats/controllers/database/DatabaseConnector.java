package com.nuggets.valueeats.controllers.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public final class DatabaseConnector {
    public static void createDatabase() throws SQLException {
        try {
            DriverManager.getConnection(DatabaseConstants.databaseName);
            log.info("Successfully created database");
        } catch (SQLException e) {
            log.error("Could not create database", e);
            throw e;
        }
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseConstants.databaseName);
            log.info("Successfully connected to database");
        } catch (SQLException e) {
            log.error("Could not connect to database", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Failed to close opened database connection", e);
            }
        }

        return connection;
    }
}
