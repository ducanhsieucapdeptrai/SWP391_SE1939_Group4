<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<c:if test="${not empty error}">
    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
</c:if>
<c:if test="${not empty message}">
    <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${message}</div>
</c:if>

<div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-semibold text-gray-800">Create Request</h2>
        <a href="${pageContext.request.contextPath}/reqlist" class="text-blue-600 hover:text-blue-800 flex items-center">
            <i class="fas fa-arrow-left mr-2"></i> Back
        </a>
    </div>

    <form id="frmRequest" action="createrequest" method="post">
        <!-- Type & SubType -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Request Type:</label>
                <select id="typeId" name="typeId" required class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                    <option value="">--Select Type--</option>
                    <c:forEach var="t" items="${types}">
                        <c:if test="${t.requestTypeId != 2 && t.requestTypeId != 4}">
                            <option value="${t.requestTypeId}">${t.requestTypeName}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            <div>
                <!-- SubType hiển thị cứng nếu Type là Export -->
                <c:choose>
                    <c:when test="${param.typeId == '1'}">
                        <label class="block text-sm font-medium text-gray-700 mb-2">SubType:</label>
                        <input type="hidden" name="subTypeId" value="1" />
                        <input type="text" value="For Construction" readonly
                               class="w-full px-3 py-2 bg-gray-100 border rounded-md" />
                    </c:when>
                    <c:otherwise>
                        <label class="block text-sm font-medium text-gray-700 mb-2">SubType:</label>
                        <select id="subTypeId" name="subTypeId" required
                                class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                            <option value="1" data-type="1">For Construction</option>
                            <option value="2" data-type="1">For Equipment Repair</option>
                        </select>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>

        <!-- Force subtype = For Construction when Export selected -->


        <!-- Material selection only -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Material:</label>
            <select id="materialId" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                <option value="">--Select--</option>
                <c:forEach var="m" items="${materials}">
                    <option value="${m.materialId}" data-name="${m.materialName}" data-img="${m.image}" data-stock="${m.quantity}">
                        ${m.materialName}
                    </option>
                </c:forEach>

                <c:if test="${empty materials}">
                    <div class="bg-yellow-100 text-yellow-800 p-3 mb-4 rounded">⚠️ Materials list is empty</div>
                </c:if>

            </select>
        </div>
        <div>Material list size: ${fn:length(materials)}</div>

        <!-- Stock & Quantity -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end mb-6">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Stock:</label>
                <input type="text" id="stock" readonly class="w-full px-3 py-2 bg-gray-100 border rounded-md">
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Quantity:</label>
                <input type="number" id="quantity" min="1" value="1" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
                <button type="button" id="btnAdd" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md w-full">Add Material</button>
            </div>
        </div>

        <!-- Selected Materials Table -->
        <div class="mb-6">
            <h3 class="text-lg font-medium text-gray-800 mb-4">Selected Materials</h3>
            <div class="overflow-x-auto border border-gray-200 rounded-lg">
                <table id="tblItems" class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">STT</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Image</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantity</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <!-- Filled via JS -->
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Project dropdown -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Project:</label>
            <select name="projectId" id="projectId" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                <option value="">-- No Project --</option>
                <c:forEach var="p" items="${projectList}">
                    <option value="${p.projectId}">${p.projectName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Note -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Note:</label>
            <textarea name="note" id="note" rows="3" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 resize-vertical"></textarea>
        </div>

        <!-- Submit -->
        <div class="text-center">
            <button type="submit" id="btnCreate" class="bg-blue-500 hover:bg-blue-600 text-white px-8 py-3 rounded-md font-medium">Create Request</button>
        </div>
    </form>
    <div class="mb-4 p-4 border bg-yellow-50">
        <strong>Debug Materials List:</strong>
        <c:forEach var="m" items="${materials}">
            <div>${m.materialId} - ${m.materialName}</div>
        </c:forEach>
    </div>

</div>
