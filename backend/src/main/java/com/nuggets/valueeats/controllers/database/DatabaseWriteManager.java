package com.nuggets.valueeats.controllers.database;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public final class DatabaseWriteManager {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Connection writeConnection = DatabaseConnector.connect(); // Sqlite3 only allows one write

    public static void processWriteRequest() {
        lock.lock();
        // TODO: Write to database
        lock.unlock();
    }
}
