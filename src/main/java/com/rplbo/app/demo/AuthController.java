package com.rplbo.app.demo;

import com.presensi.dao.UserDAO; // Sesuaikan jika package UserDAO kamu berbeda
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username dan password tidak boleh kosong!");
            lblError.setVisible(true);
            return;
        }

        // Cek ke database
        String role = userDAO.authenticateUser(username, password);

        if (role != null) {
            lblError.setVisible(false);

            // Pindah halaman jika rolenya karyawan atau admin
            if (role.equals("karyawan") || role.equals("admin")) {
                loadKaryawanDashboard(event, username); // Mengirim username
            }
        } else {
            lblError.setText("Username atau password salah!");
            lblError.setVisible(true);
        }
    }

    // Method untuk memuat Dashboard dan mengirim data Username
    private void loadKaryawanDashboard(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rplbo/app/demo/dashboard-karyawan-view.fxml"));
            Parent root = loader.load();

            // MENGIRIM DATA: Mengambil controller Dashboard dan mengirim username
            DashboardController dashboardController = loader.getController();
            dashboardController.setNamaPengguna(username);

            // Mengganti jendela
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setTitle("Aplikasi Manajemen Presensi - Dashboard Karyawan");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("GAGAL MEMUAT DASHBOARD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}