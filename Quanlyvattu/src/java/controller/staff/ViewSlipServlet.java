package controller.staff;

import DAO.TaskLogDAO;
import model.TaskLog;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import dal.DBContext;

@WebServlet("/viewSlip")
public class ViewSlipServlet extends HttpServlet {
    private final TaskLogDAO taskLogDAO = new TaskLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String taskIdStr = request.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.isEmpty()) {
            response.sendRedirect("tasklist");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            int taskId = Integer.parseInt(taskIdStr);
            TaskLog log = taskLogDAO.getTaskLogByTaskId(conn, taskId);
            if (log == null) {
                request.setAttribute("errorMessage", "Slip not found.");
                request.setAttribute("pageContent", "tasklist.jsp");
                request.getRequestDispatcher("layout/layout.jsp").forward(request, response);
                return;
            }

            request.setAttribute("requestId", log.getRequestId());
            request.setAttribute("requestType", log.getRequestTypeName());
            request.setAttribute("taskLog", log);
            request.setAttribute("signDate", new Date());
            request.setAttribute("staffName", log.getStaffName());
            request.setAttribute("pageContent", "/viewSlip.jsp");

            request.getRequestDispatcher("layout/layout.jsp").forward(request, response);
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading slip.");
            request.setAttribute("pageContent", "tasklist.jsp");
            request.getRequestDispatcher("layout/layout.jsp").forward(request, response);
        }
    }
}
