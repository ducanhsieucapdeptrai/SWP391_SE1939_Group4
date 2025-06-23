package controller.staff;

import DAO.RequestDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.RequestList;

@WebServlet("/updated-requests")
public class UpdatedRequestsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDAO dao = new RequestDAO();
        List<RequestList> updatedRequests = dao.getUpdatedRequests();

        request.setAttribute("requestList", updatedRequests);
        request.setAttribute("pageContent", "/updatedRequests.jsp"); // for layout.jsp
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
