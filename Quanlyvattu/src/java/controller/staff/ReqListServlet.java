package controller.staff;

import DAO.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.RequestList;

@WebServlet("/reqlist")
public class ReqListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDAO dao = new RequestDAO();
        List<RequestList> list = dao.getAllRequests();
        request.setAttribute("requestList", list);
        request.setAttribute("pageContent", "/reqlist.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

    }
}
