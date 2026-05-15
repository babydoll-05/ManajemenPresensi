package com.rplbo.app.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // DATABASE
    private static final String URL =
            "jdbc:mysql://localhost:3306/manajemen_presensi";

    // USER MYSQL
    private static final String USER = "root";

    // PASSWORD MYSQL
    private static final String PASSWORD = "";

    /**
     * METHOD KONEKSI DATABASE
     */
    public static Connection getConnection()
            throws SQLException {

        try {

            // LOAD DRIVER MYSQL
            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

            // RETURN KONEKSI
            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (ClassNotFoundException e) {

            System.out.println(
                    "Driver MySQL tidak ditemukan!"
            );

            e.printStackTrace();

            throw new SQLException(
                    "Database Driver Error",
                    e
            );
        }
    }
}