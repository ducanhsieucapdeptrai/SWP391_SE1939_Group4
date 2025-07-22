<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="max-w-4xl mx-auto p-6 bg-white rounded-lg shadow border border-gray-200">
    <c:if test="${not empty successMsg}">
        <div class="bg-green-100 text-green-800 px-4 py-2 rounded mb-6 border border-green-200 text-sm">
            ${successMsg}
        </div>
    </c:if>

    <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold text-gray-800">Project Detail</h2>
        <div class="space-x-2">
            <a href="project?action=form&id=${project.projectId}" class="inline-block bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600 text-sm">
                <i class="fas fa-edit mr-1"></i> Edit
            </a>
            <a href="project-delete?id=${project.projectId}" onclick="return confirm('Are you sure you want to delete this project?')" 
               class="inline-block bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 text-sm">
                <i class="fas fa-trash mr-1"></i> Delete
            </a>
            <a href="project" class="inline-block bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 text-sm">
                Back to List
            </a>
        </div>
    </div>

    <table class="w-full text-sm text-left text-gray-700 mb-6">
        <tbody>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium w-1/4 bg-gray-50">Project Name</th>
                <td class="py-2 px-4">${project.projectName}</td>
            </tr>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium bg-gray-50">Description</th>
                <td class="py-2 px-4">${project.description}</td>
            </tr>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium bg-gray-50">Start Date</th>
                <td class="py-2 px-4">
                    <fmt:formatDate value="${project.startDate}" pattern="dd/MM/yyyy" />
                </td>
            </tr>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium bg-gray-50">End Date</th>
                <td class="py-2 px-4">
                    <fmt:formatDate value="${project.endDate}" pattern="dd/MM/yyyy" />
                </td>
            </tr>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium bg-gray-50">Manager</th>
                <td class="py-2 px-4">${project.managerName}</td>
            </tr>
            <tr class="border-b">
                <th class="py-2 px-4 font-medium bg-gray-50">Status</th>
                <td class="py-2 px-4">${project.status}</td>
            </tr>
            <tr>
                <th class="py-2 px-4 font-medium bg-gray-50">Attachment</th>
                <td class="py-2 px-4">
                    <c:choose>
                        <c:when test="${not empty project.attachmentPath}">
                            <a href="${pageContext.request.contextPath}/${project.attachmentPath}" 
                               target="_blank" class="text-blue-600 hover:underline">
                                Download Attachment
                            </a>
                        </c:when>
                        <c:otherwise>
                            <span class="text-gray-500 italic">No attachment</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </tbody>
    </table>

    <div class="flex justify-between items-center mt-10 mb-4">
        <h3 class="text-xl font-semibold text-gray-800">Related Requests</h3>

        <c:if test="${canConfirmCompletion}">
            <c:choose>
                <c:when test="${project.status eq 'Completed'}">
                    <button type="button" disabled
                            class="bg-gray-300 text-gray-500 px-4 py-2 rounded-lg text-sm font-medium shadow-sm cursor-not-allowed">
                        ✅ Project Completed
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="button"
                            onclick="showConfirmationModal()"
                            class="bg-emerald-600 hover:bg-emerald-700 text-white px-4 py-2 rounded-lg text-sm font-medium shadow-sm transition">
                        ✅ Complete Project
                    </button>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>

    <div class="bg-white border border-gray-200 rounded shadow overflow-x-auto">
        <table class="min-w-full text-sm text-left">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="px-4 py-2">Request ID</th>
                    <th class="px-4 py-2">Type</th>
                    <th class="px-4 py-2">Requested By</th>
                    <th class="px-4 py-2">Status</th>
                    <th class="px-4 py-2">Created Date</th>
                    <th class="px-4 py-2">Details</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="r" items="${requestList}">
                    <tr class="border-t hover:bg-gray-50">
                        <td class="px-4 py-2">${r.requestId}</td>
                        <td class="px-4 py-2">${r.requestTypeName}</td>
                        <td class="px-4 py-2">${r.requestedByName}</td>
                        <td class="px-4 py-2">${r.status}</td>
                        <td class="px-4 py-2">
                            <fmt:formatDate value="${r.requestDate}" pattern="dd/MM/yyyy" />
                        </td>
                        <td class="px-4 py-2">
                            <a href="request-detail?id=${r.requestId}" 
                               class="inline-block bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700 text-sm">
                                View
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty requestList}">
                    <tr>
                        <td colspan="6" class="text-center text-gray-500 py-4">No requests found for this project.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<!-- Modal -->
<div id="confirmModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 hidden">
    <div class="bg-white rounded-lg shadow-lg p-6 max-w-md w-full">
        <h3 class="text-lg font-semibold text-gray-800 mb-4">Confirm Completion</h3>
        <p class="text-gray-700 mb-6">Are you sure you want to mark this project as <strong>Completed</strong>? This action cannot be undone.</p>

        <div class="flex justify-end space-x-3">
            <button onclick="hideConfirmationModal()"
                    class="px-4 py-2 bg-gray-300 text-gray-800 rounded hover:bg-gray-400">
                Cancel
            </button>

            <form id="confirmForm" action="projectDetail" method="post">
                <input type="hidden" name="action" value="confirmComplete" />
                <input type="hidden" name="projectId" value="${project.projectId}" />
                <button type="submit"
                        class="px-4 py-2 bg-emerald-600 text-white rounded hover:bg-emerald-700">
                    Confirm
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    function showConfirmationModal() {
        document.getElementById("confirmModal").classList.remove("hidden");
    }

    function hideConfirmationModal() {
        document.getElementById("confirmModal").classList.add("hidden");
    }
</script>
    