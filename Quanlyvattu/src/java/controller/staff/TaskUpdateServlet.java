package controller.staff;

import DAO.RequestDAO;
import DAO.TaskLogDAO;
import model.RequestDetailItem;
import model.Users;
import model.TaskLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import dal.DBContext;

@WebServlet("/taskUpdate")
public class TaskUpdateServlet extends HttpServlet {
    private final RequestDAO requestDAO = new RequestDAO();
    private final TaskLogDAO taskLogDAO = new TaskLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("login.jsp");
            return;
        }

        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr == null || requestIdStr.isEmpty()) {
            response.sendRedirect("tasklist.jsp");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            int requestId = Integer.parseInt(requestIdStr);

            List<RequestDetailItem> items = requestDAO.getRequestDetails(requestId);
            if (items.isEmpty()) {
                request.setAttribute("errorMessage", "Request not found or has no details");
                request.setAttribute("pageContent", "tasklist.jsp");
                request.getRequestDispatcher("layout/layout.jsp").forward(request, response);
                return;
            }

            List<TaskLog> taskLogs = taskLogDAO.getGroupedTaskLogsByRequestId(conn, requestId);

            String successMessage = (String) session.getAttribute("successMessage");
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestDetails", items);
            request.setAttribute("taskLogs", taskLogs);
            request.setAttribute("requestType", items.get(0).getRequestTypeName());
            request.setAttribute("note", items.get(0).getNote());
            request.setAttribute("requestStatus", items.get(0).getStatus());

            request.setAttribute("pageContent", "/taskUpdate.jsp");
            request.getRequestDispatcher("layout/layout.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading request details");
            request.setAttribute("pageContent", "tasklist.jsp");
            request.getRequestDispatcher("layout/layout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null || user.getRoleId() != 2) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String requestIdStr = request.getParameter("requestId");

        if (requestIdStr == null || requestIdStr.isEmpty()) {
            response.sendRedirect("tasklist.jsp");
            return;
        }

        try {
            int requestId = Integer.parseInt(requestIdStr);

            if ("createSlip".equals(action)) {
                handleCreateSlip(request, response, requestId, user);
            } else if ("signSlip".equals(action)) {
                handleSignSlip(request, response, requestId, user);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("tasklist.jsp");
        }
    }

    private void handleCreateSlip(HttpServletRequest request, HttpServletResponse response,
                                  int requestId, Users user) throws IOException {

        HttpSession session = request.getSession();

        try {
            List<RequestDetailItem> items = requestDAO.getRequestDetails(requestId);
            if (items.isEmpty()) {
                session.setAttribute("errorMessage", "Request not found");
                response.sendRedirect("taskUpdate?requestId=" + requestId);
                return;
            }

            String requestType = items.get(0).getRequestTypeName();
            int requestTypeId = "Import".equalsIgnoreCase(requestType) ? 1 : 2;

            List<Integer> materialIds = new ArrayList<>();
            List<Integer> actualQuantities = new ArrayList<>();
            boolean hasValidData = false;

            for (RequestDetailItem item : items) {
                String actualQtyStr = request.getParameter("actualQty_" + item.getMaterialId());
                if (actualQtyStr != null && !actualQtyStr.trim().isEmpty()) {
                    try {
                        int actualQty = Integer.parseInt(actualQtyStr.trim());
                        if (actualQty > 0) {
                            if ("Export".equalsIgnoreCase(requestType) && actualQty > item.getStockQuantity()) {
                                session.setAttribute("errorMessage", "Actual quantity cannot exceed stock: " + item.getMaterialName());
                                response.sendRedirect("taskUpdate?requestId=" + requestId);
                                return;
                            }

                            if (actualQty + item.getActualQuantity() > item.getQuantity()) {
                                session.setAttribute("errorMessage", "Actual quantity exceeds requested amount for: " + item.getMaterialName());
                                response.sendRedirect("taskUpdate?requestId=" + requestId);
                                return;
                            }

                            materialIds.add(item.getMaterialId());
                            actualQuantities.add(actualQty);
                            hasValidData = true;
                        }
                    } catch (NumberFormatException e) {
                        session.setAttribute("errorMessage", "Invalid quantity for material: " + item.getMaterialName());
                        response.sendRedirect("taskUpdate?requestId=" + requestId);
                        return;
                    }
                }
            }

            if (!hasValidData) {
                session.setAttribute("errorMessage", "Please enter at least one valid actual quantity");
                response.sendRedirect("taskUpdate?requestId=" + requestId);
                return;
            }

            try (Connection conn = new DBContext().getConnection()) {
                conn.setAutoCommit(false);

                boolean success = updateStockAndQuantities(conn, requestId, materialIds, actualQuantities, requestType);

                if (success) {
                    success = taskLogDAO.insertTaskLogWithDetails(
                        conn,
                        requestId,
                        requestTypeId,
                        user.getUserId(),
                        materialIds,
                        actualQuantities
                    );
                }

                if (success) {
                    requestDAO.updateStatusIfCompleted(conn, requestId);
                }

                if (success) {
                    conn.commit();
                    session.setAttribute("successMessage", "Slip created successfully");
                } else {
                    conn.rollback();
                    session.setAttribute("errorMessage", "Failed to create slip");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                session.setAttribute("errorMessage", "Database error occurred");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error processing request");
        }

        response.sendRedirect("taskUpdate?requestId=" + requestId);
    }

    private boolean updateStockAndQuantities(Connection conn, int requestId,
                                             List<Integer> materialIds, List<Integer> quantities,
                                             String requestType) throws SQLException {

        String updateDetailSql = "UPDATE RequestDetail SET ActualQuantity = ActualQuantity + ? WHERE RequestId = ? AND MaterialId = ?";
        try (var detailStmt = conn.prepareStatement(updateDetailSql)) {
            for (int i = 0; i < materialIds.size(); i++) {
                detailStmt.setInt(1, quantities.get(i));
                detailStmt.setInt(2, requestId);
                detailStmt.setInt(3, materialIds.get(i));
                detailStmt.addBatch();
            }
            detailStmt.executeBatch();
        }

        String stockSql;
        if ("Import".equalsIgnoreCase(requestType)) {
            stockSql = "UPDATE Materials SET Quantity = Quantity + ? WHERE MaterialId = ?";
        } else {
            stockSql = "UPDATE Materials SET Quantity = Quantity - ? WHERE MaterialId = ? AND Quantity >= ?";
        }

        try (var stockStmt = conn.prepareStatement(stockSql)) {
            for (int i = 0; i < materialIds.size(); i++) {
                stockStmt.setInt(1, quantities.get(i));
                stockStmt.setInt(2, materialIds.get(i));
                if ("Export".equalsIgnoreCase(requestType)) {
                    stockStmt.setInt(3, quantities.get(i));
                }
                stockStmt.addBatch();
            }
            int[] results = stockStmt.executeBatch();
            for (int result : results) {
                if (result <= 0) return false;
            }
        }

        return true;
    }

    private void handleSignSlip(HttpServletRequest request, HttpServletResponse response,
                                int requestId, Users user) throws ServletException, IOException {
        try (Connection conn = new DBContext().getConnection()) {
            List<RequestDetailItem> items = requestDAO.getRequestDetails(requestId);
            List<TaskLog> taskLogs = taskLogDAO.getGroupedTaskLogsByRequestId(conn, requestId);

            if (items.isEmpty() || taskLogs.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "No slip data found to sign");
                response.sendRedirect("taskUpdate?requestId=" + requestId);
                return;
            }

            request.setAttribute("requestId", requestId);
            request.setAttribute("requestDetails", items);
            request.setAttribute("taskLogs", taskLogs);
            request.setAttribute("requestType", items.get(0).getRequestTypeName());
            request.setAttribute("note", items.get(0).getNote());
            request.setAttribute("staffName", user.getFullName());
            request.setAttribute("signDate", new java.util.Date());

            request.getRequestDispatcher("slip.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Error generating slip");
            response.sendRedirect("taskUpdate?requestId=" + requestId);
        }
    }
}
