package controller.staff;

import DAO.RequestDAO;
import Helper.AuthorizationHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RequestList;

import java.io.IOException;
import java.util.List;

@WebServlet("/my-request")
public class MyRequestServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthorizationHelper.isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");

        String statusFilter = request.getParameter("status");
        String poStatusFilter = request.getParameter("poStatus");
        String typeFilter = request.getParameter("type");

        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {
        }

        RequestDAO dao = new RequestDAO();
        int totalRecords = dao.countRequestsByUserWithFilters(userId, statusFilter, poStatusFilter, typeFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        if (page < 1) {
            page = 1;
        }
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int offset = (page - 1) * PAGE_SIZE;
        List<RequestList> list = dao.getPagedRequestsByUserFiltered(userId, statusFilter, poStatusFilter, typeFilter, offset, PAGE_SIZE);

        request.setAttribute("myRequestList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("poStatusFilter", poStatusFilter);
        request.setAttribute("typeFilter", typeFilter);
        request.setAttribute("pageContent", "/View/CompanyStaff/my-request.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
