<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-7xl mx-auto bg-white p-6 rounded-lg shadow-md">
    <!-- Filters and Search Form -->
    <div class="mb-6">
        <form action="userlist" method="get" class="p-4 bg-gray-50 rounded-lg shadow-sm">
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
                <!-- Search Input -->
                <div>
                    <label for="search" class="block text-sm font-medium text-gray-700">Search</label>
                    <input type="text" name="search" id="search" placeholder="Name or email..." 
                           value="${searchQuery}" 
                           class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm">
                </div>
                <!-- Role Filter -->
                <div>
                    <label for="roleId" class="block text-sm font-medium text-gray-700">Role</label>
                    <select name="roleId" id="roleId" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                        <option value="0">All Roles</option>
                        <c:forEach var="role" items="${roleList}">
                            <option value="${role.roleId}" ${role.roleId == roleId ? 'selected' : ''}>${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <!-- Status Filter -->
                <div>
                    <label for="status" class="block text-sm font-medium text-gray-700">Status</label>
                    <select name="status" id="status" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                        <option value="-1" ${status == -1 ? 'selected' : ''}>All Statuses</option>
                        <option value="1" ${status == 1 ? 'selected' : ''}>Active</option>
                        <option value="0" ${status == 0 ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <!-- Submit Button -->
                <div>
                    <button type="submit" class="w-full inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Filter
                    </button>
                    <a href="userlist" class="w-full inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Reset
                    </a>
                </div>
            </div>
        </form>
    </div>

    <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold text-gray-800">User List</h1>
        <a href="${pageContext.request.contextPath}/add-user"
           class="text-sm bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
            + Add New User
        </a>
        <a href="${pageContext.request.contextPath}/dashboard" class="text-sm bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Back to HomePage</a>
    </div>

    <!-- Thông báo -->
    <c:if test="${param.message != null}">
        <div class="mb-4">
            <c:choose>
                <c:when test="${param.message eq 'delete_success'}">
                    <div class="bg-green-100 text-green-800 p-3 rounded">User deleted successfully!</div>
                </c:when>
                <c:when test="${param.message eq 'delete_error'}">
                    <div class="bg-red-100 text-red-800 p-3 rounded">An error occurred while deleting the user!</div>
                </c:when>
                <c:when test="${param.message eq 'invalid_id'}">
                    <div class="bg-yellow-100 text-yellow-800 p-3 rounded">Invalid user ID!</div>
                </c:when>
                <c:when test="${param.message eq 'missing_id'}">
                    <div class="bg-yellow-100 text-yellow-800 p-3 rounded">User ID is missing!</div>
                </c:when>
            </c:choose>
        </div>
    </c:if>

    <!-- Bảng danh sách -->
    <div class="overflow-x-auto">
        <table class="min-w-full border text-sm">
            <thead class="bg-gray-200 text-gray-600">
                <tr>
                    <th class="p-3 text-left"><a href="userlist?sortBy=userId&sortOrder=${sortBy == 'userId' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&search=${searchQuery}&roleId=${roleId}&status=${status}">ID <i class="fas fa-sort"></i></a></th>
                    <th class="p-3 text-left"><a href="userlist?sortBy=fullName&sortOrder=${sortBy == 'fullName' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&search=${searchQuery}&roleId=${roleId}&status=${status}">Full Name <i class="fas fa-sort"></i></a></th>
                    <th class="p-3 text-left"><a href="userlist?sortBy=email&sortOrder=${sortBy == 'email' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&search=${searchQuery}&roleId=${roleId}&status=${status}">Email <i class="fas fa-sort"></i></a></th>
                    <th class="p-3 text-left"><a href="userlist?sortBy=roleId&sortOrder=${sortBy == 'roleId' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&search=${searchQuery}&roleId=${roleId}&status=${status}">Role <i class="fas fa-sort"></i></a></th>
                    <th class="p-3 text-left"><a href="userlist?sortBy=isActive&sortOrder=${sortBy == 'isActive' && sortOrder == 'ASC' ? 'DESC' : 'ASC'}&search=${searchQuery}&roleId=${roleId}&status=${status}">Status <i class="fas fa-sort"></i></a></th>
                    <th class="p-3 text-left">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${userList}" var="user">
                    <tr class="border-t hover:bg-gray-50">
                        <td class="p-3 font-medium text-gray-800">${user.userId}</td>
                        <td class="p-3">${user.fullName}</td>
                        <td class="p-3">${user.email}</td>
                        <td class="p-3">
                            <c:forEach items="${roleList}" var="role">
                                <c:if test="${role.roleId == user.roleId}">
                                    ${role.roleName}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td class="p-3">
                            <c:choose>
                                <c:when test="${user.isActive}">
                                    <span class="bg-green-500 text-white px-2 py-1 rounded text-xs">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="bg-red-500 text-white px-2 py-1 rounded text-xs">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="p-3">
                            <a href="user-detail?id=${user.userId}" class="text-blue-600 hover:underline mr-2">Details</a>
                            <c:if test="${sessionScope.currentUser.userId != user.userId && user.roleId != 1}">
                                <a href="user-delete?id=${user.userId}" class="text-red-600 hover:underline" onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <div class="mt-6 flex justify-center">
        <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
            <c:if test="${currentPage > 1}">
                <a href="userlist?page=${currentPage - 1}&search=${searchQuery}&roleId=${roleId}&status=${status}&sortBy=${sortBy}&sortOrder=${sortOrder}" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                    <span>Previous</span>
                </a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="userlist?page=${i}&search=${searchQuery}&roleId=${roleId}&status=${status}&sortBy=${sortBy}&sortOrder=${sortOrder}" 
                   class="${i == currentPage ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'} relative inline-flex items-center px-4 py-2 border text-sm font-medium">
                    ${i}
                </a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="userlist?page=${currentPage + 1}&search=${searchQuery}&roleId=${roleId}&status=${status}&sortBy=${sortBy}&sortOrder=${sortOrder}" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
                    <span>Next</span>
                </a>
            </c:if>
        </nav>
    </div>

