package controller.general;

import DAO.MaterialDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Material;

import java.io.IOException;

@WebServlet("/editmaterial")
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
}
