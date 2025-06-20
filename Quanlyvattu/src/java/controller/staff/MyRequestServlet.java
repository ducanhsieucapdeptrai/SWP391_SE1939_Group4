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
        String statusFilter = request.getParameter("status"); // lấy filter từ query

        RequestDAO dao = new RequestDAO();
        List<RequestList> list;

        if (statusFilter != null && !statusFilter.isEmpty()) {
            list = dao.getSimpleRequestsByUserAndStatus(userId, statusFilter);
        } else {
            list = dao.getSimpleRequestsByUser(userId);
        }

        request.setAttribute("myRequestList", list);
        request.setAttribute("pageContent", "/my-request.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

}
