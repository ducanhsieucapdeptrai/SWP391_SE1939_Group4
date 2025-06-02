<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-7xl mx-auto bg-white p-6 rounded-lg shadow-md">
    <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold text-gray-800">User List</h1>
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
                    <th class="p-3 text-left">ID</th>
                    <th class="p-3 text-left">Full Name</th>
                    <th class="p-3 text-left">Email</th>
                    <th class="p-3 text-left">Role</th>
                    <th class="p-3 text-left">Status</th>
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
                            <a href="user-delete?id=${user.userId}" class="text-red-600 hover:underline" onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
