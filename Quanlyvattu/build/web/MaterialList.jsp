<%@page import="model.SubCategory"%>
<%@page import="model.Category"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Material" %>

<%
    List<Material> materials = (List<Material>) request.getAttribute("materials");
    int currentPage = (int) request.getAttribute("currentPage");
    int totalPage = (int) request.getAttribute("totalPage");

    String selectedCategory = request.getParameter("category");
    String selectedSubcategory = request.getParameter("subcategory");
    String searchName = request.getParameter("name");

    // Giới hạn hiển thị tối đa 3 trang
    int startPage = Math.max(1, currentPage - 1);
    int endPage = Math.min(totalPage, startPage + 2);
    if (endPage - startPage < 2) {
        startPage = Math.max(1, endPage - 2);
    }

    String baseParams = "";
    if (selectedCategory != null && !selectedCategory.isEmpty()) {
        baseParams += "&category=" + selectedCategory;
    }
    if (selectedSubcategory != null && !selectedSubcategory.isEmpty()) {
        baseParams += "&subcategory=" + selectedSubcategory;
    }
    if (searchName != null && !searchName.isEmpty())
        baseParams += "&name=" + searchName;
%>

<!-- Material List Page -->
<div id="materiallist-page" class="page">
    <div class="flex justify-between items-center mb-4">
        <form method="get" action="materiallist" class="grid grid-cols-4 gap-4 items-center mb-4 w-full">
            <h2 class="col-span-4 text-2xl font-bold text-gray-800">Material List</h2>

            <!-- Category -->
            <select name="category" id="category" class="border px-3 py-2 rounded-lg shadow-sm">
                <option value="">-- All Categories --</option>
                <%
                    List<Category> allCategories = (List<Category>) request.getAttribute("allCategories");
                    if (allCategories != null) {
                        for (Category c : allCategories) {
                %>
                <option value="<%= c.getCategoryId()%>" <%= ("" + c.getCategoryId()).equals(selectedCategory) ? "selected" : ""%>>
                    <%= c.getCategoryName()%>
                </option>
                <%
                        }
                    }
                %>
            </select>


            <!-- SubCategory -->
            <select name="subcategory" id="subcategory" class="border px-3 py-2 rounded-lg shadow-sm">
                <option value="">-- All Subcategories --</option>
            </select>

            <!-- Search -->
            <input type="text" name="name" placeholder="Search name..."
                   value="<%= searchName != null ? searchName : ""%>"
                   class="border px-3 py-2 rounded-lg shadow-sm" />


            <!-- Buttons -->
            <div class="flex space-x-2">
                <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Search</button>
                <a href="materiallist" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">View All</a>
            </div>
        </form>
    </div>
    <!-- Add Button -->
    <div class="mb-6">
        <a href="material-add" class="inline-block px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
            <i class="fas fa-plus mr-1"></i> Add Category/SubCategory
        </a>
    </div>
    
    <div class="mb-6">
        <a href="material-add" class="inline-block px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700 transition-colors">
            <i class="fas fa-plus mr-1"></i> Add New Material
        </a>
    </div>
    
</div>

