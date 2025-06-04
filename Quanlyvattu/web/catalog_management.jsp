<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container mx-auto">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-semibold">Catalog Management</h2>
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
    
    <!-- Add New Catalog Item Button -->
    <div class="mb-6">
        <button type="button" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg transition duration-300" 
                onclick="openAddModal()">
            <i class="fas fa-plus mr-2"></i> Add New Catalog Item
        </button>
    </div>
    
    <!-- Catalog Items Table -->
    <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
                <tr>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                    <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
                <c:choose>
                    <c:when test="${not empty catalogItems}">
                        <c:forEach var="item" items="${catalogItems}">
                            <tr class="hover:bg-gray-50">
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${item.id}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${item.name}</td>
                                <td class="px-6 py-4 text-sm text-gray-500">${item.description}</td>
                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <button type="button" class="text-blue-600 hover:text-blue-900 mr-3" 
                                            data-id="${item.id}" data-name="${item.name}" data-description="${item.description}" onclick="editItem(this)">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button type="button" class="text-red-600 hover:text-red-900" 
                                            data-id="${item.id}" data-name="${item.name}" onclick="deleteItem(this)">
                                        <i class="fas fa-trash-alt"></i> Delete
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">
                                No catalog items found. Add your first item using the button above.
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
            <h3 class="text-lg font-semibold">Add New Catalog Item</h3>
            <button type="button" class="text-gray-400 hover:text-gray-600" onclick="closeAddModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <form action="${pageContext.request.contextPath}/catalog-management" method="post">
            <input type="hidden" name="action" value="add">
            
            <div class="mb-4">
                <label for="addName" class="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input type="text" id="addName" name="name" required
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            
            <div class="mb-4">
                <label for="addDescription" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea id="addDescription" name="description" rows="3"
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"></textarea>
            </div>
            
            <div class="flex justify-end">
                <button type="button" class="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg mr-2" 
                        onclick="closeAddModal()">
                    Cancel
                </button>
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg">
                    Add Item
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Modal -->
<div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md">
        <div class="flex justify-between items-center mb-4">
            <h3 class="text-lg font-semibold">Edit Catalog Item</h3>
            <button type="button" class="text-gray-400 hover:text-gray-600" onclick="closeEditModal()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <form action="${pageContext.request.contextPath}/catalog-management" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" id="editId" name="id">
            
            <div class="mb-4">
                <label for="editName" class="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input type="text" id="editName" name="name" required
                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500">
            </div>
            
            <div class="mb-4">
                <label for="editDescription" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                <textarea id="editDescription" name="description" rows="3"
                          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"></textarea>
            </div>
            
            <div class="flex justify-end">
                <button type="button" class="bg-gray-200 hover:bg-gray-300 text-gray-800 py-2 px-4 rounded-lg mr-2" 
                        onclick="closeEditModal()">
                    Cancel
                </button>
                <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded-lg">
                    Update Item
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
        <p class="mb-4">Are you sure you want to delete the catalog item: <span id="deleteItemName" class="font-semibold"></span>?</p>
        <form action="${pageContext.request.contextPath}/catalog-management" method="post">
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
        document.getElementById('addDescription').value = '';
    }
    
    // Edit Modal Functions
    function editItem(button) {
        const id = button.getAttribute('data-id');
        const name = button.getAttribute('data-name');
        const description = button.getAttribute('data-description');
        
        document.getElementById('editId').value = id;
        document.getElementById('editName').value = name;
        document.getElementById('editDescription').value = description;
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
