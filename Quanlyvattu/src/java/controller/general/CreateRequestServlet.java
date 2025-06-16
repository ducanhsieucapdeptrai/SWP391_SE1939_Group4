/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.general;

import DAO.RequestDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Material;
import model.RequestDetail;
import model.RequestType;
import model.Users;

/**
 *
 * @author Admin
 */
@WebServlet(name="CreateRequestServlet", urlPatterns={"/createrequest"})
public class CreateRequestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
            // Kiểm tra session user
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        RequestDAO dao = new RequestDAO();
        
        // Lấy danh sách loại yêu cầu và vật tư
        List<RequestType> requestTypes = dao.getAllRequestTypes();
        List<Material> materials = dao.getAllMaterials();
        
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("materials", materials);
      
        
         request.setAttribute("pageContent", "/createRequest.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // Kiểm tra session user
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Lấy thông tin từ form
            int requestTypeId = Integer.parseInt(request.getParameter("requestTypeId"));
            String note = request.getParameter("note");
            
            // Lấy danh sách vật tư và số lượng
            String[] materialIds = request.getParameterValues("materialId");
            String[] quantities = request.getParameterValues("quantity");
            
            // Validate input
            if (materialIds == null || quantities == null || materialIds.length != quantities.length) {
                request.setAttribute("error", "Vui lòng chọn ít nhất một vật tư và nhập số lượng hợp lệ!");
                doGet(request, response);
                return;
            }
            
            // Tạo danh sách chi tiết yêu cầu
            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialIds.length; i++) {
                int materialId = Integer.parseInt(materialIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                
                if (quantity <= 0) {
                    request.setAttribute("error", "Số lượng phải lớn hơn 0!");
                    doGet(request, response);
                    return;
                }
                
                RequestDetail detail = new RequestDetail();
                detail.setMaterialId(materialId);
                detail.setQuantity(quantity);
                details.add(detail);
            }
            
            // Tạo yêu cầu
            RequestDAO dao = new RequestDAO();
            boolean success = dao.createRequest(user.getUserId(), requestTypeId, note, details);
            
            if (success) {
                request.setAttribute("success", "Tạo yêu cầu thành công! Yêu cầu đã được gửi đến Giám đốc để phê duyệt.");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo yêu cầu!");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ!");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        doGet(request, response);
    }
}