<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="max-w-4xl mx-auto bg-white p-6 rounded-lg shadow border border-gray-200">
    <h2 class="text-2xl font-bold mb-6 text-gray-800">
        <c:choose>
            <c:when test="${project != null}">Edit Project</c:when>
            <c:otherwise>New Project</c:otherwise>
        </c:choose>
    </h2>
    <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 border border-red-400 px-4 py-2 rounded mb-4">
            ${error}
        </div>
    </c:if>
    <!-- ✅ THÊM enctype để upload file -->
    <form method="post" action="project" enctype="multipart/form-data">
        <c:if test="${not empty project and project.projectId > 0}">
            <input type="hidden" name="projectId" value="${project.projectId}" />
        </c:if>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block font-medium text-gray-700 mb-1">Project Name</label>
                <input type="text" name="projectName" value="${project.projectName}" required
                       class="w-full rounded border-gray-300 px-3 py-2" />
            </div>

            <div>
                <label class="block font-medium text-gray-700 mb-1">Status</label>
                <select name="status" class="w-full rounded border-gray-300 px-3 py-2">
                    <option value="Active" ${project.status == 'Active' ? 'selected' : ''}>Active</option>
                    <option value="Inactive" ${project.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                </select>
            </div>

            <div>
                <label class="block font-medium text-gray-700 mb-1">Start Date</label>
                <input type="date" name="startDate" value="${project.startDate}" required
                       class="w-full rounded border-gray-300 px-3 py-2" />
            </div>

            <div>
                <label class="block font-medium text-gray-700 mb-1">End Date</label>
                <input type="date" name="endDate" value="${project.endDate}" required
                       class="w-full rounded border-gray-300 px-3 py-2" />
            </div>

            <div class="md:col-span-2">
                <label class="block font-medium text-gray-700 mb-1">Manager</label>
                <select name="managerId" class="w-full rounded border-gray-300 px-3 py-2">
                    <c:forEach var="u" items="${managers}">
                        <option value="${u.userId}" ${u.userId == project.managerId ? 'selected' : ''}>
                            ${u.fullName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="md:col-span-2">
                <label class="block font-medium text-gray-700 mb-1">Description</label>
                <textarea name="description" rows="4"
                          class="w-full rounded border-gray-300 px-3 py-2">${project.description}</textarea>
            </div>

            <!-- ✅ THÊM PHẦN FILE UPLOAD -->
            <div class="md:col-span-2">
                <label class="block font-medium text-gray-700 mb-1">Attachment (optional)</label>
                <input type="file" name="attachment" class="w-full rounded border-gray-300 px-3 py-2" />
                
                <c:if test="${project != null && project.attachmentPath != null}">
                    <p class="text-sm text-gray-600 mt-1">
                        Current file:
                        <a href="${pageContext.request.contextPath}/${project.attachmentPath}" target="_blank"
                           class="text-blue-600 underline">Download</a>
                    </p>
                </c:if>
            </div>
        </div>

        <div class="mt-6 flex justify-end space-x-3">
            <a href="projectDetail?id=${project.projectId}" class="px-4 py-2 rounded bg-gray-500 text-white hover:bg-gray-600">Cancel</a>
            <button type="submit" class="px-6 py-2 rounded bg-blue-600 text-white hover:bg-blue-700">
                <c:choose>
                    <c:when test="${project != null}">Update</c:when>
                    <c:otherwise>Create</c:otherwise>
                </c:choose>
            </button>
        </div>
    </form>
</div>
