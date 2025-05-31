package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    public Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:mysql://localhost:3306/quan_ly_vat_tu?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "123456"; // sửa theo đúng mật khẩu MySQL

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connected successfully!");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Optional: thêm method để lấy connection
    public Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();

            if (conn != null) {
                System.out.println("✅ Kết nối thành công tới cơ sở dữ liệu.");
            } else {
                System.out.println("❌ Kết nối thất bại.");
            }

            conn.close(); // Đóng kết nối sau khi kiểm tra
        } catch (Exception e) {
            System.out.println("❌ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
