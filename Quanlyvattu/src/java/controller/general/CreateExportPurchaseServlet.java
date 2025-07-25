// CreateRequestServlet.java
package controller.general;

import DAO.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/createexportpurchase")
public class CreateExportPurchaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExportPurchaseRequestDAO dao = new ExportPurchaseRequestDAO();
        req.setAttribute("types", dao.getAllRequestTypes());
        req.setAttribute("materials", dao.getAllMaterials());

        req.setAttribute("pageContent", "/createRequest.jsp");
        req.getRequestDispatcher("/layout/layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            int typeId = Integer.parseInt(req.getParameter("typeId"));
            String note = req.getParameter("note");
            if (note == null || note.trim().isEmpty())
                throw new ServletException("Note is required");

            String[] materialIds = req.getParameterValues("materialId[]");
            String[] quantities = req.getParameterValues("quantity[]");

            if (materialIds == null || quantities == null || materialIds.length != quantities.length) {
                throw new ServletException("Invalid material data");
            }

            List<RequestDetail> details = new ArrayList<>();
            for (int i = 0; i < materialIds.length; i++) {
                RequestDetail d = new RequestDetail();
                d.setMaterialId(Integer.parseInt(materialIds[i]));
                d.setQuantity(Integer.parseInt(quantities[i]));
                details.add(d);
            }

            ExportPurchaseRequestDAO dao = new ExportPurchaseRequestDAO();
            int requestId = dao.createRequest(userId, typeId, note, details);

            if (requestId > 0) {
                resp.sendRedirect("createexportpurchase?success=1");
            } else {
                req.setAttribute("error", "Failed to create request.");
                doGet(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Error: " + e.getMessage());
            doGet(req, resp);
        }
    }
}

