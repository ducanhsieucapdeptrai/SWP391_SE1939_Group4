package controller.director;

import DAO.PurchaseOrderDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;
import model.PurchaseOrderList;

@WebServlet("/purchase-request-list")
public class PurchaseRequestListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String status = request.getParameter("status");
        String createdDate = request.getParameter("createdDate");

        PurchaseOrderDAO dao = new PurchaseOrderDAO();
        List<PurchaseOrderList> poList = dao.getFilteredPurchaseOrders(status, createdDate);

        request.setAttribute("poList", poList);
        request.setAttribute("filterStatus", status);
        request.setAttribute("filterDate", createdDate);
        request.setAttribute("pageContent", "/View/Director/purchase-request-list.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
