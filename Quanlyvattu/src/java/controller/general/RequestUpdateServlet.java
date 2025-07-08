package controller.general;

import dal.DBContext;
import model.RequestDetailItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/RequestUpdateServlet")
public class RequestUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            List<RequestDetailItem> items = getRequestDetails(requestId);

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestItems", items);
            request.setAttribute("editMode", false);
            request.setAttribute("actionMode", "start");
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int requestId = Integer.parseInt(request.getParameter("requestId"));
        String mode = request.getParameter("mode");
        boolean editMode = false;
        String message = null;
        String messageType = "error";

        try (Connection conn = new DBContext().getConnection()) {

            HttpSession session = request.getSession();
            List<RequestDetailItem> parsedItems = new ArrayList<>();
            List<RequestDetailItem> requestItems;

            // ✅ Only parse when needed
            if ("edit".equals(mode) || "save".equals(mode)) {
                parsedItems = parseRequestItems(request, requestId);
            }

            switch (mode) {
                case "edit" -> {
                    editMode = true;
                    message = "Edit mode enabled.";
                    messageType = "info";
                }

                case "save" -> {
                    if (saveActualQuantities(conn, requestId, parsedItems)) {
                        String requestType = getRequestTypeName(conn, requestId);
                        int listId = -1;

                        // ✅ Get listId from session
                        if ("Import".equalsIgnoreCase(requestType)) {
                            Object obj = session.getAttribute("importId_" + requestId);
                            if (obj != null) listId = (int) obj;
                        } else {
                            Object obj = session.getAttribute("exportId_" + requestId);
                            if (obj != null) listId = (int) obj;
                        }

                        // ✅ Fallback to DB if needed
                        if (listId == -1) {
                            String listSql = "Import".equalsIgnoreCase(requestType)
                                    ? "SELECT ImportId FROM ImportList WHERE RequestId = ?"
                                    : "SELECT ExportId FROM ExportList WHERE RequestId = ?";
                            try (PreparedStatement ps = conn.prepareStatement(listSql)) {
                                ps.setInt(1, requestId);
                                ResultSet rs = ps.executeQuery();
                                if (rs.next()) {
                                    listId = rs.getInt(1);
                                }
                            }
                        }

                        // ✅ Update ImportDetail or ExportDetail
                        if (listId != -1) {
                            String updateSql = "Import".equalsIgnoreCase(requestType)
                                    ? "UPDATE ImportDetail SET Quantity = ? WHERE ImportId = ? AND MaterialId = ?"
                                    : "UPDATE ExportDetail SET Quantity = ? WHERE ExportId = ? AND MaterialId = ?";
                            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                                for (RequestDetailItem item : parsedItems) {
                                    ps.setInt(1, item.getActualQuantity());
                                    ps.setInt(2, listId);
                                    ps.setInt(3, item.getMaterialId());
                                    ps.addBatch();
                                }
                                ps.executeBatch();
                            }
                        }

                        message = "Saved successfully.";
                        messageType = "success";
                    } else {
                        message = "Save failed.";
                    }
                }

                case "accept" -> {
                    String requestType = getRequestTypeName(conn, requestId);
                    String insertMainSql, insertDetailSql;
                    String tableAlias;
                    int listId;

                    if ("Import".equalsIgnoreCase(requestType)) {
                        insertMainSql = "INSERT INTO ImportList (RequestId, ImportDate) VALUES (?, NOW())";
                        insertDetailSql = "INSERT INTO ImportDetail (ImportId, MaterialId, Quantity, Price) VALUES (?, ?, ?, ?)";
                        tableAlias = "Import";
                    } else {
                        insertMainSql = "INSERT INTO ExportList (RequestId, ExportDate) VALUES (?, NOW())";
                        insertDetailSql = "INSERT INTO ExportDetail (ExportId, MaterialId, Quantity, Price) VALUES (?, ?, ?, ?)";
                        tableAlias = "Export";
                    }

                    try (PreparedStatement ps = conn.prepareStatement(insertMainSql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setInt(1, requestId);
                        ps.executeUpdate();
                        ResultSet rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            listId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to get generated ID.");
                        }
                    }

                    List<RequestDetailItem> items = getRequestDetails(requestId);
                    try (PreparedStatement ps = conn.prepareStatement(insertDetailSql)) {
                        for (RequestDetailItem item : items) {
                            ps.setInt(1, listId);
                            ps.setInt(2, item.getMaterialId());
                            ps.setInt(3, item.getActualQuantity());
                            ps.setDouble(4, item.getPrice());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }

                    if ("Import".equalsIgnoreCase(requestType)) {
                        session.setAttribute("importId_" + requestId, listId);
                    } else {
                        session.setAttribute("exportId_" + requestId, listId);
                    }

                    message = "Request accepted and " + tableAlias + " records created.";
                    messageType = "success";
                }

                case "reject" -> {
                    message = "Request rejected.";
                    messageType = "warning";
                }

                case "start" -> {
                    message = "Review started.";
                    messageType = "info";
                }
            }

            requestItems = getRequestDetails(requestId);
            request.setAttribute("requestId", requestId);
            request.setAttribute("requestItems", requestItems);
            request.setAttribute("editMode", editMode);

            String actionMode = switch (mode) {
                case "start" -> "reviewing";
                case "accept", "reject", "edit", "save" -> "done";
                default -> "start";
            };

            request.setAttribute("actionMode", actionMode);
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Unexpected error occurred.");
            request.setAttribute("messageType", "error");
            request.setAttribute("pageContent", "/requestUpdate.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    private List<RequestDetailItem> parseRequestItems(HttpServletRequest request, int requestId) {
        int totalItems = Integer.parseInt(request.getParameter("totalItems"));
        List<RequestDetailItem> items = new ArrayList<>();

        for (int i = 0; i < totalItems; i++) {
            int materialId = Integer.parseInt(request.getParameter("materialId_" + i));
            int actualQuantity = Integer.parseInt(request.getParameter("actualQuantity_" + i));
            RequestDetailItem item = new RequestDetailItem();
            item.setRequestId(requestId);
            item.setMaterialId(materialId);
            item.setActualQuantity(actualQuantity);
            items.add(item);
        }
        return items;
    }

    private boolean saveActualQuantities(Connection conn, int requestId, List<RequestDetailItem> items) throws SQLException {
        String sql = "UPDATE RequestDetail SET ActualQuantity = ? WHERE RequestId = ? AND MaterialId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (RequestDetailItem item : items) {
                stmt.setInt(1, item.getActualQuantity());
                stmt.setInt(2, requestId);
                stmt.setInt(3, item.getMaterialId());
                stmt.addBatch();
            }
            int[] result = stmt.executeBatch();
            for (int r : result) {
                if (r <= 0) return false;
            }
            return true;
        }
    }

    private List<RequestDetailItem> getRequestDetails(int requestId) throws SQLException {
        List<RequestDetailItem> items = new ArrayList<>();
        String sql = "SELECT rd.RequestId, rd.MaterialId, rd.Quantity, rd.ActualQuantity, " +
                "m.MaterialName, rt.RequestTypeName, rl.Note, m.Quantity as StockQuantity, m.Price " +
                "FROM RequestDetail rd " +
                "JOIN Materials m ON rd.MaterialId = m.MaterialId " +
                "JOIN RequestList rl ON rd.RequestId = rl.RequestId " +
                "JOIN RequestType rt ON rl.RequestTypeId = rt.RequestTypeId " +
                "WHERE rd.RequestId = ?";

        try (Connection conn = new DBContext().getConnection();
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
                item.setPrice(rs.getDouble("Price"));
                items.add(item);
            }
        }
        return items;
    }

    private String getRequestTypeName(Connection conn, int requestId) throws SQLException {
        String sql = "SELECT rt.RequestTypeName FROM RequestList r JOIN RequestType rt ON r.RequestTypeId = rt.RequestTypeId WHERE r.RequestId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("RequestTypeName");
            }
        }
        return "Unknown";
    }
}
