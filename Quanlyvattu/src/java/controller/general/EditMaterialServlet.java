package controller.general;

import DAO.MaterialDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Material;

import java.io.IOException;

@WebServlet(urlPatterns = {"/editmaterial", "/updatematerial"})
public class EditMaterialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("materialId");

        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số materialId");
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);
            MaterialDAO dao = new MaterialDAO();
            Material material = dao.getMaterialById(id);

            if (material != null) {
                request.setAttribute("material", material);
                request.setAttribute("pageContent", "/EditMaterial.jsp");
                request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy vật tư với ID: " + id);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ: " + idRaw);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Xử lý cập nhật material
        try {
            String materialIdStr = request.getParameter("materialId");
            String materialName = request.getParameter("materialName");
            String subCategoryIdStr = request.getParameter("subCategoryId");
            String statusIdStr = request.getParameter("statusId");
            String quantityStr = request.getParameter("quantity");
            String minQuantityStr = request.getParameter("minQuantity");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");

            // Validate required fields
            if (materialIdStr == null || materialName == null || materialName.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                doGet(request, response);
                return;
            }

            int materialId = Integer.parseInt(materialIdStr);
            int subCategoryId = Integer.parseInt(subCategoryIdStr);
            int statusId = Integer.parseInt(statusIdStr);
            int quantity = Integer.parseInt(quantityStr);
            int minQuantity = Integer.parseInt(minQuantityStr);
            double price = Double.parseDouble(priceStr);

            // Tạo object Material để update
            Material material = new Material();
            material.setMaterialId(materialId);
            material.setMaterialName(materialName);
            material.setSubCategoryId(subCategoryId);
            material.setStatusId(statusId);
            material.setQuantity(quantity);
            material.setMinQuantity(minQuantity);
            material.setPrice(price);
            material.setDescription(description);

            // Cập nhật vào database
            MaterialDAO dao = new MaterialDAO();
            boolean success = dao.updateMaterial(material);

            if (success) {
                // Redirect về material detail hoặc list
                response.sendRedirect("materialdetail?id=" + materialId);
            } else {
                request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            doGet(request, response);
        }
    }
}
