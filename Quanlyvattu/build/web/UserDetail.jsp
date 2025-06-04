<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="user-detail-container bg-white p-6 rounded shadow-md w-full max-w-3xl mx-auto">
    <h1 class="text-2xl font-semibold text-gray-800 mb-6">User Details</h1>

    <div class="info-section mb-8">
        <h2 class="text-xl font-medium text-gray-700 border-b pb-2 mb-4">Basic Information</h2>

        <div class="flex flex-col md:flex-row gap-10 items-start">
            <!-- Khung ảnh chữ nhật -->
            <div class="w-52 h-60 bg-gray-100 rounded-md overflow-hidden shadow border border-gray-300">
                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${user.userImage}" 
                     alt="Avatar"
                     class="w-full h-full object-cover transition-transform duration-300 hover:scale-105">
            </div>

            <!-- Thông tin người dùng -->
            <div class="flex-1 space-y-4">
                <div class="info-row flex">
                    <div class="info-label w-40 font-semibold text-gray-600">Full Name:</div>
                    <div class="info-value text-gray-800">${user.fullName}</div>
                </div>

                <div class="info-row flex">
                    <div class="info-label w-40 font-semibold text-gray-600">Email:</div>
                    <div class="info-value text-gray-800">${user.email}</div>
                </div>

                <div class="info-row flex">
                    <div class="info-label w-40 font-semibold text-gray-600">Phone:</div>
                    <div class="info-value text-gray-800">${user.phone}</div>
                </div>

                <div class="info-row flex">
                    <div class="info-label w-40 font-semibold text-gray-600">Role:</div>
                    <div class="info-value text-gray-800">
                        <c:forEach items="${roleList}" var="role">
                            <c:if test="${role.roleId == user.roleId}">
                                ${role.roleName}
                            </c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="info-row flex">
                    <div class="info-label w-40 font-semibold text-gray-600">Status:</div>
                    <div class="info-value">
                        <c:choose>
                            <c:when test="${user.isActive}">
                                <span class="bg-green-500 text-white px-3 py-1 rounded">Active</span>
                            </c:when>
                            <c:otherwise>
                                <span class="bg-red-500 text-white px-3 py-1 rounded">Inactive</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <div class="mt-6">
            <a href="userlist" class="inline-block bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded mr-2 text-sm">Back to List</a>
            <a href="user-edit?id=${user.userId}" class="inline-block bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded text-sm">Edit</a>
        </div>
    </div>
</div>
