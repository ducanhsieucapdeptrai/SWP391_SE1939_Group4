package controller.general;

import DAO.WarehouseReportDAO;
import model.RequestList;
import model.RequestDetail;
import model.ImportList;
import model.ImportDetail;
import model.*;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/warehousereport")
public class WarehouseReportServlet extends HttpServlet {
    
    private WarehouseReportDAO requestDAO;
    
    @Override
    public void init() throws ServletException {
        requestDAO = new WarehouseReportDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        isWarehouseStaff(request);
        try {
            // Get ID from parameter
            String requestIdStr = request.getParameter("requestId");
            int requestId;
            
            // Check parameter
            if (requestIdStr != null && !requestIdStr.trim().isEmpty()) {
                requestId = Integer.parseInt(requestIdStr);
            } else {
                // If no parameter, use default value
                requestId = 1;
            }
            
            // Get request info
            RequestList requestInfo = requestDAO.getRequestById(requestId);
            if (requestInfo == null) {
                request.setAttribute("errorMessage", "Request not found with ID: " + requestId);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Get request details
            List<RequestDetail> requestDetails = requestDAO.getRequestDetailsByRequestId(requestId);
            
            // Get related import list
            List<ImportList> relatedImports = requestDAO.getRelatedImportsByRequestId(requestId);
            
            // Get related import details
            List<ImportDetail> relatedImportDetails = requestDAO.getRelatedImportDetailsByRequestId(requestId);
            
            // Set attributes for JSP
            request.setAttribute("requestId", requestId);
            request.setAttribute("requestInfo", requestInfo);
            request.setAttribute("requestDetails", requestDetails);
            request.setAttribute("relatedImports", relatedImports);
            request.setAttribute("relatedImportDetails", relatedImportDetails);
            
            // Forward to JSP
           request.setAttribute("pageContent",   "/WarehouseStaffReport.jsp");
           
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid request ID: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean isWarehouseStaff(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userRole = (String) session.getAttribute("userRole");
        if (userRole != null) {
            return "warehouse_staff".equals(userRole) || "Warehouse Staff".equals(userRole);
        }
        return false;
    }
}
