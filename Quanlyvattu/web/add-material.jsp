<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto px-4 py-8">
    <h2 class="text-2xl font-semibold mb-6 text-blue-600">Add New Material</h2>

    <!-- Success/Error Message -->
    <c:if test="${not empty successMessage}">
        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative mb-4" role="alert">
            ${successMessage}
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
            ${errorMessage}
        </div>
    </c:if>

    <!-- Add Material Form -->
    <form action="${pageContext.request.contextPath}/material-add" method="post" enctype="multipart/form-data" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <!-- Material Name -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="materialName">
                Material Name
            </label>
            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                   id="materialName" name="materialName" type="text" placeholder="Enter material name" required>
        </div>

        <!-- Category Dropdown -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="categoryId">
                Category
            </label>
            <select class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                    id="categoryId" name="categoryId" required>
                <option value="">Select Category</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryId}">${category.categoryName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Subcategory Dropdown -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="subCategoryId">
                Subcategory
            </label>
            <select class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                    id="subCategoryId" name="subCategoryId" required>
                <option value="">Select Subcategory</option>
            </select>
        </div>

        <!-- Image Upload -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="image">
                Image
            </label>
            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                   id="image" name="image" type="file" accept="image/*">
        </div>

        <!-- Description -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="description">
                Description
            </label>
            <textarea class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
                      id="description" name="description" placeholder="Enter material description"></textarea>
        </div>

        <!-- Action Buttons -->
        <div class="flex items-center justify-between">
            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" 
                    type="submit">
                Add Material
            </button>
            <a href="${pageContext.request.contextPath}/materiallist" class="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800">
                Cancel
            </a>
        </div>
    </form>
</div>

<!-- Script for dynamic subcategory filtering -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const subcategories = <c:out value="${subcategoriesJson}" escapeXml="false" />;
        const categorySelect = document.getElementById('categoryId');
        const subcategorySelect = document.getElementById('subCategoryId');

        function updateSubcategories() {
            const selectedCategoryId = categorySelect.value;
            subcategorySelect.innerHTML = '<option value="">Select Subcategory</option>';

            if (selectedCategoryId) {
                const filteredSubcategories = subcategories.filter(sub => sub.categoryId == selectedCategoryId);
                filteredSubcategories.forEach(sub => {
                    const option = document.createElement('option');
                    option.value = sub.id;
                    option.textContent = sub.name;
                    subcategorySelect.appendChild(option);
                });
            }
        }

        categorySelect.addEventListener('change', updateSubcategories);
        updateSubcategories(); // Populate on page load
    });
</script>
