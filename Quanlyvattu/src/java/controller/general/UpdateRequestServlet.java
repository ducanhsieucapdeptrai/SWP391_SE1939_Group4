package controller.general;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateRequestServlet")
public class UpdateRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quan_ly_vat_tu";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Update quantities for each request detail
            String[] quantities = {
                request.getParameter("quantity_15_1"),
                request.getParameter("quantity_15_2"),
                request.getParameter("quantity_16_1"),
                request.getParameter("quantity_16_2")
            };

            int[] requestIds = {1, 1, 2, 2}; // Assuming RequestId 1 for day 15, RequestId 2 for day 16
            int[] materialIds = {1, 2, 1, 2}; // Assuming MaterialId 1 and 2 for Hammer examples
            int index = 0;

            String sql = "UPDATE RequestDetail SET Quantity = ? WHERE RequestId = ? AND MaterialId = ?";
            pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < quantities.length; i++) {
                if (quantities[i] != null && !quantities[i].isEmpty()) {
                    int quantity = Integer.parseInt(quantities[i]);
                    pstmt.setInt(1, quantity);
                    pstmt.setInt(2, requestIds[i]);
                    pstmt.setInt(3, materialIds[i]);
                    pstmt.addBatch();
                    index++;
                }
            }

            if (index > 0) {
                pstmt.executeBatch();
                // Update RequestList status to 'Approved' after successful update
                String updateStatusSql = "UPDATE RequestList SET Status = 'Approved', ApprovedDate = NOW() WHERE RequestId IN (?, ?)";
                try (PreparedStatement statusPstmt = conn.prepareStatement(updateStatusSql)) {
                    statusPstmt.setInt(1, 1);
                    statusPstmt.setInt(2, 2);
                    statusPstmt.executeUpdate();
                }
            }

            // Redirect to a success page or back to the form
            response.sendRedirect("requestUpdate.jsp?success=true");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("requestUpdate.jsp?error=true");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}