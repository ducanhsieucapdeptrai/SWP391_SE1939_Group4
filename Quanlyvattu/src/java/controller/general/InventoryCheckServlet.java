package controller.general;

import DAO.MaterialDAO;
import DAO.MaterialInventoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Material;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "InventoryCheckServlet", urlPatterns = {"/inventory-check"})
public class InventoryCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int materialId = Integer.parseInt(request.getParameter("materialId"));

        MaterialDAO materialDAO = new MaterialDAO();
        MaterialInventoryDAO inventoryDAO = new MaterialInventoryDAO();

        try {
            // Lấy thông tin vật tư
            Material material = materialDAO.getMaterialById(materialId);

            // Lấy số lượng theo từng trạng thái (New, Used, Damaged)
            Map<String, Integer> inventoryMap = inventoryDAO.getInventoryByMaterialId(materialId);

            request.setAttribute("material", material);
            request.setAttribute("inventoryMap", inventoryMap);
            request.setAttribute("pageContent", "/inventory-check.jsp");

            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server Error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            int newQty = Integer.parseInt(request.getParameter("newQty"));
            int usedQty = Integer.parseInt(request.getParameter("usedQty"));
            int damagedQty = Integer.parseInt(request.getParameter("damagedQty"));

            int total = newQty + usedQty + damagedQty;

            MaterialInventoryDAO inventoryDAO = new MaterialInventoryDAO();
            MaterialDAO materialDAO = new MaterialDAO();

            // Cập nhật chi tiết kiểm kho theo trạng thái
            inventoryDAO.updateInventory(materialId, 1, newQty);      // StatusId 1: New
            inventoryDAO.updateInventory(materialId, 2, usedQty);     // StatusId 2: Used
            inventoryDAO.updateInventory(materialId, 3, damagedQty);  // StatusId 3: Damaged

            // Cập nhật tổng tồn kho về bảng Materials
            materialDAO.updateMaterialQuantity(materialId, total);

            // Redirect quay lại chi tiết
            response.sendRedirect("materialdetail?materialId=" + materialId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Inventory update failed");
        }
    }

}
