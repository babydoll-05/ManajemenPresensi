package com.rplbo.app.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_PATH = System.getProperty("user.dir") + "/manajemenpresensi.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        conn.createStatement().execute("PRAGMA foreign_keys = ON");
        return conn;
    }

    public static void initDatabase() {
        System.out.println("DB path: " + DB_PATH);
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS jabatan (
                    id_jabatan   INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama_jabatan VARCHAR(100) NOT NULL,
                    deskripsi    TEXT
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS departemen (
                    id_departemen     INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama_departemen   VARCHAR(100) NOT NULL,
                    kepala_departemen VARCHAR(100)
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS karyawan (
                    id_karyawan   INTEGER PRIMARY KEY AUTOINCREMENT,
                    nama          VARCHAR(100) NOT NULL,
                    nik           VARCHAR(20)  NOT NULL UNIQUE,
                    id_jabatan    INTEGER NOT NULL,
                    id_departemen INTEGER NOT NULL,
                    email         VARCHAR(100),
                    no_telepon    VARCHAR(15),
                    tanggal_masuk DATE,
                    status        TEXT NOT NULL CHECK(status IN ('aktif','nonaktif')),
                    FOREIGN KEY (id_jabatan)    REFERENCES jabatan(id_jabatan),
                    FOREIGN KEY (id_departemen) REFERENCES departemen(id_departemen)
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS pengguna (
                    id_pengguna INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_karyawan INTEGER NOT NULL UNIQUE,
                    username    VARCHAR(50)  NOT NULL UNIQUE,
                    password    VARCHAR(255) NOT NULL,
                    role        TEXT NOT NULL CHECK(role IN ('admin','karyawan')),
                    FOREIGN KEY (id_karyawan) REFERENCES karyawan(id_karyawan)
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS presensi (
                    id_presensi      INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_karyawan      INTEGER NOT NULL,
                    tanggal          DATE    NOT NULL,
                    jam_masuk        TIME,
                    jam_keluar       TIME,
                    status_kehadiran TEXT NOT NULL CHECK(status_kehadiran IN ('hadir','izin','sakit','alpha')),
                    status_waktu     TEXT CHECK(status_waktu IN ('tepat_waktu','terlambat','pulang_cepat')),
                    keterangan       TEXT,
                    edited_by        INTEGER,
                    edited_at        DATETIME,
                    FOREIGN KEY (id_karyawan) REFERENCES karyawan(id_karyawan),
                    FOREIGN KEY (edited_by)   REFERENCES pengguna(id_pengguna)
                )""");

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS izin_cuti (
                    id_izin            INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_karyawan        INTEGER NOT NULL,
                    jenis_izin         TEXT NOT NULL CHECK(jenis_izin IN ('sakit','cuti','kepentingan lain')),
                    tanggal_mulai      DATE NOT NULL,
                    tanggal_selesai    DATE NOT NULL,
                    alasan             TEXT,
                    status_persetujuan TEXT NOT NULL DEFAULT 'pending'
                                       CHECK(status_persetujuan IN ('pending','disetujui','ditolak')),
                    FOREIGN KEY (id_karyawan) REFERENCES karyawan(id_karyawan)
                )""");

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
