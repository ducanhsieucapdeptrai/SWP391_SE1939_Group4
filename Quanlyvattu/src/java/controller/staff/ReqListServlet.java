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
        String type = request.getParameter("requestType");
        String status = request.getParameter("status");
        String requestedBy = request.getParameter("requestedBy");
        String requestDate = request.getParameter("requestDate");

        RequestDAO dao = new RequestDAO();
        List<RequestList> list = dao.getFilteredRequests(type, status, requestedBy, requestDate);
        List<String> requestTypes = dao.getAllRequestTypes();
        request.setAttribute("requestTypes", requestTypes);

        request.setAttribute("requestList", list);
        request.setAttribute("pageContent", "/reqlist.jsp");

        // Giữ lại giá trị filter để hiển thị lại sau khi lọc
        request.setAttribute("filterType", type);
        request.setAttribute("filterStatus", status);
        request.setAttribute("filterRequestedBy", requestedBy);
        request.setAttribute("filterRequestDate", requestDate);

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

    }
}
