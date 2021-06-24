package com.nuggets.valueeats.service.database;

import com.nuggets.valueeats.controllers.database.DatabaseController;

import java.sql.SQLException;

public class DatabaseAuthenticationTableService {
    public static void createNewTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS authentication " +
                "     (" +
                "          username text NOT NULL, " +
                "          email text NOT NULL, " + // TODO: Might be better to use an email type if it exists
                "          password text NOT NULL, " +
                "          PRIMARY KEY (username, email) " +
                "     );";

        DatabaseController.processWriteRequest(sql);
    }

    public static void addEntry(String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO authentication " +
                "     (username, email, password) " +
                "     VALUES " +
                "     ('%s', '%s', '%s');";

        DatabaseController.processWriteRequest(String.format(sql, username, email, password));
    }
}
