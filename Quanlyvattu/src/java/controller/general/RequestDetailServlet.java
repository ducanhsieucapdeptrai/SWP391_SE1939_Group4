package controller.general;

import DAO.RequestDetailDAO;
import DAO.MaterialDAO;
import DAO.RequestDAO;
import model.Material;
import model.RequestDetail;
import model.RequestList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import dal.DBContext;

@WebServlet("/request-detail")
public class RequestDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required");
                return;
            }

            int requestId;
            try {
                requestId = Integer.parseInt(idParam.trim());
                if (requestId <= 0) {
                    throw new NumberFormatException("ID must be positive");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID format: " + idParam);
                return;
            }

            // Lấy thông tin request
            RequestDAO requestDAO = new RequestDAO();
            RequestList requestInfo = requestDAO.getRequestById(requestId);
            
            // Lấy chi tiết materials
            RequestDetailDAO dao = new RequestDetailDAO();
            List<RequestDetail> details = dao.getRequestDetailsByRequestId(requestId);
            String status = dao.getRequestStatus(requestId);

            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> allMaterials = materialDAO.getAllMaterials();

            // Set attributes
            request.setAttribute("requestInfo", requestInfo);
            request.setAttribute("requestDetails", details);
            request.setAttribute("requestStatus", status);
            request.setAttribute("requestId", requestId);
            request.setAttribute("allMaterials", allMaterials);

            if (details == null || details.isEmpty()) {
                request.setAttribute("message", "Không có vật tư nào trong yêu cầu này (ID: " + requestId + ")");
            }

            request.setAttribute("pageContent", "/request-detail.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error in RequestDetailServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String action = request.getParameter("action");
            String requestIdParam = request.getParameter("requestId");

            if (requestIdParam == null || requestIdParam.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Request ID is required");
                doGet(request, response);
                return;
            }

            int requestId;
            try {
                requestId = Integer.parseInt(requestIdParam.trim());
                if (requestId <= 0) {
                    throw new NumberFormatException("ID must be positive");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid request ID format");
                doGet(request, response);
                return;
            }

            if ("approve".equals(action)) {
                handleApprove(request, response, requestId);
            } else if ("reject".equals(action)) {
                handleReject(request, response, requestId);
            } else {
                request.setAttribute("errorMessage", "Invalid action");
                doGet(request, response);
            }

        } catch (Exception e) {
            System.out.println("Error in RequestDetailServlet POST: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request");
            doGet(request, response);
        }
    }

    private void handleApprove(HttpServletRequest request, HttpServletResponse response, int requestId)
            throws ServletException, IOException {

        String note = request.getParameter("note");
        if (note == null) {
            note = "";
        }

        try {
            boolean success = updateRequestStatus(requestId, "APPROVED", note);

            if (success) {
                request.setAttribute("successMessage", "Request has been approved successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to approve request. Please try again.");
            }

        } catch (Exception e) {
            System.out.println("Error approving request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while approving the request");
        }

        response.sendRedirect("request-detail?id=" + requestId);
    }

    private void handleReject(HttpServletRequest request, HttpServletResponse response, int requestId)
            throws ServletException, IOException {

        String reason = request.getParameter("reason");

        if (reason == null || reason.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Reason for rejection is required");
            doGet(request, response);
            return;
        }

        try {
            boolean success = updateRequestStatus(requestId, "REJECTED", reason);

            if (success) {
                request.setAttribute("successMessage", "Request has been rejected successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to reject request. Please try again.");
            }

        } catch (Exception e) {
            System.out.println("Error rejecting request: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while rejecting the request");
        }

        response.sendRedirect("request-detail?id=" + requestId);
    }

    private boolean updateRequestStatus(int requestId, String status, String note) {
        String sql = "UPDATE Requests SET Status = ?, Note = ?, UpdatedDate = GETDATE() WHERE RequestId = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, note);
            ps.setInt(3, requestId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Database error in updateRequestStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Error in updateRequestStatus: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
