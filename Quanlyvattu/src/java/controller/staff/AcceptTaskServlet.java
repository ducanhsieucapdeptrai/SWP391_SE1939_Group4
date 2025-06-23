package controller.staff;

import DAO.RequestDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;

import java.io.IOException;

@WebServlet("/accept-task")
public class AcceptTaskServlet extends HttpServlet {
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        request.setAttribute("pageContent", "/approvedrequestdetail.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String requestType = request.getParameter("requestType");

            HttpSession session = request.getSession();
            Users currentUser = (Users) session.getAttribute("user");

            if (currentUser != null) {
                int userId = currentUser.getUserId();

                RequestDAO dao = new RequestDAO();
                if ("Import".equalsIgnoreCase(requestType)) {
                    dao.assignImportTask(requestId, userId);
                } else if ("Export".equalsIgnoreCase(requestType)) {
                    dao.assignExportTask(requestId, userId);
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log lỗi nếu requestId không đúng định dạng
        }

        response.sendRedirect("approved-request-list");
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý nhân viên kho nhận nhiệm vụ từ yêu cầu đã được duyệt";
    }
}
