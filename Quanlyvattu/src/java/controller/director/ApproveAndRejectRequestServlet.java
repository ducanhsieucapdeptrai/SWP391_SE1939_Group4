package controller.director;

import DAO.MaterialDAO;
import DAO.RequestDetailDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.*;

@WebServlet(name = "ApproveAndRejectRequestServlet", urlPatterns = {"/approveandrejectrequest"})
public class ApproveAndRejectRequestServlet extends HttpServlet {

    // Hiển thị form phê duyệt khi dùng method GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

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

        RequestDetailDAO requestDAO = new RequestDetailDAO();
        RequestList requestInfo = requestDAO.getRequestById(requestId);

        if (requestInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
            return;
        }

        if (!"Pending".equalsIgnoreCase(requestInfo.getStatus()) && !"P".equalsIgnoreCase(requestInfo.getStatus())) {
            request.getSession().setAttribute("errorMessage", "This request has already been processed.");
            response.sendRedirect("request-detail?id=" + requestId);
            return;
        }

        List<RequestDetail> currentRequestDetails = requestDAO.getRequestDetailsByRequestId(requestId);
        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materialList = materialDAO.getAllMaterials();

        request.setAttribute("requestInfo", requestInfo);
        request.setAttribute("currentRequestDetails", currentRequestDetails);
        request.setAttribute("materialList", materialList);

        request.setAttribute("pageContent", "/View/Director/approve-request.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied.");
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
                request.setAttribute("errorMessage", "Rejection reason is required.");
                request.getRequestDispatcher("request-detail?id=" + requestId).forward(request, response);
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
        return "Handles both GET (render approve view) and POST (approve/reject) actions for requests.";
    }
}
