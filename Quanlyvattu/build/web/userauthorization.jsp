<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, model.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="max-w-7xl mx-auto bg-white p-6 rounded-lg shadow-md">
    <div class="flex justify-between items-center mb-4">
        <h1 class="text-2xl font-bold text-gray-800">User Authorization</h1>
        <a href="${pageContext.request.contextPath}/dashboard" class="text-sm bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">Back to HomePage</a>
    </div>

    <form method="post" action="user-matrix" class="space-y-8">
        <c:forEach var="module" items="${modules}">
            <div class="border rounded-lg shadow p-4 bg-white">
                <h2 class="text-lg font-semibold text-gray-700 mb-2">${module.moduleName}</h2>

                <table class="min-w-full text-sm border rounded">
                    <thead class="bg-gray-100 text-gray-700">
                        <tr>
                            <th class="p-2 text-left">Function</th>
                                <c:forEach var="role" items="${roles}">
                                <th class="p-2 text-left">${role.roleName}</th>
                                </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="func" items="${functions}">
                            <c:if test="${func.moduleId == module.moduleId}">
                                <tr class="border-t">
                                    <td class="p-2">${func.functionName}</td>
                                    <c:forEach var="role" items="${roles}">
                                        <td class="p-2">
                                            <c:set var="permissionKey" value="${role.roleId}:${func.functionId}" />
                                            <input type="checkbox"
                                                   name="perm"
                                                   value="${permissionKey}"
                                                   class="form-checkbox"
                                                   <c:if test="${roleFunctionPairs.contains(permissionKey)}">checked</c:if>
                                                   <c:if test="${role.roleId == 1 && func.functionId == 34}">disabled</c:if> />
                                            </td>
                                    </c:forEach>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:forEach>

        <div class="text-right">
            <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">Save Permissions</button>
        </div>
    </form>
</div>
