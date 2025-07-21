package controller.general;

import DAO.RequestDAO;
import DAO.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.RequestDetail;
import model.Users;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreateRepairReqServlet", urlPatterns = {"/create-repair-request"})
public class CreateRepairReqServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check logged-in user
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        RequestDAO dao = new RequestDAO();
        req.setAttribute("categories", dao.getAllCategories());
        req.setAttribute("subCategories", dao.getAllSubCategories());
        req.setAttribute("requestTypes", dao.getAllRequestType());
        req.setAttribute("projectList", dao.getAllProjects());

        req.setAttribute("materials", dao.getAllMaterials());
        req.setAttribute("pageContent", "/createRepairRequest.jsp");
        req.getRequestDispatcher("/layout/layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            int requestTypeId = Integer.parseInt(req.getParameter("requestTypeId"));
            String note = req.getParameter("note");

            // ✅ Đọc projectId từ form (bạn cần đảm bảo form có input name="projectId")
            int projectId = Integer.parseInt(req.getParameter("projectId"));

            String[] materialIds = req.getParameterValues("materialId");
            String[] quantities = req.getParameterValues("quantity");

            if (materialIds == null || quantities == null || materialIds.length != quantities.length) {
                req.setAttribute("error", "Please select at least one material and enter a valid quantity.");
                doGet(req, resp);
                return;
            }

            boolean checkStock = (requestTypeId == 1 || requestTypeId == 4);
            RequestDAO dao = new RequestDAO();
            List<RequestDetail> details = new ArrayList<>();

            for (int i = 0; i < materialIds.length; i++) {
                int materialId = Integer.parseInt(materialIds[i]);
                int qty = Integer.parseInt(quantities[i]);
                if (qty <= 0) {
                    throw new NumberFormatException();
                }

                if (checkStock) {
                    int stock = dao.getMaterialStock(materialId);
                    if (qty > stock) {
                        req.setAttribute("error",
                                String.format("Material ID %d only has %d in stock, cannot request %d.",
                                        materialId, stock, qty));
                        doGet(req, resp);
                        return;
                    }
                }

                RequestDetail d = new RequestDetail();
                d.setMaterialId(materialId);
                d.setQuantity(qty);
                details.add(d);
            }

            // ✅ Gọi đúng hàm với projectId
            boolean success = dao.createRequest(user.getUserId(), requestTypeId, note, projectId, details);

            if (success) {
                req.setAttribute("success", "Your repair request has been created and sent for approval.");

                // 🔔 Gửi thông báo hệ thống
                try {
                    NotificationDAO notiDAO = new NotificationDAO();
                    String requestTypeName = dao.getRequestTypeName(requestTypeId);
                    notiDAO.createSystemRequestNotification(user.getFullName(), requestTypeName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                req.setAttribute("error", "An error occurred while creating your request.");
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid data format. Please check your inputs.");
        } catch (Exception e) {
            req.setAttribute("error", "Error: " + e.getMessage());
        }

        doGet(req, resp);
    }
}
