
package controller.director;

import model.RepairOrder;
import DAO.RepairOrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/repair-order-list")
public class RepairOrderListServlet extends HttpServlet {

    private final int pageSize = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read filter parameters
        String createdByName = request.getParameter("createdByName") == null ? "" : request.getParameter("createdByName");
        String status = request.getParameter("status") == null ? "" : request.getParameter("status");
        String createdDate = request.getParameter("createdDate") == null ? "" : request.getParameter("createdDate");

        int currentPage = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {
            // Default to page 1 if parsing fails
        }

        try {
            RepairOrderDAO dao = new RepairOrderDAO();
            List<RepairOrder> repairList = dao.getRepairOrders(createdByName, status, createdDate, currentPage, pageSize);
            int totalCount = dao.countRepairOrders(createdByName, status, createdDate);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            request.setAttribute("repairList", repairList);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);

            request.setAttribute("filterCreatedByName", createdByName);
            request.setAttribute("filterStatus", status);
            request.setAttribute("filterDate", createdDate);

            request.setAttribute("pageContent", "/View/Director/repair-order-list.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading repair orders.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}



