package controller.general;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.RequestDetailItem;
import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RequestUpdateServlet")
public class RequestUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String requestId = request.getParameter("requestId");

        if (requestId == null || requestId.isEmpty()) {
            response.sendRedirect("reqlist");
            return;
        }

        try {
            int reqId = Integer.parseInt(requestId);
            List<RequestDetailItem> requestItems = getRequestDetails(reqId);
            request.setAttribute("requestItems", requestItems);
            request.setAttribute("requestId", requestId);
            request.getRequestDispatcher("requestUpdate.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            int totalItems = Integer.parseInt(request.getParameter("totalItems"));

            boolean success = updateRequestActualQuantities(request, requestId, totalItems);

            if (success) {
                request.setAttribute("message", "Request updated successfully!");
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", "Failed to update request!");
                request.setAttribute("messageType", "error");
            }

            List<RequestDetailItem> requestItems = getRequestDetails(requestId);
            request.setAttribute("requestItems", requestItems);
            request.setAttribute("requestId", requestId);
            request.getRequestDispatcher("requestUpdate.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error occurred while updating request!");
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("requestUpdate.jsp").forward(request, response);
        }
    }

    private List<RequestDetailItem> getRequestDetails(int requestId) throws SQLException {
        List<RequestDetailItem> items = new ArrayList<>();
        String sql = "SELECT rd.RequestId, rd.MaterialId, rd.Quantity, rd.ActualQuantity, " +
                     "m.MaterialName, rt.RequestTypeName, rl.Note, m.Quantity as StockQuantity " +
                     "FROM RequestDetail rd " +
                     "JOIN Materials m ON rd.MaterialId = m.MaterialId " +
                     "JOIN RequestList rl ON rd.RequestId = rl.RequestId " +
                     "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId " +
                     "WHERE rd.RequestId = ?";

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, requestId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RequestDetailItem item = new RequestDetailItem();
                item.setRequestId(rs.getInt("RequestId"));
                item.setMaterialId(rs.getInt("MaterialId"));
                item.setMaterialName(rs.getString("MaterialName"));
                item.setRequestTypeName(rs.getString("RequestTypeName"));
                item.setQuantity(rs.getInt("Quantity"));
                item.setActualQuantity(rs.getInt("ActualQuantity"));
                item.setNote(rs.getString("Note"));
                item.setStockQuantity(rs.getInt("StockQuantity"));
                items.add(item);
            }
        }

        return items;
    }

    private boolean updateRequestActualQuantities(HttpServletRequest request, int requestId, int totalItems) throws SQLException {
        String sql = "UPDATE RequestDetail SET ActualQuantity = ? WHERE RequestId = ? AND MaterialId = ?";

        DBContext db = new DBContext();
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < totalItems; i++) {
                String materialIdParam = request.getParameter("materialId_" + i);
                String actualQtyParam = request.getParameter("actualQuantity_" + i);

                if (materialIdParam == null || actualQtyParam == null) {
                    conn.rollback();
                    return false;
                }

                int materialId = Integer.parseInt(materialIdParam);
                int actualQuantity = Integer.parseInt(actualQtyParam);

                pstmt.setInt(1, actualQuantity);
                pstmt.setInt(2, requestId);
                pstmt.setInt(3, materialId);
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            for (int result : results) {
                if (result <= 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
