<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>


<%
    List<Material> materials = (List<Material>) request.getAttribute("materials");
%>
<!-- Material List Page -->
<div id="materiallist-page" class="page">
    <div class="flex justify-between items-center mb-4">

        <form method="get" action="materiallist" class="grid grid-cols-4 gap-4 items-center mb-4">
            <h2 class="col-span-4 text-2xl font-bold text-gray-800">Material List</h2>

            <!-- Category search -->
            <input type="text" name="category" placeholder="Search category..." 
                   value="<%= request.getParameter("category") != null ? request.getParameter("category") : ""%>"
                   class="border px-3 py-2 rounded-lg shadow-sm" />

            <!-- SubCategory search -->
            <input type="text" name="subcategory" placeholder="Search subcategory..." 
                   value="<%= request.getParameter("subcategory") != null ? request.getParameter("subcategory") : ""%>"
                   class="border px-3 py-2 rounded-lg shadow-sm" />

            <!-- Name search -->
            <input type="text" name="name" placeholder="Search name..." 
                   value="<%= request.getParameter("name") != null ? request.getParameter("name") : ""%>"
                   class="border px-3 py-2 rounded-lg shadow-sm" />

            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Search</button>
        
        </form>
                   
                   <form action="materiallist" method="get" style="display:inline;">
                       <a href="materiallist" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Xem tất cả</a>
                   </form>

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
            <!-- Popup image viewer -->
            <div id="image-popup" class="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50 hidden">
                <div class="relative">
                    <img id="popup-img" src="" class="max-h-[90vh] max-w-[90vw] rounded shadow-lg" />
                    <button onclick="closeImage()" class="absolute top-0 right-0 m-2 text-white text-3xl font-bold">&times;</button>
                </div>
            </div>

            <tbody>
                <% for (Material m : materials) {
                        /* format lại mã vật tư */
                        String code = "VT" + String.format("%04d", m.getMaterialId());
                        int inStock = m.getQuantity();      // tồn kho hiện tại
                %>
                <tr class="border-t hover:bg-gray-50">
                    <!-- Category name -->
                    <td class="py-2 px-4"><%= m.getCategoryName()%></td>

                    <!-- Sub-category name -->
                    <td class="py-2 px-4"><%= m.getSubCategoryName()%></td>

                    <!-- Material code -->
                    <td class="py-2 px-4 font-medium"><%= code%></td>

                    <!-- Image thumbnail -->
                    <td class="py-2 px-4">
                        <img src="<%= request.getContextPath() + "/" + ((m.getImage() == null || m.getImage().isEmpty()) ? "assets/images/materials/default.png" : m.getImage())%>"
                             alt="<%= m.getMaterialName()%>"
                             class="h-12 w-12 object-cover rounded cursor-pointer transition-transform hover:scale-110"
                             onclick="showImage(this.src)" />

                    </td>

                    <!-- Material name -->
                    <td class="py-2 px-4"><%= m.getMaterialName()%></td>

                    <!-- Status name -->
                    <td class="py-2 px-4">
                        <span class="<%= "New".equals(m.getStatusName()) ? "text-green-600" : ("Damaged".equals(m.getStatusName()) ? "text-red-600" : "text-yellow-600")%>">
                            <%= m.getStatusName()%>
                        </span>
                    </td>

                    <!-- Quantity in stock -->
                    <td class="py-2 px-4"><%= inStock%></td>

                    <!-- Price -->
                    <td class="py-2 px-4"><%= String.format("%,.0f", m.getPrice())%> đ</td>

                    <!-- View detail link -->
                    <td class="py-2 px-4">
                        <a href="materialdetail?id=<%= m.getMaterialId()%>" class="text-blue-600 hover:underline">View</a>
                    </td>
                </tr>
                <% }%>
            </tbody>
        </table>
    </div>

    <%
        int currentPage = (int) request.getAttribute("currentPage");
        int totalPage = (int) request.getAttribute("totalPage");

        // Giới hạn hiển thị tối đa 3 trang
        int startPage = Math.max(1, currentPage - 1);
        int endPage = Math.min(totalPage, startPage + 2);

        // Điều chỉnh lại startPage nếu end gần cuối
        if (endPage - startPage < 2) {
            startPage = Math.max(1, endPage - 2);
        }
    %>

    <div class="flex justify-between items-center mt-4">
        <div class="text-sm text-gray-600">
            Showing <%= currentPage%> / <%= totalPage%> page
        </div>
        <div class="space-x-1">
            <!-- Nút « -->
            <% if (currentPage > 1) {%>
            <a href="materiallist?page=<%= currentPage - 1%>" class="px-3 py-1 border rounded">«</a>
            <% } else { %>
            <span class="px-3 py-1 border rounded text-gray-400 cursor-not-allowed">«</span>
            <% } %>

            <!-- Các nút số trang -->
            <% for (int i = startPage; i <= endPage; i++) { %>
            <% if (i == currentPage) {%>
            <a href="materiallist?page=<%= i%>" class="px-3 py-1 border rounded bg-blue-600 text-white"><%= i%></a>
            <% } else {%>
            <a href="materiallist?page=<%= i%>" class="px-3 py-1 border rounded hover:bg-gray-100"><%= i%></a>
            <% } %>
            <% } %>

            <!-- Nút » -->
            <% if (currentPage < totalPage) {%>
            <a href="materiallist?page=<%= currentPage + 1%>" class="px-3 py-1 border rounded">»</a>
            <% } else { %>
            <span class="px-3 py-1 border rounded text-gray-400 cursor-not-allowed">»</span>
            <% }%>
        </div>
    </div>
    <script>
        function showImage(src) {
            const popup = document.getElementById("image-popup");
            const img = document.getElementById("popup-img");
            img.src = src;
            popup.classList.remove("hidden");
        }

        function closeImage() {
            document.getElementById("image-popup").classList.add("hidden");
        }

        // Đóng popup khi click ra ngoài ảnh
        document.getElementById("image-popup").addEventListener("click", function (e) {
            if (e.target.id === "image-popup") {
                closeImage();
            }
        });
    </script>



