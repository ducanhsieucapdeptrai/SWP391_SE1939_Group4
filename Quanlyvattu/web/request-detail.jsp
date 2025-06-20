<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Request Detail - Material Management System</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<style>
    .material-image {
        width: 60px;
        height: 60px;
        object-fit: cover;
        border-radius: 8px;
    }
</style>

<div class="p-6">
    <!-- Header -->
    <div class="mb-6">
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-2xl font-bold"><i class="fas fa-file-alt mr-2"></i>Request Detail</h1>
                <p class="text-gray-500">View detailed information about this request</p>
            </div>
            <button onclick="history.back()" class="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100">
                <i class="fas fa-arrow-left mr-2"></i>Back to Requests
            </button>
        </div>
    </div>

    <!-- Request Details Table -->
    <div class="bg-white shadow-md rounded p-4">
        <h2 class="text-xl font-semibold mb-4"><i class="fas fa-list mr-2"></i>Requested Materials</h2>
        <c:choose>
            <c:when test="${not empty requestDetails}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200 border border-gray-300">
                        <thead class="bg-gray-100">
                            <tr>
                                <th class="px-4 py-2 border">STT</th>
                                <th class="px-4 py-2 border">Image</th>
                                <th class="px-4 py-2 border">Material Name</th>
                                <th class="px-4 py-2 border">Category</th>
                                <th class="px-4 py-2 border">Quantity</th>
                                <th class="px-4 py-2 border">Unit Price</th>
                                <th class="px-4 py-2 border">Total Value</th>
                                <th class="px-4 py-2 border">Description</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y text-sm">
                            <c:set var="totalValue" value="0" />
                            <c:forEach var="detail" items="${requestDetails}" varStatus="status">
                                <c:set var="totalValue" value="${totalValue + detail.totalValue}" />
                                <tr>
                                    <td class="px-4 py-2 border">${status.index + 1}</td>
                                    <td class="px-4 py-2 border">
                                        <c:choose>
                                            <c:when test="${not empty detail.image}">
                                                <img src="assets/images/materials/${detail.image}" alt="${detail.materialName}" class="material-image" onerror="this.src='assets/images/materials/no-image.png'">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="w-16 h-16 flex items-center justify-center bg-gray-100 text-gray-400 rounded">
                                                    <i class="fas fa-image"></i>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="px-4 py-2 border font-medium">${detail.materialName}</td>
                                    <td class="px-4 py-2 border">${detail.categoryName} / ${detail.subCategoryName}</td>
                                    <td class="px-4 py-2 border text-blue-600 font-semibold">${detail.quantity}</td>
                                    <td class="px-4 py-2 border text-green-600">
                                        <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$" />
                                    </td>
                                    <td class="px-4 py-2 border text-green-700 font-semibold">
                                        <fmt:formatNumber value="${detail.totalValue}" type="currency" currencySymbol="$" />
                                    </td>
                                    <td class="px-4 py-2 border text-gray-600">${detail.description}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Summary -->
                <div class="mt-6 bg-gray-100 rounded p-4 grid grid-cols-1 md:grid-cols-3 text-center gap-4">
                    <div>
                        <h5 class="font-semibold text-primary"><i class="fas fa-boxes mr-1"></i>Total Items</h5>
                        <p class="text-lg font-bold">${requestDetails.size()}</p>
                    </div>
                    <div>
                        <h5 class="font-semibold text-info"><i class="fas fa-calculator mr-1"></i>Total Quantity</h5>
                        <p class="text-lg font-bold">
                            <c:set var="totalQuantity" value="0" />
                            <c:forEach var="detail" items="${requestDetails}">
                                <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}" />
                            </c:forEach>
                            ${totalQuantity}
                        </p>
                    </div>
                    <div>
                        <h5 class="font-semibold text-success"><i class="fas fa-dollar-sign mr-1"></i>Total Value</h5>
                        <p class="text-lg font-bold">
                            <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol="$" />
                        </p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-10">
                    <i class="fas fa-inbox fa-3x text-gray-400 mb-3"></i>
                    <h5 class="text-gray-600">No materials found in this request</h5>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Approve / Reject Buttons -->
