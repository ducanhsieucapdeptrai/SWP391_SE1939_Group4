<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-semibold">Category Management</h2>
        <div>
            <a href="${pageContext.request.contextPath}/advanced-dashboard" class="inline-flex items-center text-blue-600 hover:text-blue-800">
                <i class="fas fa-arrow-left mr-2"></i> Back to Advanced Dashboard
            </a>
        </div>
    </div>
    
    <!-- Success/Error Message Display -->
    <c:if test="${not empty successMessage}">
        <div class="bg-green-100 border-l-4 border-green-500 text-green-700 p-4 mb-6" role="alert">
            <p>${successMessage}</p>
        </div>
    </c:if>
    
    <c:if test="${not empty errorMessage}">
        <div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
            <p>${errorMessage}</p>
        </div>
    </c:if>
    
    <!-- Search and Sort Section -->
    <div class="flex justify-between items-center mb-6">
        <div class="flex-grow mr-4">
            <form action="${pageContext.request.contextPath}/category-management" method="get" class="flex">
                <input type="text" name="search" placeholder="Search categories" 
                       value="${searchTerm}" 
                       class="w-full px-3 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-r-md">
                    <i class="fas fa-search"></i>
                </button>
            </form>
        </div>
        
        <div>
            <select onchange="location.href='${pageContext.request.contextPath}/category-management?sortBy=' + this.value" 
                    class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                <option value="CategoryId" ${sortBy == 'CategoryId' ? 'selected' : ''}>Sort by ID</option>
                <option value="CategoryName" ${sortBy == 'CategoryName' ? 'selected' : ''}>Sort by Name</option>
            </select>
        </div>
        
        <div class="ml-4">
            <button type="button" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg transition duration-300" 
                    onclick="openAddModal()">
                <i class="fas fa-plus mr-2"></i> Add New Category
            </button>
        </div>
    </div>
    
    <!-- Catalog Items Table -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <a href="${pageContext.request.contextPath}/category-management?sortBy=CategoryId" class="flex items-center">
                            ID 
                            <c:if test="${sortBy == 'CategoryId'}">
                                <i class="fas fa-sort-down ml-2"></i>
                            </c:if>
                        </a>
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        <a href="${pageContext.request.contextPath}/category-management?sortBy=CategoryName" class="flex items-center">
                            Name 
                            <c:if test="${sortBy == 'CategoryName'}">
                                <i class="fas fa-sort-down ml-2"></i>
                            </c:if>
                        </a>
                    </th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <c:choose>
                    <c:when test="${not empty categories}">
                        <c:forEach var="category" items="${categories}">
                            <tr class="hover:bg-gray-50">
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${category.categoryId}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${category.categoryName}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <button type="button" class="text-blue-600 hover:text-blue-900 mr-3" 
                                            data-id="${category.categoryId}" data-name="${category.categoryName}" onclick="editItem(this)">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button type="button" class="text-red-600 hover:text-red-900" 
                                            data-id="${category.categoryId}" data-name="${category.categoryName}" onclick="deleteItem(this)">
                                        <i class="fas fa-trash-alt"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3" class="px-6 py-4 text-center text-sm text-gray-500">
                                No categories found. Add your first category using the button above.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<!-- Add Modal -->
<div id="addModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold">Add New Category</h3>
            <button type="button" class="text-gray-400 hover:text-gray-600" onclick="closeAddModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <form action="${pageContext.request.contextPath}/category-management" method="post">
            <input type="hidden" name="action" value="add">
            
            <div class="mb-4">
                <label for="addName" class="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input type="text" id="addName" name="name" required
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            
            <div class="flex justify-end">
                <button type="button" class="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg mr-2" 
                        onclick="closeAddModal()">
                    Cancel
                </button>
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg">
                    Add Category
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Modal -->
<div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold">Edit Category</h3>
            <button type="button" class="text-gray-400 hover:text-gray-600" onclick="closeEditModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <form action="${pageContext.request.contextPath}/category-management" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="editId" name="id">
            
            <div class="mb-4">
                <label for="editName" class="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input type="text" id="editName" name="name" required
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            
            <div class="flex justify-end">
                <button type="button" class="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg mr-2" 
                        onclick="closeEditModal()">
                    Cancel
                </button>
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg">
                    Update Category
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold">Confirm Deletion</h3>
            <button type="button" class="text-gray-400 hover:text-gray-600" onclick="closeDeleteModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <p class="mb-4">Are you sure you want to delete the category: <span id="deleteItemName" class="font-semibold"></span>?</p>
        <form action="${pageContext.request.contextPath}/category-management" method="post">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" id="deleteId" name="id">
            
            <div class="flex justify-end">
                <button type="button" class="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg mr-2" 
                        onclick="closeDeleteModal()">
                    Cancel
                </button>
                <button type="submit" class="bg-red-600 hover:bg-red-700 text-white py-2 px-4 rounded-lg">
                    Delete
                </button>
            </div>
        </form>
    </div>
</div>

<!-- JavaScript for Modal Handling -->
<script>
    // Add Modal Functions
    function openAddModal() {
        document.getElementById('addModal').classList.remove('hidden');
    }
    
    function closeAddModal() {
        document.getElementById('addModal').classList.add('hidden');
        document.getElementById('addName').value = '';
    }
    
    // Edit Modal Functions
    function editItem(button) {
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        
        document.getElementById('editId').value = id;
        document.getElementById('editName').value = name;
        document.getElementById('editModal').classList.remove('hidden');
    }
    
    function closeEditModal() {
        document.getElementById('editModal').classList.add('hidden');
    }
    
    // Delete Modal Functions
    function deleteItem(button) {
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        
        document.getElementById('deleteId').value = id;
        document.getElementById('deleteItemName').textContent = name;
        document.getElementById('deleteModal').classList.remove('hidden');
    }
    
    function closeDeleteModal() {
        document.getElementById('deleteModal').classList.add('hidden');
    }
    
    // Close modals when clicking outside
    window.addEventListener('click', function(event) {
        if (event.target === document.getElementById('addModal')) {
            closeAddModal();
        }
        if (event.target === document.getElementById('editModal')) {
            closeEditModal();
        }
        if (event.target === document.getElementById('deleteModal')) {
            closeDeleteModal();
        }
    });
</script>
