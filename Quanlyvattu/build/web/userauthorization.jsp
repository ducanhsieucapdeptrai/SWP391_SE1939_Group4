<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Notification -->
<c:if test="${not empty sessionScope.message}">
    <div id="messageNotification" class="message-notification ${sessionScope.messageType}">
        <div class="p-3">
            <strong>
                <c:choose>
                    <c:when test="${sessionScope.messageType == 'success'}">
                        ✓ Success!
                    </c:when>
                    <c:otherwise>
                        ✗ Error!
                    </c:otherwise>
                </c:choose>
            </strong>
            <div class="mt-1">${sessionScope.message}</div>
        </div>
    </div>
    <%-- Remove message from session after displaying --%>
    <c:remove var="message" scope="session"/>
    <c:remove var="messageType" scope="session"/>
</c:if>

<div class="max-w-7xl mx-auto bg-white p-6 rounded-lg shadow-md">
    <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-gray-800">Authorization Matrix Management</h1>
        <a href="${pageContext.request.contextPath}/dashboard" class="text-sm bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Back to HomePage</a>
    </div>

    <!-- Filter Controls -->
    <div class="bg-gray-50 p-4 rounded-lg mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <!-- Role Selection -->
            <div>
                <label for="roleSelect" class="block text-sm font-medium text-gray-700 mb-2">1. Choose Role:</label>
                <select id="roleSelect" name="roleSelect" class="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500">
                    <option value="">-- Select Role --</option>
                    <c:forEach var="role" items="${roles}">
                        <option value="${role.roleId}" ${param.selectedRole == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Status Filter -->
            <div>
                <label for="statusSelect" class="block text-sm font-medium text-gray-700 mb-2">2. Choose Status:</label>
                <select id="statusSelect" name="statusSelect" class="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500">
                    <option value="all" ${param.selectedStatus == 'all' ? 'selected' : ''}>All</option>
                    <option value="active" ${param.selectedStatus == 'active' ? 'selected' : ''}>Active</option>
                    <option value="inactive" ${param.selectedStatus == 'inactive' ? 'selected' : ''}>Inactive</option>
                </select>
            </div>

            <!-- Filter Button -->
            <div class="flex items-end">
                <button type="button" id="filterBtn" class="w-full bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                    <i class="fas fa-filter mr-2"></i>Filter
                </button>
            </div>
        </div>
    </div>

    <!-- Authorization Tables -->
    <div id="authorizationTables">
        <c:choose>
            <c:when test="${not empty param.selectedRole}">
                <form method="post" action="user-matrix" class="space-y-6">
                    <input type="hidden" name="selectedRole" value="${param.selectedRole}">
                    <input type="hidden" name="selectedStatus" value="${param.selectedStatus}">
                    
                    <div class="mb-4">
                        <h3 class="text-lg font-semibold text-gray-700">3. Table Modules</h3>
                        <p class="text-sm text-gray-600">Manage permissions for: <span class="font-medium text-blue-600">${selectedRoleName}</span></p>
                    </div>

                    <c:forEach var="module" items="${modules}">
                        <c:set var="hasVisibleFunctions" value="false" />
                        <c:forEach var="func" items="${functions}">
                            <c:if test="${func.moduleId == module.moduleId}">
                                <c:set var="permissionKey" value="${param.selectedRole}:${func.functionId}" />
                                <c:set var="isActive" value="${roleFunctionPairs.contains(permissionKey)}" />
                                <c:if test="${param.selectedStatus == 'all' || (param.selectedStatus == 'active' && isActive) || (param.selectedStatus == 'inactive' && !isActive)}">
                                    <c:set var="hasVisibleFunctions" value="true" />
                                </c:if>
                            </c:if>
                        </c:forEach>

                        <c:if test="${hasVisibleFunctions}">
                            <div class="border rounded-lg shadow p-4 bg-white">
                                <h4 class="text-md font-semibold text-gray-700 mb-3">${module.moduleName}</h4>
                                
                                <table class="min-w-full text-sm border rounded">
                                    <thead class="bg-gray-100 text-gray-700">
                                        <tr>
                                            <th class="p-3 text-left w-16">STT</th>
                                            <th class="p-3 text-left">FunctionName</th>
                                            <th class="p-3 text-center w-24">4. IsActive</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="stt" value="1" />
                                        <c:forEach var="func" items="${functions}">
                                            <c:if test="${func.moduleId == module.moduleId}">
                                                <c:set var="permissionKey" value="${param.selectedRole}:${func.functionId}" />
                                                <c:set var="isActive" value="${roleFunctionPairs.contains(permissionKey)}" />
                                                
                                                <c:if test="${param.selectedStatus == 'all' || (param.selectedStatus == 'active' && isActive) || (param.selectedStatus == 'inactive' && !isActive)}">
                                                    <tr class="border-t hover:bg-gray-50">
                                                        <td class="p-3 text-center font-medium">${stt}</td>
                                                        <td class="p-3">${func.functionName}</td>
                                                        <td class="p-3 text-center">
                                                            <input type="checkbox"
                                                                   name="perm"
                                                                   value="${permissionKey}"
                                                                   class="form-checkbox h-4 w-4 text-blue-600 rounded focus:ring-blue-500"
                                                                   ${isActive ? 'checked' : ''} />
                                                        </td>
                                                    </tr>
                                                    <c:set var="stt" value="${stt + 1}" />
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </c:forEach>

                    <!-- Save Button -->
                    <div class="text-right pt-4">
                        <button type="submit" class="bg-blue-600 text-white px-6 py-3 rounded hover:bg-blue-700 font-medium">
                            <i class="fas fa-save mr-2"></i>5. Save Changes
                        </button>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="text-center py-12 bg-gray-50 rounded-lg">
                    <i class="fas fa-user-shield text-4xl text-gray-400 mb-4"></i>
                    <p class="text-gray-600">Please select a role to view and manage permissions</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- JavaScript -->
<script>
document.addEventListener('DOMContentLoaded', function () {
    // Message notification auto-hide
    const messageNotification = document.getElementById('messageNotification');
    if (messageNotification) {
        setTimeout(() => {
            messageNotification.classList.add('show');
        }, 100);

        setTimeout(() => {
            messageNotification.classList.remove('show');
            setTimeout(() => {
                messageNotification.remove();
            }, 300);
        }, 3000);
    }

    // Filter functionality
    const filterBtn = document.getElementById('filterBtn');
    const roleSelect = document.getElementById('roleSelect');
    const statusSelect = document.getElementById('statusSelect');

    filterBtn.addEventListener('click', function() {
        const selectedRole = roleSelect.value;
        const selectedStatus = statusSelect.value;
        
        if (!selectedRole) {
            alert('Please select a role first');
            return;
        }

        // Redirect with parameters
        const url = new URL(window.location.href);
        url.searchParams.set('selectedRole', selectedRole);
        url.searchParams.set('selectedStatus', selectedStatus);
        window.location.href = url.toString();
    });
});
</script>
