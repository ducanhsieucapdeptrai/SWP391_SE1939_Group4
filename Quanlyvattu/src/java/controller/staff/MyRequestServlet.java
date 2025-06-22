package controller.staff;

import DAO.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.RequestList;

@WebServlet("/my-request")
public class MyRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String statusFilter = request.getParameter("status");
        String poStatusFilter = request.getParameter("poStatus");

        RequestDAO dao = new RequestDAO();
        List<RequestList> list = dao.getSimpleRequestsByUserFiltered(userId, statusFilter, poStatusFilter);

        request.setAttribute("myRequestList", list);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("poStatusFilter", poStatusFilter);
        request.setAttribute("pageContent", "/View/CompanyStaff/my-request.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
