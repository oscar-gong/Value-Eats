package com.nuggets.valueeats.controllers.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class DatabaseManager {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Connection writeConnection = DatabaseConnector.connect(); // Sqlite3 does not allow concurrent writes

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
