package controller.staff;

import DAO.ApprovedRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import model.RequestList;
import model.Users;

@WebServlet(name = "MyTaskServlet", urlPatterns = {"/mytasks"})
public class MyTaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        if (currentUser != null) {
            int staffId = currentUser.getUserId();

            String type = request.getParameter("type");
            String requestDate = request.getParameter("requestDate");

            ApprovedRequestDAO dao = new ApprovedRequestDAO(); // ✅ đổi DAO
            List<RequestList> myTasks = dao.getAssignedRequestsWithFilters(staffId, type, requestDate);
            List<String> requestTypes = dao.getAllRequestTypeNames(); // ✅ load loại yêu cầu

            request.setAttribute("myTasks", myTasks);
            request.setAttribute("requestTypes", requestTypes);

            request.setAttribute("filterType", type);
            request.setAttribute("filterRequestDate", requestDate);

            request.setAttribute("pageContent", "/mytask.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays tasks assigned to the current warehouse staff";
    }
}
