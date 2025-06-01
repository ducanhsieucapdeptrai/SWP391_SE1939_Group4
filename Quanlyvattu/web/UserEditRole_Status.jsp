<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-3xl mx-auto bg-white p-6 rounded shadow">
    <h2 class="text-2xl font-semibold text-gray-800 mb-6">Edit User Role & Status</h2>

    <form action="user-edit" method="post" class="space-y-5">
        <input type="hidden" name="userId" value="${user.userId}" />

        <div class="flex items-center">
            <label class="w-40 font-medium text-gray-600">Full Name:</label>
            <span class="text-gray-900">${user.fullName}</span>
        </div>

        <div class="flex items-center">
            <label class="w-40 font-medium text-gray-600">Email:</label>
            <span class="text-gray-900">${user.email}</span>
        </div>

        <div class="flex items-center">
            <label class="w-40 font-medium text-gray-600">Phone:</label>
            <span class="text-gray-900">${user.phone}</span>
        </div>

        <div>
            <label for="roleId" class="block font-medium text-gray-600 mb-1">Role:</label>
            <select id="roleId" name="roleId" class="w-full border border-gray-300 rounded px-3 py-2">
                <c:forEach var="role" items="${roles}">
                    <option value="${role.roleId}" <c:if test="${role.roleId == user.roleId}">selected</c:if>>
                        ${role.roleName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div>
            <label for="isActive" class="block font-medium text-gray-600 mb-1">Status:</label>
            <select id="isActive" name="isActive" class="w-full border border-gray-300 rounded px-3 py-2">
                <option value="true"  ${user.isActive ? 'selected' : ''}>Active</option>
                <option value="false" ${!user.isActive ? 'selected' : ''}>Inactive</option>
            </select>
        </div>

        <div class="flex justify-end space-x-3 pt-4">
            <a href="user-detail?id=${user.userId}" class="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded text-sm">
                Back to Detail
            </a>
            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded text-sm">
                Save Changes
            </button>
        </div>
    </form>
</div>
