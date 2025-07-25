package controller.general;

import DAO.RequestDetailDAO;
import DAO.MaterialDAO;
import DAO.RequestDAO;
import DAO.RepairOrderDAO;
import model.Material;
import model.RequestDetail;
import model.RequestList;
import model.RepairOrderDetail;
import model.Users;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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

            int requestId = Integer.parseInt(idParam.trim());
            RequestDAO requestDAO = new RequestDAO();
            RequestList requestInfo = requestDAO.getRequestById(requestId); // đã tương thích

            RequestDetailDAO dao = new RequestDetailDAO();
            List<RequestDetail> details = dao.getRequestDetailsByRequestId(requestId);
            String status = dao.getRequestStatus(requestId);

            MaterialDAO materialDAO = new MaterialDAO();
            List<Material> allMaterials = materialDAO.getAllMaterials();

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

            int requestId = Integer.parseInt(requestIdParam.trim());
            Users currentUser = (Users) request.getSession().getAttribute("currentUser");
            int approverId = (currentUser != null) ? currentUser.getUserId() : -1;

            if ("approve".equals(action)) {
                handleApprove(request, response, requestId, approverId);
            } else if ("reject".equals(action)) {
                handleReject(request, response, requestId, approverId);
            } else {
                request.setAttribute("errorMessage", "Invalid action");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while processing your request");
            doGet(request, response);
        }
    }

    private void handleApprove(HttpServletRequest request, HttpServletResponse response, int requestId, int approverId)
            throws ServletException, IOException {

        String note = request.getParameter("note");
        if (note == null) note = "";

        try {
            boolean success = updateRequestStatus(requestId, "Approved", note, approverId);

            if (success) {
                RequestDAO requestDAO = new RequestDAO();
                RequestList req = requestDAO.getRequestById(requestId);

                if (req.getRequestTypeName().equalsIgnoreCase("Purchase")) {
                    // TODO: Nếu cần xử lý Import New Purchase sau approve Purchase
                } else if (req.getRequestTypeName().equalsIgnoreCase("Repair")) {
                    RepairOrderDAO roDAO = new RepairOrderDAO();
                    List<RepairOrderDetail> details = roDAO.getRepairPreviewByRequest(requestId);
                    int roId = roDAO.insertRepairOrderWithStatus(requestId, req.getRequestedBy(), note, "Pending");

                    for (RepairOrderDetail d : details) {
                        roDAO.insertRepairDetail(roId, d);
                    }
                    System.out.println("✅ Repair Order #" + roId + " created from Request #" + requestId);
                }

                request.setAttribute("successMessage", "Request has been approved successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to approve request. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while approving the request");
        }

        response.sendRedirect("request-detail?id=" + requestId);
    }

    private void handleReject(HttpServletRequest request, HttpServletResponse response, int requestId, int approverId)
            throws ServletException, IOException {

        String reason = request.getParameter("reason");
        if (reason == null || reason.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Reason for rejection is required");
            doGet(request, response);
            return;
        }

        try {
            boolean success = updateRequestStatus(requestId, "Rejected", reason, approverId);

            if (success) {
                request.setAttribute("successMessage", "Request has been rejected successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to reject request. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while rejecting the request");
        }

        response.sendRedirect("request-detail?id=" + requestId);
    }

    private boolean updateRequestStatus(int requestId, String status, String note, int approverId) {
        String sql = "UPDATE RequestList SET Status = ?, ApprovalNote = ?, ApprovedDate = NOW(), ApprovedBy = ? WHERE RequestId = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, note);
            ps.setInt(3, approverId);
            ps.setInt(4, requestId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
