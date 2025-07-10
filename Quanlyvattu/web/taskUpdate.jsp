<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="max-w-6xl mx-auto p-6">
    <!-- Header -->
    <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-2xl font-bold text-gray-900">Task Update</h1>
                <p class="text-sm text-gray-700 mt-1">
                    <strong>Type:</strong> ${requestType}
                    <span class="mx-2">|</span>
                    <strong>Requested By:</strong> ${createdBy}
                    <span class="mx-2">|</span>
                    <strong>Date:</strong> <fmt:formatDate value="${createdAt}" pattern="dd/MM/yyyy HH:mm" />
                </p>
                <p class="text-gray-600 mt-1">${note}</p>
                <p class="text-sm text-gray-500 mt-1">
                    Status:
                    <span class="font-medium 
                        <c:choose>
                            <c:when test="${requestStatus eq 'Completed'}">text-green-600</c:when>
                            <c:otherwise>text-yellow-600</c:otherwise>
                        </c:choose>">
                        ${requestStatus}
                    </span>
                </p>

                <c:if test="${not empty incompleteItems}">
                    <div class="mt-2 text-sm text-yellow-700 bg-yellow-50 border border-yellow-200 rounded-md p-3">
                        <strong>Warning:</strong> Some materials in this request are not fully processed:
                        <ul class="list-disc list-inside mt-1">
                            <c:forEach var="mat" items="${incompleteItems}">
                                <li>${mat}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </div>
            <a href="tasklist"
               class="inline-flex items-center px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
                </svg>
                Back
            </a>
        </div>
    </div>

    <!-- Messages -->
    <c:if test="${not empty errorMessage}">
        <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
            <div class="flex items-center">
                <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd"
                          d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
                          clip-rule="evenodd"/>
                </svg>
                ${errorMessage}
            </div>
        </div>
    </c:if>

    <c:if test="${not empty successMessage}">
        <div class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg mb-6">
            <div class="flex items-center">
                <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
                          clip-rule="evenodd"/>
                </svg>
                ${successMessage}
            </div>
        </div>
    </c:if>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- LEFT -->
        <div class="lg:col-span-2">
            <div class="bg-white rounded-lg shadow-sm border border-gray-200">
                <div class="p-6 border-b border-gray-200">
                    <h2 class="text-lg font-semibold text-gray-900 flex items-center">
                        <svg class="w-5 h-5 mr-2 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                        </svg>
                        Create Slip
                    </h2>
                </div>

                <c:choose>
                    <c:when test="${requestStatus eq 'Completed'}">
                        <div class="p-6 text-gray-500 italic">This request has been fully processed.</div>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="taskUpdate" class="p-6">
                            <input type="hidden" name="action" value="createSlip"/>
                            <input type="hidden" name="requestId" value="${requestId}"/>

                            <div class="overflow-x-auto px-6">
                                <table class="w-full">
                                    <thead>
                                    <tr class="border-b border-gray-200">
                                        <th class="py-2 px-4 text-left text-gray-700 font-medium">Material</th>
                                        <th class="py-2 px-4 text-center text-gray-700 font-medium">Requested</th>
                                        <th class="py-2 px-4 text-center text-gray-700 font-medium">
                                            <c:choose>
                                                <c:when test="${requestType eq 'Export'}">Available</c:when>
                                                <c:otherwise>Price</c:otherwise>
                                            </c:choose>
                                        </th>
                                        <th class="py-2 px-4 text-center text-gray-700 font-medium">Actual</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="item" items="${requestDetails}">
                                        <tr class="border-b hover:bg-gray-50">
                                            <td class="py-3 px-4 font-medium text-gray-900">${item.materialName}</td>
                                            <td class="py-3 px-4 text-center">${item.quantity}</td>
                                            <td class="py-3 px-4 text-center">
                                                <c:choose>
                                                    <c:when test="${requestType eq 'Export'}">
                                                        <span class="inline-block px-2 py-0.5 rounded-full text-xs font-medium
                                                            ${item.stockQuantity >= item.quantity ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                                                            ${item.stockQuantity}
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="$"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="py-3 px-4 text-center">
                                                <input type="number"
                                                       name="actualQty_${item.materialId}"
                                                       class="w-20 text-center border rounded-md px-2 py-1"
                                                       min="0"
                                                       <c:if test="${requestType eq 'Export'}">max="${item.stockQuantity}"</c:if>
                                                       placeholder="0"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="mt-6 px-6 flex justify-end gap-4">
                                <button type="submit"
                                        class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
                                    Sign & Save Slip
                                </button>
                            </div>
                        </form>
                    </c:otherwise>
                </c:choose>

                <c:if test="${not empty taskLogs}">
                    <form method="get" action="printSlip" class="mt-4 px-6 flex justify-end">
                        <input type="hidden" name="requestId" value="${requestId}" />
                        <button type="submit"
                                class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg text-sm font-medium">
                            Print Slip
                        </button>
                    </form>
                </c:if>
            </div>
        </div>

        <!-- RIGHT: History -->
        <div>
            <div class="bg-white rounded-lg shadow-sm border border-gray-200">
                <div class="p-6 border-b border-gray-200">
                    <h2 class="text-lg font-semibold text-gray-900 flex items-center">
                        <svg class="w-5 h-5 mr-2 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"/>
                        </svg>
                        Processing History
                    </h2>
                </div>
                <div class="p-6 space-y-4">
                    <c:choose>
                        <c:when test="${not empty taskLogs}">
                            <c:forEach var="log" items="${taskLogs}">
                                <div class="border border-gray-200 rounded-md p-4">
                                    <div class="flex justify-between items-center mb-2">
                                        <div class="text-sm text-gray-800 font-semibold">
                                            By ${log.staffName}
                                        </div>
                                        <div class="text-xs text-gray-500">
                                            <fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </div>
                                    </div>
                                    <ul class="text-sm text-gray-700 list-disc pl-5">
                                        <c:forEach var="detail" items="${log.slipDetails}">
                                            <li>${detail.materialName} – ${detail.quantity}</li>
                                        </c:forEach>
                                    </ul>
                                    <div class="mt-2">
                                        <a href="printSlip?taskId=${log.taskId}" class="text-sm text-blue-600 hover:underline">
                                            Reprint Slip
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="text-sm text-gray-500 italic">No activity recorded yet.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
