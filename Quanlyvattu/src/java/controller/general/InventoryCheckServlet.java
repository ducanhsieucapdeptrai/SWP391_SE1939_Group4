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
            Material material = materialDAO.getMaterialById(materialId);
            Map<String, Integer> inventoryMap = inventoryDAO.getInventoryByMaterialId(materialId);

            request.setAttribute("material", material);
            request.setAttribute("inventoryMap", inventoryMap);
            request.setAttribute("pageContent", "/inventory-check.jsp");

            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tải thông tin vật tư: " + e.getMessage());
            request.setAttribute("pageContent", "/inventory-check.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            int newQty = parseQuantitySafe(request.getParameter("newQty"));
            int usedQty = parseQuantitySafe(request.getParameter("usedQty"));
            int damagedQty = parseQuantitySafe(request.getParameter("damagedQty"));

            int total = newQty + usedQty + damagedQty;

            System.out.println("==== Updating Inventory ====");
            System.out.println("Material ID: " + materialId);
            System.out.println("New Qty: " + newQty);
            System.out.println("Used Qty: " + usedQty);
            System.out.println("Damaged Qty: " + damagedQty);
            System.out.println("Total: " + total);

            MaterialInventoryDAO inventoryDAO = new MaterialInventoryDAO();
            MaterialDAO materialDAO = new MaterialDAO();

            inventoryDAO.updateInventory(materialId, 1, newQty);      // StatusId 1: New
            inventoryDAO.updateInventory(materialId, 2, usedQty);     // StatusId 2: Used
            inventoryDAO.updateInventory(materialId, 3, damagedQty);  // StatusId 3: Damaged

            materialDAO.updateMaterialQuantity(materialId, total);

            response.sendRedirect("materialdetail?materialId=" + materialId);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Inventory update failed: " + e.getMessage());
            request.setAttribute("pageContent", "/inventory-check.jsp");
            request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
        }
    }

    private int parseQuantitySafe(String param) {
        try {
            return (param != null && !param.trim().isEmpty()) ? Integer.parseInt(param.trim()) : 0;
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format for quantity: " + param);
            return 0;
        }
    }
}
