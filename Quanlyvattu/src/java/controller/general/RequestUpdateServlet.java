package controller.general;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.RequestDetailItem;
import dal.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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
            boolean showOnlyBack = requestItems.isEmpty();
            String message = showOnlyBack ? "This request is disapproved or contains no items." : null;
            boolean isAlreadyUpdated = checkIsAlreadyUpdated(reqId);

            request.setAttribute("requestItems", requestItems);
            request.setAttribute("requestId", requestId);
            request.setAttribute("showOnlyBack", showOnlyBack);
            request.setAttribute("isAlreadyUpdated", isAlreadyUpdated);
            request.setAttribute("message", message);
            request.setAttribute("messageType", "error");
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

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
            String mode = request.getParameter("mode");

            boolean success;
            if ("revert".equalsIgnoreCase(mode)) {
                success = revertRequestUpdate(requestId);
            } else {
                int totalItems = Integer.parseInt(request.getParameter("totalItems"));
                success = updateRequestActualQuantities(request, requestId, totalItems);
            }

            List<RequestDetailItem> requestItems = getRequestDetails(requestId);
            boolean showOnlyBack = requestItems.isEmpty();
            boolean isAlreadyUpdated = checkIsAlreadyUpdated(requestId);

            request.setAttribute("requestItems", requestItems);
            request.setAttribute("requestId", requestId);
            request.setAttribute("showOnlyBack", showOnlyBack);
            request.setAttribute("isAlreadyUpdated", isAlreadyUpdated);

            if (success) {
                request.setAttribute("message", ("revert".equalsIgnoreCase(mode)
                        ? "Request reverted successfully!"
                        : "Request updated successfully!"));
                request.setAttribute("messageType", "success");
            } else {
                request.setAttribute("message", ("revert".equalsIgnoreCase(mode)
                        ? "Failed to revert request!"
                        : "This request cannot be updated again or stock check failed."));
                request.setAttribute("messageType", "error");
            }

            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error occurred during request processing!");
            request.setAttribute("messageType", "error");
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    private boolean revertRequestUpdate(int requestId) throws SQLException {
        DBContext db = new DBContext();

        try (Connection conn = db.getConnection()) {
            if (!checkIsAlreadyUpdated(requestId, conn)) return false;

            String requestType = getRequestTypeName(conn, requestId);
            if (requestType == null) return false;

            conn.setAutoCommit(false);

            String selectSql = "SELECT MaterialId, ActualQuantity FROM RequestDetail WHERE RequestId = ?";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, requestId);
                ResultSet rs = selectStmt.executeQuery();

                while (rs.next()) {
                    int materialId = rs.getInt("MaterialId");
                    int actualQty = rs.getInt("ActualQuantity");

                    if (rs.wasNull()) continue;

                    String stockSql;
                    if ("Import".equalsIgnoreCase(requestType) || "Purchase".equalsIgnoreCase(requestType)) {
                        stockSql = "UPDATE Materials SET Quantity = Quantity - ? WHERE MaterialId = ? AND Quantity >= ?";
                    } else if ("Export".equalsIgnoreCase(requestType) || "Repair".equalsIgnoreCase(requestType)) {
                        stockSql = "UPDATE Materials SET Quantity = Quantity + ? WHERE MaterialId = ?";
                    } else {
                        conn.rollback();
                        return false;
                    }

                    try (PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                        stockStmt.setInt(1, actualQty);
                        stockStmt.setInt(2, materialId);
                        if (stockSql.contains("AND Quantity >=")) {
                            stockStmt.setInt(3, actualQty);
                        }

                        int affected = stockStmt.executeUpdate();
                        if (affected <= 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }
            }

            try (PreparedStatement clearStmt = conn.prepareStatement(
                    "UPDATE RequestDetail SET ActualQuantity = NULL WHERE RequestId = ?")) {
                clearStmt.setInt(1, requestId);
                clearStmt.executeUpdate();
            }

            try (PreparedStatement resetStmt = conn.prepareStatement(
                    "UPDATE RequestList SET IsUpdated = FALSE WHERE RequestId = ?")) {
                resetStmt.setInt(1, requestId);
                resetStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
        DBContext db = new DBContext();

        try (Connection conn = db.getConnection()) {
            if (checkIsAlreadyUpdated(requestId, conn)) return false;

            String requestTypeName = getRequestTypeName(conn, requestId);
            if (requestTypeName == null) return false;

            conn.setAutoCommit(false);

            String updateDetailSql = "UPDATE RequestDetail SET ActualQuantity = ? WHERE RequestId = ? AND MaterialId = ?";
            try (PreparedStatement detailStmt = conn.prepareStatement(updateDetailSql)) {
                for (int i = 0; i < totalItems; i++) {
                    int materialId = Integer.parseInt(request.getParameter("materialId_" + i));
                    int actualQuantity = Integer.parseInt(request.getParameter("actualQuantity_" + i));

                    detailStmt.setInt(1, actualQuantity);
                    detailStmt.setInt(2, requestId);
                    detailStmt.setInt(3, materialId);
                    detailStmt.addBatch();

                    String stockSql;
                    if ("Import".equalsIgnoreCase(requestTypeName) || "Purchase".equalsIgnoreCase(requestTypeName)) {
                        stockSql = "UPDATE Materials SET Quantity = Quantity + ? WHERE MaterialId = ?";
                    } else if ("Export".equalsIgnoreCase(requestTypeName) || "Repair".equalsIgnoreCase(requestTypeName)) {
                        stockSql = "UPDATE Materials SET Quantity = Quantity - ? WHERE MaterialId = ? AND Quantity >= ?";
                    } else {
                        conn.rollback();
                        return false;
                    }

                    try (PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                        stockStmt.setInt(1, actualQuantity);
                        stockStmt.setInt(2, materialId);
                        if (stockSql.contains("AND Quantity >=")) {
                            stockStmt.setInt(3, actualQuantity);
                        }

                        int affected = stockStmt.executeUpdate();
                        if (affected <= 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                int[] detailResults = detailStmt.executeBatch();
                for (int res : detailResults) {
                    if (res <= 0) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement markStmt = conn.prepareStatement(
                        "UPDATE RequestList SET IsUpdated = TRUE WHERE RequestId = ?")) {
                    markStmt.setInt(1, requestId);
                    markStmt.executeUpdate();
                }

                conn.commit();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkIsAlreadyUpdated(int requestId) throws SQLException {
        DBContext db = new DBContext();
        try (Connection conn = db.getConnection()) {
            return checkIsAlreadyUpdated(requestId, conn);
        }
    }

    private boolean checkIsAlreadyUpdated(int requestId, Connection conn) throws SQLException {
        String sql = "SELECT IsUpdated FROM RequestList WHERE RequestId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean("IsUpdated");
        }
    }

    private String getRequestTypeName(Connection conn, int requestId) throws SQLException {
        String sql = "SELECT rt.RequestTypeName FROM RequestList rl " +
                     "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId " +
                     "WHERE rl.RequestId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RequestTypeName");
            }
        }
        return null;
    }
}
