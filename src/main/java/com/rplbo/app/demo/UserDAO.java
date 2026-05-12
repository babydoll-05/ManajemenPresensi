package com.rplbo.app.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public String[] authenticateUserFull(String username, String password) {
        String query = "SELECT p.role, k.id_karyawan FROM pengguna p JOIN karyawan k ON p.id_karyawan = k.id_karyawan WHERE p.username = ? AND p.password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("role"), String.valueOf(rs.getInt("id_karyawan"))};
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
