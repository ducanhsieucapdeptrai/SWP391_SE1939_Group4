package controller.general;

import DAO.MaterialDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Material;

@WebServlet(name = "MaterialList", urlPatterns = {"/materiallist"})
public class MaterialListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        // Lấy số trang nếu có
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * pageSize;

        // Gọi DAO
        MaterialDAO dao = new MaterialDAO();
        List<Material> materials = dao.getMaterialsByPage(offset, pageSize);
        int totalCount = dao.getTotalMaterialCount();
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // Truyền dữ liệu sang JSP
        request.setAttribute("materials", materials);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        // Forward sang trang hiển thị
        request.getRequestDispatcher("MaterialList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Không xử lý POST ở đây
        response.sendRedirect("materiallist");
    }

    @Override
    public String getServletInfo() {
        return "Hiển thị danh sách vật tư có phân trang";
    }
}