<!-- Table -->
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

        <div id="image-popup" class="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50 hidden">
            <div class="relative">
                <img id="popup-img" src="" class="max-h-[90vh] max-w-[90vw] rounded shadow-lg" />
                <button onclick="closeImage()" class="absolute top-0 right-0 m-2 text-white text-3xl font-bold">&times;</button>
            </div>
        </div>

        <tbody>
            <% for (Material m : materials) {
                    String code = "VT" + String.format("%04d", m.getMaterialId());
                    int inStock = m.getQuantity();
            %>
            <tr class="border-t hover:bg-gray-50">
                <td class="py-2 px-4"><%= m.getCategoryName()%></td>
                <td class="py-2 px-4"><%= m.getSubCategoryName()%></td>
                <td class="py-2 px-4 font-medium"><%= code%></td>
                <td class="py-2 px-4">
                    <img src="<%= request.getContextPath() + "/assets/images/materials/" + ((m.getImage() == null || m.getImage().isEmpty()) ? "default.png" : m.getImage())%>"
                         alt="<%= m.getMaterialName()%>"
                         class="h-12 w-12 object-cover rounded cursor-pointer transition-transform hover:scale-110"
                         onclick="showImage(this.src)" />
                </td>
                <td class="py-2 px-4"><%= m.getMaterialName()%></td>
                <td class="py-2 px-4">
                    <span class="<%= "New".equals(m.getStatusName()) ? "text-green-600" : ("Damaged".equals(m.getStatusName()) ? "text-red-600" : "text-yellow-600")%>">
                        <%= m.getStatusName()%>
                    </span>
                </td>
                <td class="py-2 px-4"><%= inStock%></td>
                <td class="py-2 px-4"><%= String.format("%,.0f", m.getPrice())%> đ</td>
                <td class="py-2 px-4">
                    <a href="materialdetail?id=<%= m.getMaterialId()%>&page=<%= currentPage%><%= baseParams%>" class="text-blue-600 hover:underline">View</a>
                </td>
            </tr>
            <% }%>
        </tbody>
    </table>
</div>

<!-- Pagination -->
<div class="flex justify-between items-center mt-4">
    <div class="text-sm text-gray-600">
        Showing <%= currentPage%> / <%= totalPage%> page
    </div>
    <div class="space-x-1">
        <% if (currentPage > 1) {%>
        <a href="materiallist?page=<%= currentPage - 1%><%= baseParams%>" class="px-3 py-1 border rounded">«</a>
        <% } else { %>
        <span class="px-3 py-1 border rounded text-gray-400 cursor-not-allowed">«</span>
        <% } %>

        <% for (int i = startPage; i <= endPage; i++) { %>
        <% if (i == currentPage) {%>
        <span class="px-3 py-1 border rounded bg-blue-600 text-white"><%= i%></span>
        <% } else {%>
        <a href="materiallist?page=<%= i%><%= baseParams%>" class="px-3 py-1 border rounded hover:bg-gray-100"><%= i%></a>
        <% } %>
        <% } %>

        <% if (currentPage < totalPage) {%>
        <a href="materiallist?page=<%= currentPage + 1%><%= baseParams%>" class="px-3 py-1 border rounded">»</a>
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

    document.getElementById("image-popup").addEventListener("click", function (e) {
        if (e.target.id === "image-popup") {
            closeImage();
        }
    });
</script>
<script>
    let allSubcategories = [];

    async function fetchSubcategories() {
        try {
            const response = await fetch("<%= request.getContextPath()%>/general/get-subcategories");


            allSubcategories = await response.json();
        } catch (error) {
            console.error("Failed to fetch subcategories:", error);
        }
    }

    function updateSubcategories() {
        const categorySelect = document.querySelector('select[name="category"]');
        const subcategorySelect = document.querySelector('select[name="subcategory"]');
        const selectedCategoryId = categorySelect.value;

        subcategorySelect.innerHTML = '<option value="">-- All Subcategories --</option>';

        const categoryId = Number(selectedCategoryId);
        const filtered = allSubcategories.filter(sub => sub.categoryId === categoryId);

        filtered.forEach(sub => {
            const option = document.createElement("option");
            option.value = sub.subCategoryId; // ✅ Đúng
            option.text = sub.subCategoryName;
            subcategorySelect.appendChild(option);
        });

        // Giữ lại subcategory đã chọn sau khi lọc
        const selectedSub = "<%= selectedSubcategory != null ? selectedSubcategory : ""%>";
        if (selectedSub) {
            Array.from(subcategorySelect.options).forEach(option => {
                if (option.value === selectedSub) {
                    option.selected = true;
                }
            });
        }
    }

    document.addEventListener("DOMContentLoaded", async function () {
        await fetchSubcategories();
        document.querySelector('select[name="category"]').addEventListener("change", updateSubcategories);
        updateSubcategories(); // initial trigger
    });

</script>



