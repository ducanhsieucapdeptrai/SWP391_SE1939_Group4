<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="max-w-6xl mx-auto p-6">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">Project List</h2>
        <a href="project?action=form" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
            + New Project
        </a>
    </div>

    <!-- Filter Form -->
    <form method="get" action="project" class="mb-6 bg-gray-50 p-4 rounded-lg border flex flex-wrap gap-4">
        <input type="hidden" name="action" value="list" />

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Project Name:</label>
            <input type="text" name="projectName" value="${projectName}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Manager Name:</label>
            <input type="text" name="managerName" value="${managerName}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Created Date:</label>
            <input type="date" name="createdDate" value="${createdDate}" class="rounded border-gray-300 px-3 py-2" />
        </div>

        <div class="flex flex-col">
            <label class="font-medium text-gray-700 mb-1">Status:</label>
            <select name="status" class="rounded border-gray-300 px-3 py-2">
                <option value="">All</option>
                <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                <option value="Completed" ${status == 'Completed' ? 'selected' : ''}>Completed</option>
                <option value="Inactive" ${status == 'Inactive' ? 'selected' : ''}>Inactive</option>
            </select>
        </div>

        <div class="flex items-end space-x-2">
            <button type="submit" class="bg-blue-600 text-white font-semibold px-4 py-2 rounded hover:bg-blue-700">
                Search
            </button>
            <a href="project" class="bg-gray-500 text-white font-semibold px-4 py-2 rounded hover:bg-gray-600">
                Clear Filter
            </a>
        </div>
    </form>

    <!-- Table -->
    <div class="bg-white shadow rounded border border-gray-200 overflow-x-auto">
        <table class="min-w-full text-sm text-left">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="px-4 py-2">ID</th>
                    <th class="px-4 py-2">Project Name</th>
                    <th class="px-4 py-2">Manager</th>
                    <th class="px-4 py-2">Created Date</th>
                    <th class="px-4 py-2">Status</th>
                    <th class="px-4 py-2">Details</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${projects}">
                    <tr class="border-t hover:bg-gray-50">
                        <td class="px-4 py-2 text-gray-800">${p.projectId}</td>
                        <td class="px-4 py-2 text-gray-800">${p.projectName}</td>
                        <td class="px-4 py-2 text-gray-800">${p.managerName}</td>
                        <td class="px-4 py-2 text-gray-700">
                            <fmt:formatDate value="${p.startDate}" pattern="dd/MM/yyyy" />
                        </td>
                        <td class="px-4 py-2">
                            <c:choose>
                                <c:when test="${p.status == 'Active'}">
                                    <span class="text-green-600 font-semibold">Active</span>
                                </c:when>
                                <c:when test="${p.status == 'Completed'}">
                                    <span class="text-gray-600 font-semibold">Completed</span>
                                </c:when>
                                <c:when test="${p.status == 'Inactive'}">
                                    <span class="text-red-500 font-semibold">Inactive</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-gray-500">${p.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="px-4 py-2 space-x-2">
                            <a href="projectDetail?id=${p.projectId}" 
                               class="inline-block bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 text-sm">
                                View
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty projects}">
                    <tr>
                        <td colspan="6" class="text-center text-gray-500 py-4">No projects found.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <c:set var="startPage" value="${currentPage - 2 < 1 ? 1 : currentPage - 2}" />
    <c:set var="endPage" value="${currentPage + 2 > totalPages ? totalPages : currentPage + 2}" />

    <div class="mt-6 flex flex-wrap items-center justify-center gap-2">
        <!-- Prev -->
        <form method="get" action="project">
            <input type="hidden" name="page" value="${currentPage - 1}" />
            <input type="hidden" name="projectName" value="${projectName}" />
            <input type="hidden" name="managerName" value="${managerName}" />
            <input type="hidden" name="createdDate" value="${createdDate}" />
            <input type="hidden" name="status" value="${status}" />
            <button
                class="px-4 py-2 rounded-md border font-semibold
                       ${currentPage == 1 ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed' 
                                          : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}"
                ${currentPage == 1 ? 'disabled' : ''}>
                « Prev
            </button>
        </form>

        <!-- First page -->
        <c:if test="${startPage > 1}">
            <form method="get" action="project">
                <input type="hidden" name="page" value="1" />
                <input type="hidden" name="projectName" value="${projectName}" />
                <input type="hidden" name="managerName" value="${managerName}" />
                <input type="hidden" name="createdDate" value="${createdDate}" />
                <input type="hidden" name="status" value="${status}" />
                <button class="px-3 py-2 rounded-md border font-medium bg-white text-gray-700 border-gray-300 hover:bg-gray-100">
                    1
                </button>
            </form>
            <span class="px-2 text-gray-500">...</span>
        </c:if>

        <!-- Middle pages -->
        <c:forEach var="i" begin="${startPage}" end="${endPage}">
            <form method="get" action="project">
                <input type="hidden" name="page" value="${i}" />
                <input type="hidden" name="projectName" value="${projectName}" />
                <input type="hidden" name="managerName" value="${managerName}" />
                <input type="hidden" name="createdDate" value="${createdDate}" />
                <input type="hidden" name="status" value="${status}" />
                <button
                    class="px-3 py-2 rounded-md border font-medium
                           ${i == currentPage ? 'bg-blue-600 text-white border-blue-600' 
                                              : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}">
                    ${i}
                </button>
            </form>
        </c:forEach>

        <!-- Last page -->
        <c:if test="${endPage < totalPages}">
            <span class="px-2 text-gray-500">...</span>
            <form method="get" action="project">
                <input type="hidden" name="page" value="${totalPages}" />
                <input type="hidden" name="projectName" value="${projectName}" />
                <input type="hidden" name="managerName" value="${managerName}" />
                <input type="hidden" name="createdDate" value="${createdDate}" />
                <input type="hidden" name="status" value="${status}" />
                <button class="px-3 py-2 rounded-md border font-medium bg-white text-gray-700 border-gray-300 hover:bg-gray-100">
                    ${totalPages}
                </button>
            </form>
        </c:if>

        <!-- Next -->
        <form method="get" action="project">
            <input type="hidden" name="page" value="${currentPage + 1}" />
            <input type="hidden" name="projectName" value="${projectName}" />
            <input type="hidden" name="managerName" value="${managerName}" />
            <input type="hidden" name="createdDate" value="${createdDate}" />
            <input type="hidden" name="status" value="${status}" />
            <button
                class="px-4 py-2 rounded-md border font-semibold
                       ${currentPage == totalPages ? 'bg-gray-100 text-gray-400 border-gray-200 cursor-not-allowed' 
                                                   : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-100'}"
                ${currentPage == totalPages ? 'disabled' : ''}>
                Next »
            </button>
        </form>
    </div>

    <div class="text-center text-sm text-gray-500 mt-2">
        Page ${currentPage} of ${totalPages}
    </div>
</div>
