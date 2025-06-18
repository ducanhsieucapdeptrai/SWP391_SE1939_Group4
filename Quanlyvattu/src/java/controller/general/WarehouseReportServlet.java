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
            // Lấy ID từ parameter
            String requestIdStr = request.getParameter("requestId");
            int requestId;
            
            // Kiểm tra parameter
            if (requestIdStr != null && !requestIdStr.trim().isEmpty()) {
                requestId = Integer.parseInt(requestIdStr);
            } else {
                // Nếu không có parameter, sử dụng giá trị mặc định
                requestId = 1;
            }
            
            // Lấy thông tin request
            RequestList requestInfo = requestDAO.getRequestById(requestId);
            if (requestInfo == null) {
                request.setAttribute("errorMessage", "Không tìm thấy yêu cầu với ID: " + requestId);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            // Lấy chi tiết request
            List<RequestDetail> requestDetails = requestDAO.getRequestDetailsByRequestId(requestId);
            
            // Lấy danh sách import liên quan
            List<ImportList> relatedImports = requestDAO.getRelatedImportsByRequestId(requestId);
            
            // Lấy chi tiết import liên quan
            List<ImportDetail> relatedImportDetails = requestDAO.getRelatedImportDetailsByRequestId(requestId);
            
            // Set attributes cho JSP
            request.setAttribute("requestInfo", requestInfo);
            request.setAttribute("requestDetails", requestDetails);
            request.setAttribute("relatedImports", relatedImports);
            request.setAttribute("relatedImportDetails", relatedImportDetails);
            
            // Forward đến JSP
            request.getRequestDispatcher("WarehouseStaffReport.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID yêu cầu không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
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
