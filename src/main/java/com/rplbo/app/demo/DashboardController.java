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

    // LABEL DASHBOARD
    @FXML
    private Label lblJam;

    @FXML
    private Label lblTanggal;

    @FXML
    private Label lblNamaProfil;

    @FXML
    private Label lblStatusDetail;

    @FXML
    private Label lblHadir;

    @FXML
    private Label lblTerlambat;

    @FXML
    private Label lblCuti;

    @FXML
    private Label lblIzin;

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {

        // JAM REALTIME
        initClock();

        // LOAD DATA DASHBOARD
        loadUserData();
    }

    /**
     * JAM DIGITAL REALTIME
     */
    private void initClock() {

        Locale localeID =
                new Locale("id", "ID");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern(
                        "HH:mm:ss"
                );

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy",
                        localeID
                );

        Timeline clock = new Timeline(

                new KeyFrame(Duration.ZERO, e -> {

                    LocalDateTime now =
                            LocalDateTime.now();

                    lblJam.setText(
                            now.format(timeFormatter)
                    );

                    lblTanggal.setText(
                            now.format(dateFormatter)
                    );
                }),

                new KeyFrame(Duration.seconds(1))
        );

        clock.setCycleCount(
                Animation.INDEFINITE
        );

        clock.play();
    }

    /**
     * LOAD DATA AWAL DASHBOARD
     */
    private void loadUserData() {

        lblStatusDetail.setText(
                "Belum Absen Hari Ini. Silakan Clock-In."
        );

        lblHadir.setText(
                "0 hari"
        );

        lblTerlambat.setText(
                "0 kali"
        );

        lblCuti.setText(
                "0 hari"
        );

        lblIzin.setText(
                "0"
        );
    }

    /**
     * MENERIMA USERNAME DARI LOGIN
     */
    public void setNamaPengguna(String username) {

        lblNamaProfil.setText(username);
    }

    // =====================================================
    // NAVIGASI MENU
    // =====================================================

    /**
     * MENU BERANDA
     */
    @FXML
    private void handleMenuBeranda(ActionEvent event) {

        System.out.println(
                "Anda sedang berada di Dashboard."
        );
    }

    /**
     * MENU PRESENSI
     */
    @FXML
    private void handleMenuPresensi(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "presensi-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    (Stage)((Node)event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 900, 600)
            );

            stage.setTitle(
                    "Manajemen Presensi - Presensi"
            );

            stage.show();

        } catch (IOException e) {

            System.err.println(
                    "Gagal membuka halaman Presensi"
            );

            e.printStackTrace();
        }
    }

    /**
     * MENU RIWAYAT
     */
    @FXML
    private void handleMenuRiwayat(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "riwayat-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    (Stage)((Node)event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 900, 600)
            );

            stage.setTitle(
                    "Manajemen Presensi - Riwayat"
            );

            stage.show();

        } catch (IOException e) {

            System.err.println(
                    "Gagal membuka halaman Riwayat"
            );

            e.printStackTrace();
        }
    }

    /**
     * MENU CUTI / IZIN
     */
    @FXML
    private void handleMenuCuti(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "cuti-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    (Stage)((Node)event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 900, 600)
            );

            stage.setTitle(
                    "Manajemen Presensi - Cuti"
            );

            stage.show();

        } catch (IOException e) {

            System.err.println(
                    "Gagal membuka halaman Cuti"
            );

            e.printStackTrace();
        }
    }

    /**
     * LOGOUT
     */
    @FXML
    private void handleLogout(ActionEvent event) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "login-view.fxml"
                            )
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    (Stage)((Node)event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root, 400, 500)
            );

            stage.setTitle(
                    "Manajemen Presensi - Login"
            );

            stage.centerOnScreen();

            stage.show();

        } catch (IOException e) {

            System.err.println(
                    "Gagal logout"
            );

            e.printStackTrace();
        }
    }
}