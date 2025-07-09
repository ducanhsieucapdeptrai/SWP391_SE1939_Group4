package controller.staff;

import DAO.RequestDAO;
import model.RequestList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/completedTasks")
public class CompletedTaskServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        try {
            RequestDAO dao = new RequestDAO();
            List<RequestList> completedList = dao.getCompletedRequests();
            request.setAttribute("completedList", completedList);
            request.getRequestDispatcher("/taskCompletedList.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
