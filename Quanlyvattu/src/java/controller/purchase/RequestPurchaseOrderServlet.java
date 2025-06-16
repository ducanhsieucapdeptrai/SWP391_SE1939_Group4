package controller.purchase;

import DAO.RequestDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import model.RequestList;

import java.io.IOException;
import java.util.List;

public class RequestPurchaseOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        // Kiểm tra đăng nhập
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lọc trạng thái (nếu có)
        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) {
            status = "All"; // Hiển thị tất cả nếu không chọn trạng thái
        }

        // Lấy danh sách yêu cầu vật tư theo userId và trạng thái
        List<RequestList> requests = RequestDAO.getRequestsByUserAndStatus(userId, status);

        // Gửi dữ liệu ra view
        request.setAttribute("requests", requests);
        request.setAttribute("selectedStatus", status);
        request.setAttribute("pageContent", "/View/CompanyStaff/my-requests.jsp"); // phần nội dung chính
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
