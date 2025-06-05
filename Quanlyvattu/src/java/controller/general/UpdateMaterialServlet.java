package controller.general;

import DAO.MaterialDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Material;

import java.io.IOException;

@WebServlet("/updatematerial")
public class UpdateMaterialServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Parse tất cả parameters với error handling
            int id = Integer.parseInt(request.getParameter("materialId"));
            String name = request.getParameter("materialName");
            int subCategoryId = Integer.parseInt(request.getParameter("subCategoryId"));
            int statusId = Integer.parseInt(request.getParameter("statusId"));
            String image = request.getParameter("image");
            String desc = request.getParameter("description");
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int minQuantity = Integer.parseInt(request.getParameter("minQuantity"));
            double price = Double.parseDouble(request.getParameter("price"));

            // Validate input data
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("error", "Tên vật tư không được để trống");
                request.getRequestDispatcher("/EditMaterial.jsp").forward(request, response);
                return;
            }

            if (quantity < 0 || minQuantity < 0 || price < 0) {
                request.setAttribute("error", "Số lượng và giá không được âm");
                request.getRequestDispatcher("/EditMaterial.jsp").forward(request, response);
                return;
            }

            // Create Material object
            Material m = new Material();
            m.setMaterialId(id);
            m.setMaterialName(name.trim());
            m.setSubCategoryId(subCategoryId);
            m.setStatusId(statusId);
            m.setImage(image);
            m.setDescription(desc);
            m.setQuantity(quantity);
            m.setMinQuantity(minQuantity);
            m.setPrice(price);

            // Update material
            MaterialDAO dao = new MaterialDAO();
            boolean success = dao.updateMaterial(m);

            if (success) {
                response.sendRedirect("materialdetail?materialId=" + id);
            } else {
                request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
                // Get material data again to repopulate form
                Material material = dao.getMaterialById(id);
                request.setAttribute("material", material);
                request.getRequestDispatcher("/EditMaterial.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập vào không hợp lệ. Vui lòng kiểm tra lại các trường số.");
            request.getRequestDispatcher("/EditMaterial.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/EditMaterial.jsp").forward(request, response);
        }
    }
}