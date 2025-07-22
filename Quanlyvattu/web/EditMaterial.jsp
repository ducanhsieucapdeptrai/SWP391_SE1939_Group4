<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Material</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
<div class="container mx-auto px-4 py-8">
    <h2 class="text-2xl font-semibold mb-6 text-purple-600">Edit Material</h2>

    <c:if test="${not empty error}">
        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
            ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/updatematerial" method="post" enctype="multipart/form-data"
          class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
        <!-- Hidden field -->
        <input type="hidden" name="materialId" value="${material.materialId}" />

        <!-- Image -->
        <div class="mb-4 text-center">
            <c:choose>
                <c:when test="${not empty material.image}">
                    <img src="${pageContext.request.contextPath}/assets/images/materials/${material.image}"
                         alt="Material Image"
                         class="mx-auto max-h-48 object-contain rounded" />
                </c:when>
                <c:otherwise>
                    <span class="text-gray-400 text-6xl">&#128247;</span>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Name -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="materialName">Material Name</label>
            <input class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                   id="materialName" name="materialName" type="text"
                   value="${material.materialName}" required>
        </div>

        <!-- Category -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="categoryId">Category</label>
            <select class="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
                    id="categoryId" name="categoryId" required>
                <option value="">Select Category</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.categoryId}" ${cat.categoryId == material.categoryId ? 'selected' : ''}>
                        ${cat.categoryName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Subcategory -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="subCategoryId">Subcategory</label>
            <select class="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
                    id="subCategoryId" name="subCategoryId" required>
                <option value="">Select Subcategory</option>
            </select>
        </div>

        <!-- Unit -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="unit">Unit</label>
            <input class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                   id="unit" name="unit" type="text"
                   value="${material.unit}" required>
        </div>

        <!-- Description -->
        <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2" for="description">Description</label>
            <textarea class="shadow border rounded w-full py-2 px-3 text-gray-700 focus:outline-none focus:shadow-outline"
                      id="description" name="description" rows="4">${material.description}</textarea>
        </div>

        <!-- Actions -->
        <div class="flex items-center justify-between">
            <button class="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                    type="submit">Save Changes</button>
            <a href="${pageContext.request.contextPath}/materiallist"
               class="inline-block align-baseline font-bold text-sm text-gray-600 hover:text-black">
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
        const selectedSubId = "${material.subCategoryId}";

        function updateSubcategories() {
            const selectedCategoryId = categorySelect.value;
            subcategorySelect.innerHTML = '<option value="">Select Subcategory</option>';

            if (selectedCategoryId) {
                const filtered = subcategories.filter(sub => sub.categoryId == selectedCategoryId);
                filtered.forEach(sub => {
                    const option = document.createElement('option');
                    option.value = sub.id;
                    option.textContent = sub.name;
                    if (sub.id == selectedSubId) option.selected = true;
                    subcategorySelect.appendChild(option);
                });
            }
        }

        categorySelect.addEventListener('change', updateSubcategories);
        updateSubcategories(); // Load initial
    });
</script>
</body>
</html>
