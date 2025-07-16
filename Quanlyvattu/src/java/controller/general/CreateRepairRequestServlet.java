package controller.general;

import DAO.MaterialDAO;
import model.Category;
import model.Material;
import model.SubCategory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/createRepairRequest")
public class CreateRepairRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MaterialDAO dao = new MaterialDAO();
        List<Category> categories = dao.getAllCategories();
        List<SubCategory> subCategories = dao.getAllSubcategories();
        List<Material> materials = dao.getAllMaterials();

        System.out.println("Materials size: " + materials.size());
        if (materials.isEmpty()) {
            System.out.println("No materials found in database.");
        } else {
            for (Material m : materials) {
                System.out.println("Material ID: " + m.getMaterialId() + ", Name: " + m.getMaterialName() + ", Quantity: " + m.getQuantity());
            }
        }

        request.setAttribute("categories", categories);
        request.setAttribute("subCategories", subCategories);
        request.setAttribute("materials", materials);
        request.setAttribute("pageContent", "/View/WarehouseManager/create-repair-request.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách các vật tư đã add
        String[] materialIds = request.getParameterValues("materialId[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] statuses = request.getParameterValues("deviceStatus[]");
        String[] budgets = request.getParameterValues("budget[]");
        String note = request.getParameter("note");

        // Kiểm tra nếu chưa có vật tư nào
        if (materialIds == null || materialIds.length == 0) {
            request.setAttribute("error", "You must add at least one material.");
            doGet(request, response); // Trả lại form với lỗi
            return;
        }

        // In ra console để kiểm tra (có thể xoá sau)
        for (int i = 0; i < materialIds.length; i++) {
            System.out.println("Material ID: " + materialIds[i]);
            System.out.println("Quantity: " + quantities[i]);
            System.out.println("Status: " + statuses[i]);
            System.out.println("Budget: " + budgets[i]);
        }

        System.out.println("Note: " + note);

        // TODO: Thực hiện lưu vào DB nếu cần (sử dụng RepairRequestDAO chẳng hạn)
        // Sau khi xử lý, có thể redirect hoặc hiển thị lại form
        response.sendRedirect("repair-order-list"); // Giả sử quay về danh sách
    }

}
