package controller.staff;

import DAO.RequestDetailDAO;
import DAO.ApprovedRequestDAO;
import model.RequestDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import model.RequestList;

@WebServlet("/approvedrequestdetail")
public class ApprovedRequestDetailServlet extends HttpServlet {

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

            RequestDetailDAO dao = new RequestDetailDAO();
            List<RequestDetail> details = dao.getRequestDetailsByRequestId(requestId);

            ApprovedRequestDAO requestDao = new ApprovedRequestDAO();
            RequestList requestList = requestDao.getRequestById(requestId);

            if (requestList != null) {
                request.setAttribute("filterRequestDate", requestList.getRequestDate());
            }

            request.setAttribute("requestDetails", details);
            request.setAttribute("requestId", requestId);

            if (details == null || details.isEmpty()) {
                request.setAttribute("message", "Không có vật tư nào trong yêu cầu này (ID: " + requestId + ")");
            }

            request.setAttribute("pageContent", "/approvedrequestdetail.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error in ApprovedRequestDetailServlet (GET): " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error: " + e.getMessage());
        }
    }



@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("requestId");
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            int staffId = (int) session.getAttribute("userId");

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

            // Assign request to staff
            ApprovedRequestDAO dao = new ApprovedRequestDAO();
            boolean success = dao.assignRequestToStaff(requestId, staffId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/mytasks"); // Assuming this is the servlet for My Tasks
            } else {
                request.setAttribute("error", "Không thể nhận việc. Vui lòng thử lại.");
                doGet(request, response); // reload page with error
            }

        } catch (Exception e) {
            System.out.println("Error in ApprovedRequestDetailServlet (POST): " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error: " + e.getMessage());
        }
    }
}
