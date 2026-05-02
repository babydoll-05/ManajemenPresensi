package com.rplbo.app.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
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

    private boolean isClockedIn = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startClock();
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
    private void handlePresensi(ActionEvent event) {
        LocalTime now = LocalTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (!isClockedIn) {
            // Logika Clock-In
            lblJamMasuk.setText(now.format(dtf));
            lblStatusMasuk.setText("Tepat Waktu");
            btnPresensi.setText("🚪 Clock-Out Sekarang");
            btnPresensi.setStyle("-fx-background-color: #D32F2F; -fx-background-radius: 30; -fx-text-fill: white;");
            isClockedIn = true;
            System.out.println("User Clock-In pada: " + now);
            // TODO: Panggil DAO untuk INSERT ke tb_presensi[cite: 1]
        } else {
            // Logika Clock-Out
            lblJamKeluar.setText(now.format(dtf));
            lblStatusKeluar.setText("Selesai Shift");
            btnPresensi.setDisable(true);
            btnPresensi.setText("Presensi Hari Ini Selesai");
            System.out.println("User Clock-Out pada: " + now);
            // TODO: Panggil DAO untuk UPDATE tb_presensi[cite: 1]
        }
    }
}