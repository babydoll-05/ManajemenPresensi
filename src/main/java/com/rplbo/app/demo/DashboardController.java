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

    @FXML private Label lblJam;
    @FXML private Label lblTanggal;
    @FXML private Label lblInisial;
    @FXML private Label lblNamaProfil;
    @FXML private Label lblRole;
    @FXML private Label lblStatusDetail;
    @FXML private Label lblHadir;
    @FXML private Label lblTerlambat;
    @FXML private Label lblCuti;
    @FXML private Label lblIzin;

    private int idKaryawan;
    private String username;
    private String role;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initClock();
        lblStatusDetail.setText("Belum Absen Hari Ini. Silakan Clock-In.");
        lblHadir.setText("0 hari");
        lblTerlambat.setText("0 kali");
        lblCuti.setText("0 hari");
        lblIzin.setText("0");
    }

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

    public void setNamaPengguna(String username, String role, int idKaryawan) {
        this.username = username;
        this.role = role;
        this.idKaryawan = idKaryawan;
        lblNamaProfil.setText(username);
        lblRole.setText(role.substring(0, 1).toUpperCase() + role.substring(1));
        lblInisial.setText(String.valueOf(username.charAt(0)).toUpperCase());
    }

    @FXML
    private void handleMenuBeranda(ActionEvent event) {}

    @FXML
    private void handleMenuPresensi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/presensi-view.fxml"));
            Parent root = loader.load();

            PresensiController pc = loader.getController();
            pc.setUserData(username, role, idKaryawan);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Manajemen Presensi - Presensi");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuRiwayat(ActionEvent event) {
        System.out.println("Navigasi ke Riwayat...");
    }

    @FXML
    private void handleMenuCuti(ActionEvent event) {
        System.out.println("Navigasi ke Cuti/Izin...");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
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
