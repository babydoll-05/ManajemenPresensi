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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class PresensiController implements Initializable {

    @FXML private Label lblJamDigital;
    @FXML private Label lblTanggal;
    @FXML private Label lblJamMasuk;
    @FXML private Label lblJamKeluar;
    @FXML private Label lblStatusMasuk;
    @FXML private Label lblStatusKeluar;
    @FXML private Button btnPresensi;

    private int idKaryawan;
    private String username;
    private String role;
    private int idPresensi = -1;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startClock();
    }

    public void setUserData(String username, String role, int idKaryawan) {
        this.username = username;
        this.role = role;
        this.idKaryawan = idKaryawan;
        loadPresensiHariIni();
    }

    private void loadPresensiHariIni() {
        String today = LocalDate.now().toString();
        String query = "SELECT id_presensi, jam_masuk, jam_keluar FROM presensi WHERE id_karyawan = ? AND tanggal = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idKaryawan);
            stmt.setString(2, today);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                idPresensi = rs.getInt("id_presensi");
                String jamMasuk = rs.getString("jam_masuk");
                String jamKeluar = rs.getString("jam_keluar");

                lblJamMasuk.setText(jamMasuk != null ? jamMasuk : "-- : -- : --");
                lblStatusMasuk.setText(jamMasuk != null ? "Tepat Waktu" : "Belum Absen");

                if (jamKeluar != null) {
                    lblJamKeluar.setText(jamKeluar);
                    lblStatusKeluar.setText("Selesai Shift");
                    btnPresensi.setDisable(true);
                    btnPresensi.setText("Presensi Hari Ini Selesai");
                } else if (jamMasuk != null) {
                    btnPresensi.setText("🚪 Clock-Out Sekarang");
                    btnPresensi.setStyle("-fx-background-color: #D32F2F; -fx-background-radius: 30; -fx-text-fill: white;");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startClock() {
        Locale localeID = new Locale("id", "ID");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeID);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            lblJamDigital.setText(now.format(timeFormatter));
            lblTanggal.setText(now.format(dateFormatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rplbo/app/demo/dashboard-karyawan-view.fxml"));
            Parent root = loader.load();

            DashboardController dc = loader.getController();
            dc.setNamaPengguna(username, role, idKaryawan);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Aplikasi Manajemen Presensi - Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePresensi(ActionEvent event) {
        LocalTime now = LocalTime.now();
        String today = LocalDate.now().toString();

        if (idPresensi == -1) {
            String sql = "INSERT INTO presensi (id_karyawan, tanggal, jam_masuk, status_kehadiran, status_waktu) VALUES (?, ?, ?, 'hadir', 'tepat_waktu')";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, idKaryawan);
                stmt.setString(2, today);
                stmt.setString(3, now.format(DTF));
                stmt.executeUpdate();

                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) idPresensi = keys.getInt(1);

                lblJamMasuk.setText(now.format(DTF));
                lblStatusMasuk.setText("Tepat Waktu");
                btnPresensi.setText("🚪 Clock-Out Sekarang");
                btnPresensi.setStyle("-fx-background-color: #D32F2F; -fx-background-radius: 30; -fx-text-fill: white;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String sql = "UPDATE presensi SET jam_keluar = ? WHERE id_presensi = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, now.format(DTF));
                stmt.setInt(2, idPresensi);
                stmt.executeUpdate();

                lblJamKeluar.setText(now.format(DTF));
                lblStatusKeluar.setText("Selesai Shift");
                btnPresensi.setDisable(true);
                btnPresensi.setText("Presensi Hari Ini Selesai");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
