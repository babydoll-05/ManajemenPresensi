package com.rplbo.app.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // Elemen UI yang dihubungkan dengan fx:id di FXML
    @FXML private Label lblJam;
    @FXML private Label lblTanggal;
    @FXML private Label lblNamaProfil;
    @FXML private Label lblStatusDetail;
    @FXML private Label lblHadir;
    @FXML private Label lblTerlambat;
    @FXML private Label lblCuti;
    @FXML private Label lblIzin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClock();     // Menjalankan jam real-time
        loadUserData();  // Inisialisasi data tampilan awal
    }

    /**
     * Membuat jam digital yang berdetak setiap detik menggunakan Timeline.
     */
    private void initClock() {
        Locale localeID = new Locale("id", "ID");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeID);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            lblJam.setText(now.format(timeFormatter));
            lblTanggal.setText(now.format(dateFormatter));
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    /**
     * Mengatur tampilan awal dashboard menjadi kosong sebelum ada data dari database[cite: 1].
     */
    private void loadUserData() {
        lblStatusDetail.setText("Belum Absen Hari Ini. Silakan Clock-In.");
        lblHadir.setText("0 hari");
        lblTerlambat.setText("0 kali");
        lblCuti.setText("0 hari");
        lblIzin.setText("0");
    }

    /**
     * Method untuk menerima nama dari AuthController setelah login berhasil[cite: 1].
     */
    public void setNamaPengguna(String username) {
        lblNamaProfil.setText(username);
    }

    // ========================================================
    // LOGIKA NAVIGASI (PINDAH HALAMAN)[cite: 1]
    // ========================================================

    @FXML
    private void handleMenuBeranda(ActionEvent event) {
        System.out.println("Anda sudah berada di Beranda.");
    }

    @FXML
    private void handleMenuPresensi(ActionEvent event) {
        try {
            // Memuat halaman Presensi (Clock-In/Out)[cite: 1]
            FXMLLoader loader = new FXMLLoader(getClass().getResource("presensi-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Manajemen Presensi - Presensi");
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal memuat halaman presensi: " + e.getMessage());
        }
    }

    @FXML
    private void handleMenuRiwayat(ActionEvent event) {
        // TODO: Implementasi pindah ke halaman Riwayat[cite: 1]
        System.out.println("Navigasi ke Riwayat...");
    }

    @FXML
    private void handleMenuCuti(ActionEvent event) {
        // TODO: Implementasi pindah ke halaman Cuti[cite: 1]
        System.out.println("Navigasi ke Cuti/Izin...");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Kembali ke halaman Login[cite: 1]
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.setTitle("Manajemen Presensi - Login");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}