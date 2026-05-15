package com.rplbo.app.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.Locale;
import java.util.ResourceBundle;

public class PresensiController implements Initializable {

    @FXML
    private Label lblJamDigital;

    @FXML
    private Label lblTanggal;

    @FXML
    private Label lblJamMasuk;

    @FXML
    private Label lblJamKeluar;

    @FXML
    private Label lblStatusMasuk;

    @FXML
    private Label lblStatusKeluar;

    @FXML
    private Button btnPresensi;

    private boolean isClockedIn = false;

    @Override
    public void initialize(URL location,
                           ResourceBundle resources) {

        startClock();

        loadPresensiHariIni();
    }

    // JAM REALTIME
    private void startClock() {

        Locale localeID =
                new Locale("id", "ID");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy",
                        localeID
                );

        Timeline clock = new Timeline(

                new KeyFrame(Duration.ZERO, e -> {

                    LocalDateTime now =
                            LocalDateTime.now();

                    lblJamDigital.setText(
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

    // HANDLE PRESENSI
    @FXML
    private void handlePresensi(ActionEvent event) {

        LocalTime now = LocalTime.now();

        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        // CLOCK IN
        if (!isClockedIn) {

            lblJamMasuk.setText(
                    now.format(dtf)
            );

            lblStatusMasuk.setText(
                    "Tepat Waktu"
            );

            btnPresensi.setText(
                    "🚪 Clock-Out Sekarang"
            );

            btnPresensi.setStyle(
                    "-fx-background-color: #D32F2F;" +
                            "-fx-background-radius: 10;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 18px;"
            );

            simpanClockIn(now);

            isClockedIn = true;

            System.out.println(
                    "User Clock-In: " + now
            );
        }

        // CLOCK OUT
        else {

            lblJamKeluar.setText(
                    now.format(dtf)
            );

            lblStatusKeluar.setText(
                    "Selesai Shift"
            );

            btnPresensi.setDisable(true);

            btnPresensi.setText(
                    "Presensi Hari Ini Selesai"
            );

            simpanClockOut(now);

            System.out.println(
                    "User Clock-Out: " + now
            );
        }
    }

    // SIMPAN CLOCK IN
    private void simpanClockIn(LocalTime jamMasuk) {

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            String sql =
                    "INSERT INTO tb_presensi " +
                            "(tanggal, jam_masuk, status) " +
                            "VALUES (?, ?, ?)";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setDate(
                    1,
                    java.sql.Date.valueOf(
                            LocalDate.now()
                    )
            );

            pst.setTime(
                    2,
                    java.sql.Time.valueOf(
                            jamMasuk
                    )
            );

            pst.setString(
                    3,
                    "Tepat Waktu"
            );

            pst.executeUpdate();

            pst.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // SIMPAN CLOCK OUT
    private void simpanClockOut(LocalTime jamKeluar) {

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            String sql =
                    "UPDATE tb_presensi " +
                            "SET jam_keluar = ?, status = ? " +
                            "WHERE tanggal = ?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setTime(
                    1,
                    java.sql.Time.valueOf(
                            jamKeluar
                    )
            );

            pst.setString(
                    2,
                    "Selesai Shift"
            );

            pst.setDate(
                    3,
                    java.sql.Date.valueOf(
                            LocalDate.now()
                    )
            );

            pst.executeUpdate();

            pst.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // LOAD DATA HARI INI
    private void loadPresensiHariIni() {

        try {

            Connection conn =
                    DatabaseConnection.getConnection();

            String sql =
                    "SELECT * FROM tb_presensi " +
                            "WHERE tanggal = ?";

            PreparedStatement pst =
                    conn.prepareStatement(sql);

            pst.setDate(
                    1,
                    java.sql.Date.valueOf(
                            LocalDate.now()
                    )
            );

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                lblJamMasuk.setText(
                        rs.getString("jam_masuk")
                );

                lblStatusMasuk.setText(
                        rs.getString("status")
                );

                isClockedIn = true;

                // SUDAH CLOCK OUT
                if (rs.getString("jam_keluar") != null) {

                    lblJamKeluar.setText(
                            rs.getString("jam_keluar")
                    );

                    lblStatusKeluar.setText(
                            "Selesai Shift"
                    );

                    btnPresensi.setDisable(true);

                    btnPresensi.setText(
                            "Presensi Hari Ini Selesai"
                    );

                } else {

                    btnPresensi.setText(
                            "🚪 Clock-Out Sekarang"
                    );
                }
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // KEMBALI DASHBOARD
    @FXML
    private void handleKembaliDashboard(ActionEvent event)
            throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "dashboard-karyawan-view.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage)((Node)event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(scene);

        stage.setTitle("Dashboard");

        stage.show();
    }
}