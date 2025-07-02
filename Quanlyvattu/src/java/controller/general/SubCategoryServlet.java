/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.general;

import DAO.SubCategoryDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.SubCategory;

/**
 *
 * @author anhdu
 */
@WebServlet("/general/get-subcategories")
public class SubCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<SubCategory> list = new SubCategoryDAO().getAllWithCategory();

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        out.print(gson.toJson(list));
        out.flush();
    }
}
