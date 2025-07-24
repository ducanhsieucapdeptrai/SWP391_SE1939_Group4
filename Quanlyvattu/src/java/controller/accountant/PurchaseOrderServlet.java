package controller.accountant;

import DAO.RequestDAO;
import model.RequestList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/purchase-order")
public class PurchaseOrderServlet extends HttpServlet {

    private RequestDAO requestDAO;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<RequestList> approvedRequests = requestDAO.getApprovedPurchaseRequestsWithoutPO();
        request.setAttribute("requestList", approvedRequests);
        request.setAttribute("pageContent", "/View/Accountant/purchase-order.jsp");
        request.getRequestDispatcher("/layout/layout.jsp").forward(request, response);
    }
}
