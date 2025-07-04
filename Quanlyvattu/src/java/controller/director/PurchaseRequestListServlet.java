package controller.director;

import DAO.PurchaseOrderDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;
import model.PurchaseOrderList;

@WebServlet("/purchase-request-list")
public class PurchaseRequestListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.hasAnyRole(request, "Director", "Warehouse Manager")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        String status = request.getParameter("status");
        String createdDate = request.getParameter("createdDate");
        String createdByName = request.getParameter("createdByName");
        String pageParam = request.getParameter("page");

        int pageSize = 5;
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        int offset = (currentPage - 1) * pageSize;

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        int totalRecords = dao.countFilteredPurchaseOrders(status, createdDate, createdByName);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
            offset = (currentPage - 1) * pageSize;
        }

        if (currentPage < 1) {
            currentPage = 1;
            offset = 0;
        }

        List<PurchaseOrderList> poList = dao.getFilteredPurchaseOrdersPaged(status, createdDate, createdByName, offset, pageSize);

        request.setAttribute("poList", poList);
        request.setAttribute("filterStatus", status);
        request.setAttribute("filterDate", createdDate);
        request.setAttribute("filterCreatedByName", createdByName);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageContent", "/View/Director/purchase-request-list.jsp");

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
