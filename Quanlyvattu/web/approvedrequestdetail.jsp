<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Request Detail - Material Management System</title>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<script src="https://cdn.tailwindcss.com"></script>
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
                <h1 class="text-2xl font-bold">
                    <i class="fas fa-file-alt mr-2"></i>
                    Request Detail
                </h1>
                <p class="text-gray-500">View detailed information about this request</p>
            </div>
            <button onclick="history.back()" class="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100">
                <i class="fas fa-arrow-left mr-2"></i>
                Back to Requests
            </button>
        </div>
    </div>

    <!-- Request Details Table -->
    <div class="bg-white shadow-md rounded p-4">
        <h2 class="text-xl font-semibold mb-4">
            <i class="fas fa-list mr-2"></i>
            Requested Materials
        </h2>
        <c:choose>
            <c:when test="${not empty requestDetails}">
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200 border border-gray-300">
                        <thead class="bg-gray-100">
                            <tr>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">#</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Image</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Material Name</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Category</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Quantity</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Unit Price</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Total Value</th>
                                <th class="px-4 py-2 text-left text-sm font-semibold text-gray-700 border">Description</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 bg-white text-sm">
                            <c:set var="totalValue" value="0" />
                            <c:forEach var="detail" items="${requestDetails}" varStatus="status">
                                <c:set var="totalValue" value="${totalValue + detail.totalValue}" />
                                <tr>
                                    <td class="px-4 py-2 border">${status.index + 1}</td>
                                    <td class="px-4 py-2 border">
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
                                    <td class="px-4 py-2 border font-medium">${detail.materialName}</td>
                                    <td class="px-4 py-2 border">${detail.categoryName} / ${detail.subCategoryName}</td>
                                    <td class="px-4 py-2 border text-blue-600 font-semibold">${detail.quantity}</td>
                                    <td class="px-4 py-2 border text-green-600">
                                        <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$"/>
                                    </td>
                                    <td class="px-4 py-2 border text-green-700 font-semibold">
                                        <fmt:formatNumber value="${detail.totalValue}" type="currency" currencySymbol="$"/>
                                    </td>
                                    <td class="px-4 py-2 border text-gray-600">${detail.description}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Summary -->
                <div class="mt-6 bg-gray-100 rounded p-4">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-center">
                        <div>
                            <h5 class="text-primary font-semibold">
                                <i class="fas fa-boxes mr-1"></i>
                                Total Items
                            </h5>
                            <p class="text-lg font-bold">${requestDetails.size()}</p>
                        </div>
                        <div>
                            <h5 class="text-info font-semibold">
                                <i class="fas fa-calculator mr-1"></i>
                                Total Quantity
                            </h5>
                            <p class="text-lg font-bold">
                                <c:set var="totalQuantity" value="0" />
                                <c:forEach var="detail" items="${requestDetails}">
                                    <c:set var="totalQuantity" value="${totalQuantity + detail.quantity}" />
                                </c:forEach>
                                ${totalQuantity}
                            </p>
                        </div>
                        <div>
                            <h5 class="text-success font-semibold">
                                <i class="fas fa-dollar-sign mr-1"></i>
                                Total Value
                            </h5>
                            <p class="text-lg font-bold">
                                <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol="$"/>
                            </p>
                        </div>
                    </div>
                </div>
                <!-- Accept Job Button -->
                <c:if test="${not empty requestId}">
                    <!-- Nút Nhận việc -->
                    <!-- Nút Nhận việc (căn giữa) -->
                    <div class="flex justify-center mt-6">
                        <button type="button" 
                                class="bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700 shadow"
                                onclick="showConfirmModal()">
                            <i class="fas fa-check-circle mr-2"></i> Accept Task
                        </button>
                    </div>


                    <!-- Modal xác nhận -->
                    <div id="confirmModal" class="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center hidden">
                        <div class="bg-white rounded-lg shadow-lg p-6 w-80 text-center">
                            <h2 class="text-lg font-semibold mb-4">Are you sure you want to accept this task ?</h2>
                            <form method="post" action="${pageContext.request.contextPath}/approvedrequestdetail">
                                <input type="hidden" name="requestId" value="${requestId}" />
                                <div class="flex justify-center gap-4">
                                    <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Yes</button>
                                    <button type="button" onclick="hideConfirmModal()" class="bg-gray-400 text-white px-4 py-2 rounded hover:bg-gray-500">No</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- JS để toggle modal -->
                    <script>
                        function showConfirmModal() {
                            document.getElementById('confirmModal').classList.remove('hidden');
                        }
                        function hideConfirmModal() {
                            document.getElementById('confirmModal').classList.add('hidden');
                        }
                    </script>
                </c:if>

            </c:when>


            <c:otherwise>
                <div class="text-center py-10">
                    <i class="fas fa-inbox fa-3x text-gray-400 mb-3"></i>
                    <h5 class="text-gray-600">No materials found in this request</h5>
                    <c:if test="${not empty message}">
                        <p class="text-gray-500">${message}</p>
                    </c:if>
                    <c:if test="${empty message}">
                        <p class="text-gray-500">This request doesn't contain any material details.</p>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>



    </div>
</div>

