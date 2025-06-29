<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<meta charset="UTF-8">
<title>Approve Material Request</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<style>
    .material-image {
        width: 50px;
        height: 50px;
        object-fit: cover;
        border-radius: 4px;
    }
</style>

<select id="materialSelectTemplate" class="hidden">
    <option disabled selected value="">-- Select Material --</option>
    <c:forEach var="material" items="${materialList}">
        <option value="${material.materialId}">${material.materialName}</option>
    </c:forEach>
</select>


<div class="p-6 bg-gray-50 min-h-screen">
    <div class="max-w-4xl mx-auto bg-white shadow-lg rounded-lg p-8">
        <h1 class="text-3xl font-bold text-gray-800 mb-6 text-center">
            <i class="fas fa-check-circle mr-2 text-green-600"></i>
            Approve Request
        </h1>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-gray-700 mb-6">
            <div>
                <p class="font-semibold">Requested By:</p>
                <p class="text-lg text-gray-900">${requestInfo.requestedByName}</p>
            </div>
            <div>
                <p class="font-semibold">Request Date:</p>
                <p class="text-lg text-gray-900">
                    <fmt:formatDate value="${requestInfo.requestDate}" pattern="dd/MM/yyyy HH:mm" />
                </p>
            </div>
        </div>

        <form method="POST" action="approveandrejectrequest" id="approveForm">
            <input type="hidden" name="requestId" value="${requestInfo.requestId}" />
            <input type="hidden" name="action" value="approve" />

            <h2 class="text-xl font-semibold mb-2 text-gray-800">
                <i class="fas fa-boxes text-blue-600 mr-2"></i>Material List Request
            </h2>

            <div class="overflow-x-auto mb-4 border rounded-lg shadow">
                <table class="min-w-full divide-y divide-gray-200 text-sm text-left">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="px-4 py-3">#</th>
                            <th class="px-4 py-3">Material</th>
                            <th class="px-4 py-3">Quantity</th>
                            <th class="px-4 py-3">Action</th>
                        </tr>
                    </thead>
                    <tbody id="materialTableBody" class="bg-white divide-y divide-gray-200">
                        <c:forEach var="detail" items="${currentRequestDetails}" varStatus="loop">
                            <tr>
                                <td class="px-4 py-2">${loop.index + 1}</td>
                                <td class="px-4 py-2">
                                    ${detail.materialName}
                                    <input type="hidden" name="materialId[]" value="${detail.materialId}" />
                                </td>
                                <td class="px-4 py-2">
                                    <input type="number" name="quantity[]" value="${detail.quantity}" min="1"
                                           class="w-24 border rounded px-2 py-1" />
                                </td>
                                <td class="px-4 py-2 text-center text-gray-400">
                                    <i class="fas fa-lock" title="Fixed material"></i>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="mb-4">
                <button type="button" onclick="addMaterialRow()" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                    <i class="fas fa-plus mr-1"></i> Add Material
                </button>
            </div>

            <div class="mb-6">
                <label class="block font-medium mb-2">Approval Note (optional):</label>
                <textarea name="note" rows="4" class="w-full border rounded px-3 py-2"></textarea>
            </div>

            <div class="flex justify-between">
                <a href="request-detail?id=${requestInfo.requestId}" class="px-6 py-2 border border-gray-300 rounded text-gray-700 hover:bg-gray-100">
                    <i class="fas fa-arrow-left mr-2"></i> Back to Detail
                </a>
                <button type="submit" class="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                    <i class="fas fa-check mr-2"></i> Confirm Approve
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    function addMaterialRow() {
        const tbody = document.getElementById("materialTableBody");
        const row = document.createElement("tr");

        // Clone the select template
        const templateSelect = document.getElementById("materialSelectTemplate");
        const clonedSelect = templateSelect.cloneNode(true);
        clonedSelect.classList.remove("hidden");
        clonedSelect.removeAttribute("id");
        clonedSelect.name = "materialId[]";
        clonedSelect.classList.add("w-full", "border", "rounded", "px-2", "py-1");

        // Build the row
        const indexCell = `<td class="px-4 py-2">New</td>`;
        const materialCell = `<td class="px-4 py-2"></td>`;
        const quantityCell = `
            <td class="px-4 py-2">
                <input type="number" name="quantity[]" min="1" value="1" class="w-24 border rounded px-2 py-1" />
            </td>
        `;
        const actionCell = `
            <td class="px-4 py-2 text-center">
                <button type="button" onclick="this.closest('tr').remove()" class="text-red-500 hover:text-red-700">
                    <i class="fas fa-trash-alt" title="Remove"></i>
                </button>
            </td>
        `;

        // Append cells to row
        row.innerHTML = indexCell + materialCell + quantityCell + actionCell;
        tbody.appendChild(row);

        // Insert the cloned select into the correct cell
        row.children[1].appendChild(clonedSelect);
    }
</script>
