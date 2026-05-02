package com.presensi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Sesuaikan URL, USER, dan PASSWORD dengan konfigurasi database lokal Anda
    private static final String URL = "jdbc:mysql://localhost:3306/manajemen_presensi";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Kosongkan jika menggunakan XAMPP secara default

    /**
     * Mendapatkan koneksi ke database MySQL.
     * Menggunakan pola Singleton sederhana untuk mencegah pembukaan koneksi berlebihan.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Memastikan driver JDBC sudah dimuat
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan. Pastikan library MySQL Connector/J sudah ditambahkan!");
            e.printStackTrace();
            throw new SQLException("Database driver error", e);
        }
    }
}