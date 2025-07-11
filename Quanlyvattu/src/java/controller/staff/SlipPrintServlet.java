package controller.staff;

import DAO.RequestDAO;
import DAO.TaskLogDAO;
import model.RequestDetailItem;
import model.TaskLog;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dal.DBContext;

@WebServlet("/printSlip")
public class SlipPrintServlet extends HttpServlet {
    private final TaskLogDAO taskLogDAO = new TaskLogDAO();
    private final RequestDAO requestDAO = new RequestDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object user = session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String requestIdStr = request.getParameter("requestId");
        String taskIdStr = request.getParameter("taskId");

        try (Connection conn = new DBContext().getConnection()) {
            TaskLog log = null;
            int requestId;

            // Trường hợp reprint: có taskId
            if (taskIdStr != null && !taskIdStr.isEmpty()) {
                int taskId = Integer.parseInt(taskIdStr);
                log = taskLogDAO.getTaskLogByTaskId(conn, taskId);
                if (log == null) {
                    session.setAttribute("errorMessage", "Slip log not found.");
                    response.sendRedirect("tasklist");
                    return;
                }
                requestId = log.getRequestId();
            }
            // Trường hợp print mới: có requestId
            else if (requestIdStr != null && !requestIdStr.isEmpty()) {
                requestId = Integer.parseInt(requestIdStr);
                log = taskLogDAO.getLatestTaskLogByRequestId(conn, requestId);
                if (log == null) {
                    session.setAttribute("errorMessage", "No slip found to print.");
                    response.sendRedirect("taskUpdate?requestId=" + requestId);
                    return;
                }
            }
            else {
                response.sendRedirect("tasklist");
                return;
            }

            List<RequestDetailItem> items = requestDAO.getRequestDetails(requestId);
            if (items.isEmpty()) {
                session.setAttribute("errorMessage", "Request details not found.");
                response.sendRedirect("taskUpdate?requestId=" + requestId);
                return;
            }

            // Truyền dữ liệu sang JSP
            request.setAttribute("requestId", requestId);
            request.setAttribute("taskLog", log);
            request.setAttribute("requestType", log.getRequestTypeName());
            request.setAttribute("note", items.get(0).getNote());
            request.setAttribute("staffName", log.getStaffName());
            request.setAttribute("signDate", new Date());

            // Luôn render qua layout.jsp
            request.setAttribute("pageContent", "/slip.jsp");
            request.getRequestDispatcher("layout/layout.jsp").forward(request, response);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error loading slip.");
            response.sendRedirect("tasklist");
        }
    }
}
