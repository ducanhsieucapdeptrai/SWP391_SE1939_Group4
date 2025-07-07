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

        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        String type = request.getParameter("requestType");
        String status = request.getParameter("status");
        String requestedBy = request.getParameter("requestedBy");
        String requestDate = request.getParameter("requestDate");

        if (requestDate == null) {
            requestDate = "";
        }

        RequestDAO dao = new RequestDAO();

        List<RequestList> list = dao.getFilteredRequestsByPage(type, status, requestedBy, requestDate, offset, pageSize);
        int totalCount = dao.countFilteredRequests(type, status, requestedBy, requestDate);
        int totalPage = Math.max(1, (int) Math.ceil((double) totalCount / pageSize));

        int startPage = Math.max(1, page - 1);
        int endPage = Math.min(totalPage, startPage + 2);
        if (endPage - startPage < 2) {
            startPage = Math.max(1, endPage - 2);
        }

        StringBuilder baseParams = new StringBuilder();
        if (type != null && !type.isEmpty()) {
            baseParams.append("&requestType=").append(type);
        }
        if (status != null && !status.isEmpty()) {
            baseParams.append("&status=").append(status);
        }
        if (requestedBy != null && !requestedBy.isEmpty()) {
            baseParams.append("&requestedBy=").append(requestedBy);
        }
        if (!requestDate.isEmpty()) {
            baseParams.append("&requestDate=").append(requestDate);
        }

        request.setAttribute("requestList", list);
        request.setAttribute("requestTypes", dao.getAllRequestTypes());
        request.setAttribute("filterType", type);
        request.setAttribute("filterStatus", status);
        request.setAttribute("filterRequestedBy", requestedBy);
        request.setAttribute("filterRequestDate", requestDate);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("baseParams", baseParams.toString());

        request.setAttribute("pageContent", "/reqlist.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
