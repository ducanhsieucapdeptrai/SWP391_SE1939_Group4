package controller.director;

import DAO.RepairOrderDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RepairOrderList;

import java.io.IOException;
import java.util.List;

@WebServlet("/repair-request-list")
public class RepairRequestListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check quyền
        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        // Lấy filter từ request
        String status = request.getParameter("status");
        String createdDate = request.getParameter("createdDate");
        String createdByName = request.getParameter("createdByName");
        String pageParam = request.getParameter("page");

        int pageSize = 5;
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {}
        }

        int offset = (currentPage - 1) * pageSize;

        // Gọi DAO
        RepairOrderDAO dao = new RepairOrderDAO();
        int totalRecords = dao.countFilteredRepairOrders(status, createdDate, createdByName);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
            offset = (currentPage - 1) * pageSize;
        }
        if (currentPage < 1) {
            currentPage = 1;
            offset = 0;
        }

        // Lấy danh sách theo phân trang
        List<RepairOrderList> roList = dao.getFilteredRepairOrdersPaged(status, createdDate, createdByName, offset, pageSize);

        // Đặt attribute cho JSP
        request.setAttribute("roList", roList);
        request.setAttribute("filterStatus", status);
        request.setAttribute("filterDate", createdDate);
        request.setAttribute("filterCreatedByName", createdByName);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageContent", "/View/Director/repair-request-list.jsp");

        // Forward
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
