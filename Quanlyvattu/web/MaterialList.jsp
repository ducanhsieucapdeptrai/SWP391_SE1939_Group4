<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>


<%
    List<Material> materials = (List<Material>) request.getAttribute("materials");
%>
<!-- Material List Page -->
<div id="materiallist-page" class="page">
    <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-bold text-gray-800">Material List</h2>
        <input type="text" placeholder="Search materials..." class="border px-3 py-2 rounded-lg shadow-sm w-1/3" />
    </div>

    <div class="overflow-x-auto bg-white rounded-lg shadow">
        <table class="min-w-full text-sm text-left">
            <thead class="bg-gray-100 text-gray-600 font-medium">
                <tr>
                    <th class="py-3 px-4">Category</th>
                    <th class="py-3 px-4">Sub&nbsp;Category</th>
                    <th class="py-3 px-4">Material&nbsp;ID</th>
                    <th class="py-3 px-4">Image</th>
                    <th class="py-3 px-4">Material&nbsp;Name</th>
                    <th class="py-3 px-4">Status</th>
                    <th class="py-3 px-4">In&nbsp;Stock</th>
                    <th class="py-3 px-4">Price</th>
                    <th class="py-3 px-4">Action</th>
                </tr>
            </thead>
            <tbody>
                <% for (Material m : materials) {
                       /* format lại mã vật tư */
                       String code = "VT" + String.format("%04d", m.getMaterialId());
                       int inStock = m.getQuantity();      // tồn kho hiện tại
                %>
                <tr class="border-t hover:bg-gray-50">
                    <!-- Category name -->
                    <td class="py-2 px-4"><%= m.getCategoryName() %></td>

                    <!-- Sub-category name -->
                    <td class="py-2 px-4"><%= m.getSubCategoryName() %></td>

                    <!-- Material code -->
                    <td class="py-2 px-4 font-medium"><%= code %></td>

                    <!-- Image thumbnail -->
                    <td class="py-2 px-4">
                        <img src="<%= (m.getImage() == null || m.getImage().isEmpty()) ? "img/default.png" : m.getImage() %>"
                             alt="<%= m.getMaterialName() %>"
                             class="h-12 w-12 object-cover rounded" />
                    </td>

                    <!-- Material name -->
                    <td class="py-2 px-4"><%= m.getMaterialName() %></td>

                    <!-- Status name -->
                    <td class="py-2 px-4">
                        <span class="<%= "New".equals(m.getStatusName()) ? "text-green-600" : ("Damaged".equals(m.getStatusName()) ? "text-red-600" : "text-yellow-600") %>">
                            <%= m.getStatusName() %>
                        </span>
                    </td>

                    <!-- Quantity in stock -->
                    <td class="py-2 px-4"><%= inStock %></td>

                    <!-- Price -->
                    <td class="py-2 px-4"><%= String.format("%,.0f", m.getPrice()) %> đ</td>

                    <!-- View detail link -->
                    <td class="py-2 px-4">
                        <a href="materialDetail?id=<%= m.getMaterialId() %>" class="text-blue-600 hover:underline">View</a>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <!-- Simple pagination (giữ tạm, có thể thay JS/AJAX sau) -->
    <div class="flex justify-between items-center mt-4">
        <div class="text-sm text-gray-600">
            Showing <%= request.getAttribute("currentPage") %> / <%= request.getAttribute("totalPage") %> page
        </div>
        <div class="space-x-1">
            <!-- thêm logic active/disable nếu cần -->
            <a href="materiallist?page=1" class="px-3 py-1 border rounded">«</a>
            <a href="materiallist?page=<%= request.getAttribute("currentPage") %>" class="px-3 py-1 border rounded bg-blue-600 text-white">
                <%= request.getAttribute("currentPage") %>
            </a>
            <a href="materiallist?page=<%= ((int)request.getAttribute("currentPage")) + 1 %>" class="px-3 py-1 border rounded">»</a>
        </div>
    </div>
</div>




