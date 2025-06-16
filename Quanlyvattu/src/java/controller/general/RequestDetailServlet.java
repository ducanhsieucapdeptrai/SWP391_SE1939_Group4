package controller.general;

import DAO.RequestDetailDAO;
import model.RequestDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

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
            
            RequestDetailDAO dao = new RequestDetailDAO();
            List<RequestDetail> details = dao.getRequestDetailsByRequestId(requestId);
            
            request.setAttribute("requestDetails", details);
            request.setAttribute("requestId", requestId);
            
            if (details == null || details.isEmpty()) {
                request.setAttribute("message", "Không có vật tư nào trong yêu cầu này (ID: " + requestId + ")");
            }
            
            request.getRequestDispatcher("request-detail.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Log the error for debugging
            System.out.println("Error in RequestDetailServlet: " + e.getMessage());
            e.printStackTrace();
            
            // Send internal server error
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Internal server error: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}