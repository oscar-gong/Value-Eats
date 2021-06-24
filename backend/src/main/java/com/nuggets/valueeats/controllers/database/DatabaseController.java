package com.nuggets.valueeats.controllers.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class DatabaseController {
    public final static String databaseName = "jdbc:sqlite:src/valueeats.db";
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Connection writeConnection = connect(); // Sqlite3 does not allow concurrent writes

    public static void createDatabase() throws SQLException {
        try {
            DriverManager.getConnection(databaseName);
            log.info("Successfully created database");
        } catch (SQLException e) {
            log.error("Could not create database", e);
            throw e;
        }
    }

    private static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseName);
            log.info("Successfully connected to database");
        } catch (SQLException e) {
            log.error("Could not connect to database", e);
        }

        return connection;
    }

    public static void processWriteRequest(String sql) throws SQLException {
        Statement statement;
        try {
            statement = writeConnection.createStatement();
        } catch (SQLException e) {
            log.error("Could not create statement");
            throw e;
        }

        try {
            lock.lock();
            statement.execute(sql);
        } catch (SQLException e) {
            log.error("Write Request Failed", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public static ResultSet processReadRequest(String sql) throws SQLException {
        try {
            Statement statement = writeConnection.createStatement();
            statement.execute(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            log.error("Could not create statement");
            throw e;
        }
    }
}
