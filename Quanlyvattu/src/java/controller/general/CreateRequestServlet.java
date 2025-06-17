    package controller.general;

    import DAO.RequestDAO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.*;
    import model.RequestDetail;
    import model.Users;

    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    @WebServlet(name = "CreateRequestServlet", urlPatterns = {"/createrequest"})
    public class CreateRequestServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            // Check logged‑in user
            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("currentUser");
            if (user == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            RequestDAO dao = new RequestDAO();
            req.setAttribute("categories",    dao.getAllCategories());
            req.setAttribute("subCategories", dao.getAllSubCategories());
            req.setAttribute("requestTypes",  dao.getAllRequestType());
            req.setAttribute("materials",     dao.getAllMaterials());  // or dao.getMaterialsBySubCategory(...)
            req.setAttribute("pageContent",   "/createRequest.jsp");
            req.getRequestDispatcher("/layout/layout.jsp").forward(req, resp);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setCharacterEncoding("UTF-8");
            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("currentUser");
            if (user == null) {
                resp.sendRedirect("login.jsp");
                return;
            }

            try {
                int requestTypeId = Integer.parseInt(req.getParameter("requestTypeId"));
                String note      = req.getParameter("note");

                String[] materialIds = req.getParameterValues("materialId");
                String[] quantities  = req.getParameterValues("quantity");

                if (materialIds == null || quantities == null || materialIds.length != quantities.length) {
                    req.setAttribute("error", "Please select at least one material and enter a valid quantity.");
                    doGet(req, resp);
                    return;
                }
                 boolean checkStock=(requestTypeId==1);
             RequestDAO dao = new RequestDAO();
                List<RequestDetail> details = new ArrayList<>();
                for (int i = 0; i < materialIds.length; i++) {
                    int materialId = Integer.parseInt(materialIds[i]);
                    int qty        = Integer.parseInt(quantities[i]);
                    if (qty <= 0) throw new NumberFormatException();
                     if (checkStock) {
                    int stock = dao.getMaterialStock(materialId);
                    if (qty > stock) {
                        req.setAttribute("error",
                            String.format("Material ID %d only has %d in stock, cannot request %d.",
                                          materialId, stock, qty));
                        doGet(req, resp);
                        return;
                    }
                }
                    RequestDetail d = new RequestDetail();
                    d.setMaterialId(materialId);
                    d.setQuantity(qty);
                    details.add(d);
                }

                boolean success = new RequestDAO()
                                      .createRequest(user.getUserId(), requestTypeId, note, details);
                if (success) {
                    req.setAttribute("success", "Your request has been created and sent for approval.");
                } else {
                    req.setAttribute("error",   "An error occurred while creating your request.");
                }

            } catch (NumberFormatException e) {
                req.setAttribute("error", "Invalid data format. Please check your inputs.");
            } catch (Exception e) {
                req.setAttribute("error", "Error: " + e.getMessage());
            }

            doGet(req, resp);
        }
    }
