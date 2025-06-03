package controller.general;

import DAO.MaterialDAO;
import model.Material;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/materialdetail")
public class MaterialDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("materiallist");
            return;
        }

        try {
            int materialId = Integer.parseInt(idParam);
            MaterialDAO dao = new MaterialDAO();
            Material material = dao.getMaterialById(materialId);

            if (material != null) {
                request.setAttribute("m", material);
                request.setAttribute("pageContent", "/MaterialDetail.jsp");
                request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Material not found.");
                request.getRequestDispatcher("materiallist").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("materiallist"); // hoặc trang lỗi
        }
    }
}
