package com.rplbo.app.demo;

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

        String[] result = userDAO.authenticateUserFull(username, password);

        if (result != null) {
            lblError.setVisible(false);
            String role = result[0];
            int idKaryawan = Integer.parseInt(result[1]);
            loadDashboard(event, username, role, idKaryawan);
        } else {
            lblError.setText("Username atau password salah!");
            lblError.setVisible(true);
        }
    }

    private void loadDashboard(ActionEvent event, String username, String role, int idKaryawan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/rplbo/app/demo/dashboard-karyawan-view.fxml"));
            Parent root = loader.load();

            DashboardController dc = loader.getController();
            dc.setNamaPengguna(username, role, idKaryawan);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Aplikasi Manajemen Presensi - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
