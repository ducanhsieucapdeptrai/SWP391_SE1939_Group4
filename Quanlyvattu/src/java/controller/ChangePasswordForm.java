package controller;

import java.sql.*;
import javax.swing.*;

public class ChangePasswordForm extends JFrame {
    private JTextField emailField;
    private JPasswordField currentPassField, newPassField, confirmPassField;
    private JButton changeButton;

    public ChangePasswordForm() {
        setTitle("Đổi mật khẩu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 20, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(140, 20, 200, 25);
        add(emailField);

        JLabel currentPassLabel = new JLabel("Mật khẩu hiện tại:");
        currentPassLabel.setBounds(30, 60, 120, 25);
        add(currentPassLabel);
        currentPassField = new JPasswordField();
        currentPassField.setBounds(140, 60, 200, 25);
        add(currentPassField);

        JLabel newPassLabel = new JLabel("Mật khẩu mới:");
        newPassLabel.setBounds(30, 100, 100, 25);
        add(newPassLabel);
        newPassField = new JPasswordField();
        newPassField.setBounds(140, 100, 200, 25);
        add(newPassField);

        JLabel confirmPassLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPassLabel.setBounds(30, 140, 120, 25);
        add(confirmPassLabel);
        confirmPassField = new JPasswordField();
        confirmPassField.setBounds(140, 140, 200, 25);
        add(confirmPassField);

        changeButton = new JButton("Đổi mật khẩu");
        changeButton.setBounds(140, 190, 150, 30);
        add(changeButton);

        changeButton.addActionListener(e -> changePassword());
    }

    private void changePassword() {
        String email = emailField.getText().trim();
        String currentPassword = new String(currentPassField.getPassword());
        String newPassword = new String(newPassField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp!");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quan_ly_vat_tu", "root", "yourpassword");

            String hashedCurrent = PasswordUtils.hashPassword(currentPassword);
            String hashedNew = PasswordUtils.hashPassword(newPassword);

            String sql = "SELECT * FROM UsersList WHERE Email = ? AND Password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, hashedCurrent);
            rs = stmt.executeQuery();

            if (rs.next()) {
                sql = "UPDATE UsersList SET Password = ? WHERE Email = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, hashedNew);
                stmt.setString(2, email);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi cập nhật.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sai mật khẩu hiện tại.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL.");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChangePasswordForm().setVisible(true);
        });
    }
}