<c:if test="${requestStatus eq 'Pending'}">
    <div class="mt-6 flex justify-center gap-4">
        <button onclick="openApproveModal()" class="bg-green-600 hover:bg-green-700 text-white font-semibold px-6 py-2 rounded shadow">
            <i class="fas fa-check-circle mr-2"></i> Approve
        </button>
        <button onclick="confirmReject(${requestId})" class="bg-red-600 hover:bg-red-700 text-white font-semibold px-6 py-2 rounded shadow">
            <i class="fas fa-times-circle mr-2"></i> Reject
        </button>
    </div>

    <!-- Approve Modal -->
    <div id="approveModal" class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50 hidden">
        <div class="bg-white p-6 rounded shadow-md w-[90%] max-w-4xl max-h-[90vh] overflow-y-auto">
            <h2 class="text-xl font-bold mb-4">Approve Request</h2>

            <form method="POST" action="approveandrejectrequest">
                <input type="hidden" name="requestId" value="${requestId}" />
                <input type="hidden" name="action" value="approve" />
                <table class="w-full border mb-4" id="materialTable">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="border p-2">STT</th>
                            <th class="border p-2">Material</th>
                            <th class="border p-2">Quantity</th>
                            <th class="border p-2">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${requestDetails}" varStatus="loop">
                            <tr>
                                <td class="border p-2">${loop.index + 1}</td>
                                <td class="border p-2">
                                    <select name="materialId[]" class="w-full bg-gray-100 px-2 py-1" disabled>
                                        <option value="${detail.materialId}">${detail.materialName}</option>
                                    </select>
                                    <input type="hidden" name="materialId[]" value="${detail.materialId}" />
                                </td>
                                <td class="border p-2">
                                    <input name="quantity[]" type="number" value="${detail.quantity}" readonly class="w-full bg-gray-100 px-2 py-1" />
                                </td>
                                <td class="border p-2 text-center text-gray-400"><i class="fas fa-lock"></i></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <button type="button" onclick="addMaterialRow()" class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 mb-4">
                    <i class="fas fa-plus-circle mr-1"></i>Add Material
                </button>

                <label class="block mb-2 font-medium text-gray-700">Approval Note:</label>
                <textarea name="note" class="w-full border p-2 mb-4 rounded" rows="3" placeholder="Enter your note..."></textarea>

                <div class="flex justify-end gap-3">
                    <button type="button" onclick="closeApproveModal()" class="px-4 py-2 border rounded text-gray-600 hover:bg-gray-100">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">Confirm Approve</button>
                </div>
            </form>
        </div>
    </div>
</c:if>

<script>
    function openApproveModal() {
        document.getElementById("approveModal").classList.remove("hidden");
    }

    function closeApproveModal() {
        document.getElementById("approveModal").classList.add("hidden");
    }

    function confirmReject(requestId) {
        Swal.fire({
            title: 'Reject this request?',
            input: 'textarea',
            inputLabel: 'Rejection Reason',
            inputPlaceholder: 'Enter your reason...',
            inputValidator: (value) => {
                if (!value.trim())
                    return 'You must enter a reason to reject.';
            },
            showCancelButton: true,
            confirmButtonText: 'Reject',
            cancelButtonText: 'Cancel',
            icon: 'warning',
            preConfirm: (reason) => {
                return new Promise((resolve) => {
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = 'approveandrejectrequest';
                    form.innerHTML = `
                        <input type="hidden" name="requestId" value="${requestId}">
                        <input type="hidden" name="reason" value="${reason}">
                        <input type="hidden" name="action" value="reject">
                    `;
                    document.body.appendChild(form);
                    form.submit();
                    resolve();
                });
            }
        });
    }

    function addMaterialRow() {
        const table = document.getElementById('materialTable').getElementsByTagName('tbody')[0];
        const rowCount = table.rows.length;
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="border p-2">\${rowCount + 1}</td>
            <td class="border p-2">
                <select name="materialId[]" class="w-full border px-2 py-1">
    <c:forEach var="m" items="${allMaterials}">
                        <option value="${m.materialId}">${m.materialName}</option>
    </c:forEach>
                </select>
            </td>
            <td class="border p-2">
                <input name="quantity[]" type="number" class="w-full border px-2 py-1" value="1" min="1">
            </td>
            <td class="border p-2 text-center">
                <button type="button" onclick="this.closest('tr').remove(); updateSTT();" class="text-red-600 hover:text-red-800">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </td>
        `;
        table.appendChild(row);
        updateSTT();
    }

    function updateSTT() {
        const rows = document.querySelectorAll('#materialTable tbody tr');
        rows.forEach((row, index) => {
            row.querySelector('td').innerText = index + 1;
        });
    }
</script>
