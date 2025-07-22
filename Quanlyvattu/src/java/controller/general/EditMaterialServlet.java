package controller.general;

import DAO.MaterialDAO;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Material;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import model.Category;
import model.SubCategory;

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
            List<Category> categories = dao.getAllCategories();  // Giống bên add
            List<SubCategory> subcategories = dao.getAllSubcategories(); // ✅ Chính xác

            if (material != null) {
                // Convert subcategories -> JSON để dùng trong script
                String subJson = new Gson().toJson(
                        subcategories.stream().map(sub -> Map.of(
                        "id", sub.getSubCategoryId(),
                        "name", sub.getSubCategoryName(),
                        "categoryId", sub.getCategoryId()
                )).toList()
                );

                request.setAttribute("material", material);
                request.setAttribute("categories", categories);
                request.setAttribute("subcategoriesJson", subJson);
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

        try {
            String materialIdStr = request.getParameter("materialId");
            String materialName = request.getParameter("materialName");
            String subCategoryIdStr = request.getParameter("subCategoryId");
            String unit = request.getParameter("unit");
            String description = request.getParameter("description");

            if (materialIdStr == null || materialName == null || materialName.trim().isEmpty()
                    || subCategoryIdStr == null || unit == null || unit.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
                doGet(request, response);
                return;
            }

            int materialId = Integer.parseInt(materialIdStr);
            int subCategoryId = Integer.parseInt(subCategoryIdStr);

            Material material = new Material();
            material.setMaterialId(materialId);
            material.setMaterialName(materialName);
            material.setSubCategoryId(subCategoryId);
            material.setUnit(unit);
            material.setDescription(description);

            MaterialDAO dao = new MaterialDAO();
            boolean success = dao.updateMaterialBasicInfo(material);

            if (success) {
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
