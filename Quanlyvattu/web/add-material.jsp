<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto px-4 py-8">
    <h2 class="text-2xl font-semibold mb-6 text-purple-600">Edit Material</h2>

    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
            ${error}
        </div>
    </c:if>

    <form action="updatematerial" method="post" enctype="multipart/form-data" class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <!-- Hidden ID -->
        <input type="hidden" name="materialId" value="${material.materialId}" />

        <!-- Material Name -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="materialName">
                Material Name
            </label>
            <input class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                   id="materialName" name="materialName" type="text" value="${material.materialName}" required />
        </div>

        <!-- Category -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="categoryId">
                Category
            </label>
            <select class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    id="categoryId" name="categoryId" required>
                <option value="">Select Category</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.categoryId}" ${category.categoryId == material.categoryId ? 'selected' : ''}>
                        ${category.categoryName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Subcategory -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="subCategoryId">
                Subcategory
            </label>
            <select class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    id="subCategoryId" name="subCategoryId" required>
                <option value="">Select Subcategory</option>
                <!-- JavaScript sẽ tự động thêm option đúng -->
            </select>
        </div>

        <!-- Unit -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="unit">
                Unit
            </label>
            <input class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                   id="unit" name="unit" type="text" value="${material.unit}" placeholder="e.g. cái, kg, m³" required />
        </div>

        <!-- Image -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="image">
                Image (Leave blank to keep current)
            </label>
            <div class="mb-2">
                <c:if test="${not empty material.image}">
                    <img src="${pageContext.request.contextPath}/${material.image}" alt="Material Image"
                         class="w-32 h-32 object-contain border rounded" />
                </c:if>
            </div>
            <input type="file" name="image" id="image"
                   class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                   accept="image/*" />
        </div>

        <!-- Description -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="description">
                Description
            </label>
            <textarea id="description" name="description"
                      class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                      rows="4">${material.description}</textarea>
        </div>

        <!-- Buttons -->
        <div class="flex items-center justify-between">
            <button type="submit"
                    class="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline">
                Save Changes
            </button>
            <a href="${pageContext.request.contextPath}/materiallist"
               class="inline-block align-baseline font-bold text-sm text-gray-600 hover:text-gray-800">
                Cancel
            </a>
        </div>
    </form>
</div>

<!-- Load Subcategories Dynamically -->
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const subcategories = <c:out value="${subcategoriesJson}" escapeXml="false" />;
        const categorySelect = document.getElementById('categoryId');
        const subcategorySelect = document.getElementById('subCategoryId');
        const selectedSubId = '${material.subCategoryId}';

        function updateSubcategories() {
            const selectedCategoryId = categorySelect.value;
            subcategorySelect.innerHTML = '<option value="">Select Subcategory</option>';

            const filtered = subcategories.filter(sub => sub.categoryId == selectedCategoryId);
            filtered.forEach(sub => {
                const option = document.createElement('option');
                option.value = sub.id;
                option.textContent = sub.name;
                if (sub.id == selectedSubId)
                    option.selected = true;
                subcategorySelect.appendChild(option);
            });
        }

        categorySelect.addEventListener('change', updateSubcategories);
        updateSubcategories(); // Run on load
    });
</script>
