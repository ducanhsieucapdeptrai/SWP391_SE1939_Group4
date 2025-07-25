package controller.general;

import DAO.MaterialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Material;

import jakarta.servlet.annotation.WebServlet;
import model.Category;
import model.SubCategory;

@WebServlet(name = "MaterialList", urlPatterns = {"/materiallist"})
public class MaterialListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        // Lấy dữ liệu tìm kiếm
        String category = request.getParameter("category");
        String subcategory = request.getParameter("subcategory");
        String name = request.getParameter("name");

        MaterialDAO dao = new MaterialDAO();
        List<Material> materials;
        int totalCount;

        boolean isSearching = (category != null && !category.isEmpty())
                || (subcategory != null && !subcategory.isEmpty())
                || (name != null && !name.isEmpty());

        if (isSearching) {
            materials = dao.searchMaterials(category, subcategory, name);
            totalCount = materials.size();  // Không phân trang khi tìm kiếm
            request.setAttribute("isSearching", true);
        } else {
            materials = dao.getMaterialsByPage(offset, pageSize); // ✅ đã include Unit
            totalCount = dao.getTotalMaterialCount();
        }

        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // Lấy danh sách dropdown filter
        List<Category> allCategories = dao.getAllCategories();
        List<SubCategory> allSubcategories = dao.getAllSubcategories();

        // Truyền các dữ liệu cần thiết sang JSP
        request.setAttribute("materials", materials);
        request.setAttribute("allCategories", allCategories);
        request.setAttribute("allSubcategories", allSubcategories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        // Giữ lại các tham số để tái sử dụng trong phân trang / filter
        request.setAttribute("selectedCategory", category);
        request.setAttribute("selectedSubcategory", subcategory);
        request.setAttribute("searchName", name);

        // Truyền thêm current page để sử dụng khi back từ detail
        request.setAttribute("pageIndex", page);

        request.setAttribute("pageContent", "/MaterialList.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("materiallist");
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị danh sách vật tư có phân trang và tìm kiếm kèm đơn vị tính";
    }
}
