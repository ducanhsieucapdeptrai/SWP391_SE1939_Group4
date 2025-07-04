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
    .status-badge {
        display: inline-flex;
        align-items: center;
        padding: 0.25rem 0.75rem;
        border-radius: 9999px;
        font-size: 0.875rem;
        font-weight: 500;
    }
    .status-pending {
        background-color: #fef3c7;
        color: #92400e;
    }
    .status-approved {
        background-color: #d1fae5;
        color: #065f46;
    }
    .status-rejected {
        background-color: #fee2e2;
        color: #991b1b;
    }
    .info-card {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
    }
    .action-button {
        transition: all 0.3s ease;
    }
    .action-button:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 25px rgba(0,0,0,0.2);
    }
</style>

<div class="p-6 bg-gray-50 min-h-screen">
    <div class="mb-6">
        <div class="flex justify-between items-center">
            <div>
                <h1 class="text-3xl font-bold text-gray-800">
                    <i class="fas fa-file-alt mr-3 text-blue-600"></i>
                    Request Detail #${requestInfo.requestId}
                </h1>
                <p class="text-gray-600 mt-1">Complete information about this material request</p>
            </div>
            <div class="flex space-x-3">
                <button onclick="history.back()" class="px-6 py-2 bg-white border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors shadow-sm">
                    <i class="fas fa-arrow-left mr-2"></i>
                    Back to Requests
                </button>

            </div>
        </div>
    </div>

    <c:if test="${not empty successMessage}">
        <div class="mb-6 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" role="alert">
            <span class="block sm:inline">${successMessage}</span>
        </div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="mb-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative" role="alert">
            <span class="block sm:inline">${errorMessage}</span>
        </div>
    </c:if>

    <div class="bg-white shadow-lg rounded-lg p-6 mb-6">
        <h2 class="text-xl font-semibold mb-4 text-gray-800">
            <i class="fas fa-info-circle mr-2 text-blue-600"></i>
            Request Information
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <div class="p-4 bg-green-50 rounded-lg">
                <label class="block text-sm font-medium text-gray-700 mb-1">Requested By</label>
                <p class="text-lg font-semibold text-green-700">
                    <i class="fas fa-user mr-2"></i>
                    ${requestInfo.requestedByName}
                </p>
                <p class="text-xs text-green-600">ID: ${requestInfo.requestedBy}</p>
            </div>

            <div class="p-4 bg-orange-50 rounded-lg">
                <label class="block text-sm font-medium text-gray-700 mb-1">Request Date</label>
                <p class="text-lg font-semibold text-orange-700">
                    <i class="fas fa-calendar mr-2"></i>
                    <fmt:formatDate value="${requestInfo.requestDate}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
            </div>

            <div class="p-4 bg-purple-50 rounded-lg">
                <label class="block text-sm font-medium text-gray-700 mb-1">Request Type</label>
                <p class="text-lg font-semibold text-purple-700">
                    <i class="fas fa-tag mr-2"></i>
                    ${requestInfo.requestTypeName}
                    <c:if test="${requestInfo.requestTypeName == 'Export' && not empty requestInfo.exportTypeName}">
                        <br><span class="text-sm text-purple-600">(${requestInfo.exportTypeName})</span>
                    </c:if>
                    <c:if test="${requestInfo.requestTypeName == 'Import' && not empty requestInfo.importTypeName}">
                        <br><span class="text-sm text-purple-600">(${requestInfo.importTypeName})</span>
                    </c:if>
                </p>
                <p class="text-xs text-purple-600">Type ID: ${requestInfo.requestTypeId}</p>
            </div>

            <div class="p-4 bg-gray-50 rounded-lg">
                <label class="block text-sm font-medium text-gray-700 mb-1">Current Status</label>
                <span class="status-badge ${requestInfo.status == 'Pending' || requestInfo.status == 'P' ? 'status-pending' :
                                            requestInfo.status == 'Approved' || requestInfo.status == 'A' ? 'status-approved' : 'status-rejected'}">
                    <i class="fas ${requestInfo.status == 'Pending' || requestInfo.status == 'P' ? 'fa-clock' :
                                    requestInfo.status == 'Approved' || requestInfo.status == 'A' ? 'fa-check-circle' : 'fa-times-circle'} mr-2"></i>
                       ${not empty requestInfo.statusDescription ? requestInfo.statusDescription : requestInfo.status}
                    </span>
                </div>
            </div>

            <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-6">
                <div class="md:col-span-1">
                    <c:if test="${not empty requestInfo.note}">
                        <div class="p-4 bg-yellow-50 rounded-lg border-l-4 border-yellow-400">
                            <label class="block text-sm font-medium text-gray-700 mb-2">
                                <i class="fas fa-sticky-note mr-2"></i>Request Note
                            </label>
                            <p class="text-gray-800">${requestInfo.note}</p>
                        </div>
                    </c:if>
                    <c:if test="${empty requestInfo.note}">
                        <div class="p-4 bg-gray-50 rounded-lg border-l-4 border-gray-300">
                            <label class="block text-sm font-medium text-gray-700 mb-2">
                                <i class="fas fa-sticky-note mr-2"></i>Request Note
                            </label>
                            <p class="text-gray-500 italic">No note provided</p>
                        </div>
                    </c:if>
                </div>

                <div class="md:col-span-2">
                    <c:choose>
                        <c:when test="${not empty requestInfo.approvedByName}">
                            <div class="p-4 bg-indigo-50 rounded-lg border-l-4 border-indigo-400">
                                <label class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-user-check mr-2"></i>Approval Information
                                </label>
                                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div>
                                        <p class="text-sm text-gray-600">Approved By:</p>
                                        <p class="text-lg font-semibold text-indigo-700">${requestInfo.approvedByName}</p>
                                        <p class="text-xs text-indigo-600">ID: ${requestInfo.approvedBy}</p>
                                    </div>
                                    <div>
                                        <p class="text-sm text-gray-600">Approval Date:</p>
                                        <p class="text-lg font-semibold text-indigo-700">
                                            <c:if test="${not empty requestInfo.approvedDate}">
                                                <fmt:formatDate value="${requestInfo.approvedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </c:if>
                                            <c:if test="${empty requestInfo.approvedDate}">
                                                Not yet approved
                                            </c:if>
                                        </p>
                                    </div>
                                </div>
                                <c:if test="${not empty requestInfo.approvalNote}">
                                    <div class="mt-3 pt-3 border-t border-indigo-200">
                                        <p class="text-sm text-gray-600">Approval Note:</p>
                                        <p class="text-indigo-700">${requestInfo.approvalNote}</p>
                                    </div>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="p-4 bg-gray-50 rounded-lg border-l-4 border-gray-300">
                                <label class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-user-check mr-2"></i>Approval Information
                                </label>
                                <p class="text-gray-500 italic">Request is pending approval</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="bg-white shadow-lg rounded-lg p-6">
            <h2 class="text-xl font-semibold mb-4 text-gray-800">
                <i class="fas fa-list mr-2 text-green-600"></i>
                Requested Materials
            </h2>
            <c:choose>
                <c:when test="${not empty requestDetails}">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200 border border-gray-300 rounded-lg">
                            <thead class="bg-gray-100">
                                <tr>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">#</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Image</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Material Info</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Category</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Quantity</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Unit Price</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Total Value</th>
                                    <th class="px-4 py-3 text-left text-sm font-semibold text-gray-700 border">Description</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-gray-200 bg-white text-sm">
                                <c:set var="totalValue" value="0" />
                                <c:forEach var="detail" items="${requestDetails}" varStatus="status">
                                    <c:set var="totalValue" value="${totalValue + detail.totalValue}" />
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-4 py-3 border font-medium">${status.index + 1}</td>
                                        <td class="px-4 py-3 border">
                                            <c:choose>
                                                <c:when test="${not empty detail.image}">
                                                    <img src="assets/images/materials/${detail.image}" alt="${detail.materialName}" class="material-image"
                                                         onerror="this.src='assets/images/materials/no-image.png'">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="w-16 h-16 flex items-center justify-center bg-gray-100 text-gray-400 rounded">
                                                        <i class="fas fa-image"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-4 py-3 border">
                                            <div>
                                                <p class="font-medium text-gray-900">${detail.materialName}</p>
                                                <p class="text-xs text-gray-500">ID: ${detail.materialId}</p>
                                            </div>
                                        </td>
                                        <td class="px-4 py-3 border text-gray-600">
                                            <div>
                                                <p class="font-medium">${detail.categoryName}</p>
                                                <p class="text-xs text-gray-500">${detail.subCategoryName}</p>
                                            </div>
                                        </td>
                                        <td class="px-4 py-3 border text-blue-600 font-semibold text-center">
                                            <span class="bg-blue-100 px-2 py-1 rounded">${detail.quantity}</span>
                                        </td>
                                        <td class="px-4 py-3 border text-green-600 font-medium">
                                            <fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true"/>đ
                                        </td>
                                        <td class="px-4 py-3 border text-green-700 font-semibold">
                                            <fmt:formatNumber value="${detail.totalValue}" type="number" groupingUsed="true"/>đ
                                        </td>
                                        <td class="px-4 py-3 border text-gray-600">
                                            <c:choose>
                                                <c:when test="${not empty detail.description}">
                                                    ${detail.description}
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-gray-400 italic">No description</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="mt-6 bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-6">
                        <h3 class="text-lg font-semibold mb-4 text-gray-800">
                            <i class="fas fa-chart-bar mr-2"></i>Request Summary
                        </h3>
                        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <div class="text-center p-4 bg-white rounded-lg shadow-sm">
                                <div class="text-2xl font-bold text-blue-600 mb-1">
                                    <i class="fas fa-boxes text-blue-500"></i>
                                </div>
                                <h5 class="text-sm font-semibold text-gray-700 mb-1">Total Items</h5>
                                <p class="text-2xl font-bold text-blue-600">${requestDetails.size()}</p>
                            </div>
                            <div class="text-center p-4 bg-white rounded-lg shadow-sm">
                                <div class="text-2xl font-bold text-purple-600 mb-1">
                                    <i class="fas fa-calculator text-purple-500"></i>
                                </div>
                                <h5 class="text-sm font-semibold text-gray-700 mb-1">Total Quantity</h5>
                                <p class="text-2xl font-bold text-purple-600">
                                    <c:set var="totalQuantity" value="0" />
                                    <c:forEach var="detail" items="${requestDetails}">
                                        <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}" />
                                    </c:forEach>
                                    ${totalQuantity}
                                </p>
                            </div>
                            <div class="text-center p-4 bg-white rounded-lg shadow-sm">
                                <div class="text-2xl font-bold text-green-600 mb-1">
                                    <i class="fas fa-dong-sign text-green-500"></i>
                                </div>
                                <h5 class="text-sm font-semibold text-gray-700 mb-1">Total Value</h5>
                                <p class="text-2xl font-bold text-green-600">
                                    <fmt:formatNumber value="${totalValue}" type="number" groupingUsed="true"/>đ
                                </p>
                            </div>
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
        <c:if test="${(requestInfo.status == 'Pending' || requestInfo.status == 'P') && (sessionScope.userRole == 'Director' || sessionScope.userRole == 'Warehouse Manager')}">
            <div class="flex justify-end gap-4 mt-6">
                <button onclick="redirectToApprovePage(${requestInfo.requestId})"
                        class="action-button px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors shadow-sm">
                    <i class="fas fa-check mr-2"></i>
                    Approve
                </button>
                <button onclick="confirmReject(${requestInfo.requestId})"
                        class="action-button px-6 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors shadow-sm">
                    <i class="fas fa-times mr-2"></i>
                    Reject
                </button>
            </div>
        </c:if>
    </div>


    <%-- The approveModal div and all its contents are removed from here --%>

    <div id="rejectModal" class="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50 hidden">
        <div class="bg-white p-6 rounded shadow-md w-full max-w-md">
            <h2 class="text-xl font-bold mb-4">Reject Request</h2>
            <form id="rejectForm" method="POST" action="approveandrejectrequest">
                <input type="hidden" name="requestId" value="${requestInfo.requestId}" />
                <input type="hidden" name="action" value="reject" />
                <label for="rejectReason" class="block mb-2 font-medium text-gray-700">Rejection Reason:</label>
                <textarea id="rejectReason" name="reason" class="w-full border p-2 mb-4 rounded" rows="4" placeholder="Enter reason for rejection..." required></textarea>
                <div class="flex justify-end gap-3">
                    <button type="button" onclick="closeRejectModal()" class="px-4 py-2 border rounded text-gray-600 hover:bg-gray-100">Cancel</button>
                    <button type="submit" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">Confirm Reject</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function redirectToApprovePage(requestId) {
            window.location.href = 'approve-request?requestId=' + requestId; // Or your servlet path, e.g., 'approveRequestServlet?requestId='
        }

        function openRejectModal() {
            document.getElementById("rejectModal").classList.remove("hidden");
        }

        function closeRejectModal() {
            document.getElementById("rejectModal").classList.add("hidden");
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
    </script>