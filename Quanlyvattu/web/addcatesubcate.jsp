<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Toast -->
<c:if test="${not empty toast}">
    <div class="fixed top-4 right-4 bg-green-500 text-white px-4 py-2 rounded shadow-lg z-50 animate-bounce">
        ${toast}
    </div>
    <script>
        setTimeout(() => {
            document.querySelector(".fixed.top-4.right-4").style.display = "none";
        }, 3000);
    </script>
</c:if>

<div class="max-w-3xl mx-auto bg-white p-8 rounded shadow mt-6">
    <h2 class="text-xl font-bold mb-4 text-blue-600">➕ Add Category</h2>
    <form action="add-category" method="post" class="mb-8 flex gap-4">
        <input type="hidden" name="action" value="category" />
        <input type="text" name="categoryName" placeholder="Enter category name"
               class="border px-4 py-2 flex-1 rounded" required />
        <button type="submit"
                class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded">Add</button>
    </form>

    <h2 class="text-xl font-bold mb-4 text-green-600">➕ Add SubCategory</h2>
    <form action="add-category" method="post" class="space-y-4">
        <input type="hidden" name="action" value="subcategory" />
        <label class="block font-semibold">Parent Category</label>
        <select name="parentCategoryId" class="border px-4 py-2 w-full rounded" required>
            <option value="">-- Select Category --</option>
            <c:forEach var="cat" items="${categories}">
                <option value="${cat.categoryId}">${cat.categoryName}</option>
            </c:forEach>
        </select>

        <input type="text" name="subCategoryName" placeholder="Subcategory name"
               class="border px-4 py-2 w-full rounded" required />

        <button type="submit"
                class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded">
            Add SubCategory
        </button>
    </form>
</div>
