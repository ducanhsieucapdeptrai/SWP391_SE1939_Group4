<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto p-6">
    <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Add New Material</h2>
        
        <form action="${pageContext.request.contextPath}/material-add" method="post" enctype="multipart/form-data" class="space-y-6">
            <!-- Error Message -->
            <c:if test="${not empty errorMessage}">
                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
                    <span class="block sm:inline">${errorMessage}</span>
                </div>
            </c:if>
            
            <!-- Success Message -->
            <c:if test="${not empty successMessage}">
                <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
                    <span class="block sm:inline">${successMessage}</span>
                </div>
            </c:if>
            
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Left Column -->
                <div class="space-y-4">
                    <!-- Material Name -->
                    <div>
                        <label for="materialName" class="block text-sm font-medium text-gray-700">Material Name *</label>
                        <input type="text" id="materialName" name="materialName" required
                               class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                    </div>
                    
                    <!-- Category -->
                    <div>
                        <label for="categoryId" class="block text-sm font-medium text-gray-700">Category *</label>
                        <select id="categoryId" name="categoryId" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                            <option value="">-- Select Category --</option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.categoryId}">${category.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Subcategory -->
                    <div>
                        <label for="subCategoryId" class="block text-sm font-medium text-gray-700">Subcategory *</label>
                        <select id="subCategoryId" name="subCategoryId" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                            <option value="">-- Select Subcategory --</option>
                            <c:forEach items="${subcategories}" var="subcategory">
                                <option value="${subcategory.subCategoryId}" data-category="${subcategory.categoryId}">
                                    ${subcategory.subCategoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <!-- Status -->
                    <div>
                        <label for="statusId" class="block text-sm font-medium text-gray-700">Status *</label>
                        <select id="statusId" name="statusId" required
                                class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                            <option value="">-- Select Status --</option>
                            <c:forEach items="${statuses}" var="status">
                                <option value="${status.statusId}">${status.statusName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                
                <!-- Right Column -->
                <div class="space-y-4">
                    <!-- Quantity -->
                    <div>
                        <label for="quantity" class="block text-sm font-medium text-gray-700">Quantity *</label>
                        <input type="number" id="quantity" name="quantity" min="0" required
                               class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                    </div>
                    
                    <!-- Minimum Quantity -->
                    <div>
                        <label for="minQuantity" class="block text-sm font-medium text-gray-700">Minimum Quantity *</label>
                        <input type="number" id="minQuantity" name="minQuantity" min="0" required
                               class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                    </div>
                    
                    <!-- Price -->
                    <div>
                        <label for="price" class="block text-sm font-medium text-gray-700">Price (VND) *</label>
                        <input type="number" id="price" name="price" min="0" step="0.01" required
                               class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500">
                    </div>
                    
                    <!-- Image Upload -->
                    <div>
                        <label for="image" class="block text-sm font-medium text-gray-700">Image</label>
                        <input type="file" id="image" name="image" accept="image/*"
                               class="mt-1 block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100">
                    </div>
                </div>
            </div>
            
            <!-- Description -->
            <div>
                <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                <textarea id="description" name="description" rows="3"
                          class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"></textarea>
            </div>
            
            <!-- Buttons -->
            <div class="flex justify-end space-x-4 pt-4">
                <a href="${pageContext.request.contextPath}/materiallist" 
                   class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    Cancel
                </a>
                <button type="submit" 
                        class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    Add Material
                </button>
            </div>
        </form>
    </div>
</div>

<!-- JavaScript for dynamic subcategory loading -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const categorySelect = document.getElementById('categoryId');
    const subcategorySelect = document.getElementById('subCategoryId');
    
    // Store all subcategories
    const allSubcategories = Array.from(subcategorySelect.options);
    
    // Filter subcategories when category changes
    categorySelect.addEventListener('change', function() {
        const selectedCategoryId = this.value;
        
        // Clear and add default option
        subcategorySelect.innerHTML = '<option value="">-- Select Subcategory --</option>';
        
        if (!selectedCategoryId) return;
        
        // Filter and add matching subcategories
        allSubcategories.forEach(option => {
            if (option.value && option.dataset.category === selectedCategoryId) {
                subcategorySelect.add(option);
            }
        });
    });
});
</script>
