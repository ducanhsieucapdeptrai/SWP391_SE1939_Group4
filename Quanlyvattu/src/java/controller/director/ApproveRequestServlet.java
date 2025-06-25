package controller.director;

import DAO.MaterialDAO;
import DAO.RequestDetailDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Material;
import model.RequestDetail;
import model.RequestList;
import model.Users;

@WebServlet(name = "ApproveRequestServlet", urlPatterns = {"/approve-request"})
public class ApproveRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");

        if (user == null || user.getRole() == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in.");
            return;
        }

        String roleName = user.getRole().getRoleName();
        if (!"Director".equalsIgnoreCase(roleName) && !"Warehouse Manager".equalsIgnoreCase(roleName)) {
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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID format.");
            return;
        }

        RequestDetailDAO requestDAO = new RequestDetailDAO();
        RequestList requestInfo = requestDAO.getRequestById(requestId);

        if (requestInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found.");
            return;
        }

        if (!"Pending".equalsIgnoreCase(requestInfo.getStatus()) && !"P".equalsIgnoreCase(requestInfo.getStatus())) {
            session.setAttribute("errorMessage", "This request has already been processed.");
            response.sendRedirect("request-detail?id=" + requestId);
            return;
        }

        List<RequestDetail> currentRequestDetails = requestDAO.getRequestDetailsByRequestId(requestId);

        MaterialDAO materialDAO = new MaterialDAO();
        List<Material> materialList = materialDAO.getAllMaterials();

        request.setAttribute("requestInfo", requestInfo);
        request.setAttribute("currentRequestDetails", currentRequestDetails);
        request.setAttribute("materialList", materialList);

        request.getRequestDispatcher("/View/Director/approve-request.jsp").forward(request, response);
    }
}
