package controller.staff;

import DAO.ApprovedRequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RequestList;

import java.io.IOException;
import java.util.List;

@WebServlet("/approvedrequests")
public class ApprovedRequestListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("requestType");
        String requestedBy = request.getParameter("requestedBy");
        String requestDate = request.getParameter("requestDate");

        ApprovedRequestDAO dao = new ApprovedRequestDAO();

        // Lấy danh sách request đã duyệt & chưa ai nhận
        List<RequestList> list = dao.getApprovedRequests(type, requestedBy, requestDate);

        // Lấy danh sách các loại request (Import, Export, Repair, Purchase)
        List<String> requestTypes = dao.getAllRequestTypes();

        // Truyền dữ liệu ra JSP
        request.setAttribute("approvedRequestList", list);
        request.setAttribute("requestTypes", requestTypes);

        // Giữ lại filter đã chọn
        request.setAttribute("filterType", type);
        request.setAttribute("filterRequestedBy", requestedBy);
        request.setAttribute("filterRequestDate", requestDate);

        request.setAttribute("pageContent", "/approvedrequests.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
