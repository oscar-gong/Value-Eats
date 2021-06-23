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
        }

        return connection;
    }

    public static void createNewTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS warehousesfs (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        DatabaseManager.processWriteRequest(sql);
    }
}
