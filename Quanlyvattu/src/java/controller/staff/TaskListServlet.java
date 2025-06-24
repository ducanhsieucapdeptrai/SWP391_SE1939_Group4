package controller.staff;

import DAO.AssignTaskDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RequestList;

import java.io.IOException;
import java.util.List;

@WebServlet("/tasklist")
public class TaskListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("requestType");
        String requestedBy = request.getParameter("requestedBy");
        String requestDate = request.getParameter("requestDate");
        String view = request.getParameter("view"); // "ongoing" hoặc "upcoming" (mặc định là "ongoing")

        if (view == null || view.isEmpty()) {
            view = "ongoing";
        }

        AssignTaskDAO dao = new AssignTaskDAO();
        List<RequestList> list;

        // Gọi DAO phù hợp với bộ lọc
        if ("upcoming".equals(view)) {
            list = dao.getUpcomingTasksFiltered(type, requestedBy, requestDate);
        } else {
            list = dao.getOngoingTasksTodayFiltered(type, requestedBy, requestDate);
        }

        // Danh sách loại yêu cầu
        List<String> requestTypes = dao.getAllRequestTypes();

        // Gửi dữ liệu cho JSP
        request.setAttribute("approvedRequestList", list);
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("filterType", type);
        request.setAttribute("filterRequestedBy", requestedBy);
        request.setAttribute("filterRequestDate", requestDate);
        request.setAttribute("activeView", view);

        request.setAttribute("pageContent", "/tasklist.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}