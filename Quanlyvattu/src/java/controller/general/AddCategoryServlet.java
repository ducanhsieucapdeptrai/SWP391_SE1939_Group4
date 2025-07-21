package controller.general;

import DAO.MaterialDAO;
import DAO.NotificationDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Category;

@WebServlet("/add-category")
public class AddCategoryServlet extends HttpServlet {

    private final MaterialDAO dao = new MaterialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = dao.getAllCategories();
        request.setAttribute("categories", categories);
        request.setAttribute("pageContent", "/addcatesubcate.jsp"); // KHÔNG có "/"

        String success = request.getParameter("success");
        if (success != null) {
            if (success.equals("category")) {
                request.setAttribute("toast", "Category added successfully!");
            } else if (success.equals("subcategory")) {
                request.setAttribute("toast", "SubCategory added successfully!");
            }
        }

        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

       if ("category".equals(action)) {
    String name = request.getParameter("categoryName");
    if (name != null && !name.trim().isEmpty()) {
        dao.addCategory(name);

        // 🔔 Thông báo danh mục mới
        try {
            NotificationDAO notiDAO = new NotificationDAO();
            notiDAO.createCategoryNotification(name, false); // false = là Category
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("add-category?success=category");
        return;
    }
} else if ("subcategory".equals(action)) {
    try {
        int categoryId = Integer.parseInt(request.getParameter("parentCategoryId"));
        String subCategoryName = request.getParameter("subCategoryName");

        if (subCategoryName != null && !subCategoryName.trim().isEmpty()) {
            dao.addSubCategory(categoryId, subCategoryName);

            // 🔔 Thông báo danh mục con mới
            try {
                NotificationDAO notiDAO = new NotificationDAO();
                notiDAO.createCategoryNotification(subCategoryName, true); // true = là SubCategory
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect("add-category?success=subcategory");
            return;
        }
    } catch (NumberFormatException e) {
        e.printStackTrace();
    }
}}}


