package controller.general;

import DAO.RequestDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.RequestDetail;
import model.Users;

public class ApproveAndRejectRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String requestIdStr = request.getParameter("requestId");

        if (requestIdStr == null || requestIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing request ID.");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
            return;
        }

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }

        int approvedBy = user.getUserId();
        RequestDetailDAO dao = new RequestDetailDAO();

        if ("approve".equalsIgnoreCase(action)) {
            String note = request.getParameter("note");
            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");

            List<RequestDetail> addedDetails = new ArrayList<>();
            if (materialIds != null && quantities != null && materialIds.length == quantities.length) {
                for (int i = 0; i < materialIds.length; i++) {
                    try {
                        int materialId = Integer.parseInt(materialIds[i]);
                        int quantity = Integer.parseInt(quantities[i]);

                        RequestDetail detail = new RequestDetail();
                        detail.setMaterialId(materialId);
                        detail.setQuantity(quantity);
                        addedDetails.add(detail);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            dao.approveRequest(requestId, note != null ? note.trim() : "", approvedBy, addedDetails);

        } else if ("reject".equalsIgnoreCase(action)) {
            String reason = request.getParameter("reason");
            if (reason == null || reason.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rejection reason is required.");
                return;
            }
            dao.rejectRequest(requestId, reason.trim(), approvedBy);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        response.sendRedirect("reqlist");
    }

    @Override
    public String getServletInfo() {
        return "Handles approval and rejection of material requests including additional materials";
    }
}
