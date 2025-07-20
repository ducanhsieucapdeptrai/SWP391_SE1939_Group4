// === CompletedTaskServlet.java ===
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

        String type = request.getParameter("type");
        String requestedBy = request.getParameter("requestedBy");
        String createdDateStr = request.getParameter("createdDate");
        String finishDateStr = request.getParameter("finishDate");
        String sortBy = request.getParameter("sortBy");

        String sortColumn = "finishedDate"; // default
        String sortDirection = "desc";      // default

        if (sortBy != null) {
            switch (sortBy) {
                case "requestId_asc" -> {
                    sortColumn = "requestId";
                    sortDirection = "asc";
                }
                case "requestId_desc" -> {
                    sortColumn = "requestId";
                    sortDirection = "desc";
                }
                case "requestDate_asc" -> {
                    sortColumn = "requestDate";
                    sortDirection = "asc";
                }
                case "requestDate_desc" -> {
                    sortColumn = "requestDate";
                    sortDirection = "desc";
                }
                case "finishedDate_asc" -> {
                    sortColumn = "finishedDate";
                    sortDirection = "asc";
                }
                case "finishedDate_desc" -> {
                    sortColumn = "finishedDate";
                    sortDirection = "desc";
                }
            }
        }

        String pageStr = request.getParameter("page");
        int page = 1;
        int pageSize = 10;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date createdDate = null;
        Date finishDate = null;

        try {
            if (createdDateStr != null && !createdDateStr.isEmpty()) {
                createdDate = new Date(sdf.parse(createdDateStr).getTime());
            }
            if (finishDateStr != null && !finishDateStr.isEmpty()) {
                finishDate = new Date(sdf.parse(finishDateStr).getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RequestDAO dao = new RequestDAO();
        List<RequestList> completedList = dao.getCompletedRequestsFiltered(
            type, requestedBy, createdDate, finishDate,
            sortColumn, sortDirection, offset, pageSize
        );

        int totalRecords = dao.countCompletedRequestsFiltered(
            type, requestedBy, createdDate, finishDate
        );
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Gửi dữ liệu sang JSP
        request.setAttribute("completedList", completedList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("type", type);
        request.setAttribute("requestedBy", requestedBy);
        request.setAttribute("createdDate", createdDateStr);
        request.setAttribute("finishDate", finishDateStr);
        request.setAttribute("sortBy", sortBy); // ✅ giữ lại cho dropdown và phân trang

        request.getRequestDispatcher("taskCompletedList.jsp").forward(request, response);
    }
}
