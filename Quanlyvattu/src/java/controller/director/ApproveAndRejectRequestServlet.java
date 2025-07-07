package controller.director;

import DAO.RequestDetailDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.RequestDetail;
import model.Users;

@WebServlet(name = "ApproveAndRejectRequestServlet", urlPatterns = {"/approveandrejectrequest"})
public class ApproveAndRejectRequestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Only Directors or Warehouse Managers can approve or reject requests.");
            return;
        }

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
        int approvedBy = user.getUserId();

        RequestDetailDAO dao = new RequestDetailDAO();

        if ("approve".equalsIgnoreCase(action)) {
            String note = request.getParameter("note");
            String[] materialIds = request.getParameterValues("materialId[]");
            String[] quantities = request.getParameterValues("quantity[]");

            Map<Integer, Integer> materialMap = new LinkedHashMap<>();

            if (materialIds != null && quantities != null && materialIds.length == quantities.length) {
                for (int i = 0; i < materialIds.length; i++) {
                    try {
                        int materialId = Integer.parseInt(materialIds[i].trim());
                        int quantity = Integer.parseInt(quantities[i].trim());

                        if (materialId > 0 && quantity > 0) {
                            materialMap.put(materialId, materialMap.getOrDefault(materialId, 0) + quantity);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }

            List<RequestDetail> addedDetails = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : materialMap.entrySet()) {
                RequestDetail detail = new RequestDetail();
                detail.setMaterialId(entry.getKey());
                detail.setQuantity(entry.getValue());
                addedDetails.add(detail);
            }

            dao.approveRequest(requestId, note != null ? note.trim() : "", approvedBy, addedDetails);
            session.setAttribute("successMessage", "Request approved successfully!");

        } else if ("reject".equalsIgnoreCase(action)) {
            String reason = request.getParameter("reason");
            if (reason == null || reason.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rejection reason is required.");
                return;
            }

            dao.rejectRequest(requestId, reason.trim(), approvedBy);
            session.setAttribute("successMessage", "Request rejected successfully!");

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        response.sendRedirect("reqlist?success=true");
    }

    @Override
    public String getServletInfo() {
        return "Handles approval and rejection of material requests including additional materials.";
    }
}
