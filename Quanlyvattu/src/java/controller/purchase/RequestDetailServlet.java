package controller.purchase;

import DAO.RequestDAO;
import DAO.MaterialDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.util.List;
import model.Material;
import model.RequestDetail;
import model.RequestList;

public class RequestDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("id"));
        RequestList info = RequestDAO.getRequestById(requestId);
        List<RequestDetail> details = RequestDAO.getRequestDetails(requestId);

        MaterialDAO materialDao = new MaterialDAO();
        List<Material> materialList = materialDao.getAllMaterials();

        request.setAttribute("requestInfo", info);
        request.setAttribute("details", details);
        request.setAttribute("materialList", materialList);
        request.setAttribute("pageContent", "/request-detail.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int requestId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        int approverId = (int) session.getAttribute("userId");

        if ("approve".equals(action)) {
            // Lấy mảng materialIds và quantities do user chỉnh sửa
            String[] materialIds = request.getParameterValues("materialIds");
            String[] quantities = request.getParameterValues("quantities");
            String approveNote = request.getParameter("approveNote");

            // Gọi DAO xử lý duyệt và lưu chi tiết mới
            RequestDAO.approveRequest(
                    requestId,
                    approverId,
                    approveNote,
                    materialIds,
                    quantities
            );

        } else if ("reject".equals(action)) {
            String reason = request.getParameter("reason");
            RequestDAO.rejectRequest(requestId, approverId, reason);
        }

        response.sendRedirect(request.getContextPath() + "/pending-requests");
    }

    @Override
    public String getServletInfo() {
        return "RequestDetailServlet handles viewing, approving and rejecting material requests";
    }
}
