        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
        <%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

        <%-- Sẽ dùng session để lấy staffName đúng nhất --%>
        <c:set var="staffName" value="${sessionScope.loggedUser.fullName}" />



        <div class="max-w-6xl mx-auto p-6">
            <!-- Header -->
            <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
                <div class="flex items-center justify-between">
                    <div>
                        <h1 class="text-2xl font-bold text-gray-900">Task Update</h1>
                        <p class="text-sm text-gray-700 mt-1">
                            <strong>Request Type:</strong> ${requestType}
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
                        <p class="text-sm text-gray-500 mt-1">
                            Signed in as: <strong>${staffName}</strong>
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

            <!-- Notification -->
            <div id="notificationBox"
                 class="hidden px-4 py-3 rounded-lg mb-6 text-sm flex items-start space-x-2"
                 role="alert">
                <svg class="w-5 h-5 mt-1 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                    <path id="notificationIcon" fill-rule="evenodd" clip-rule="evenodd"
                          d="M10 18a8 8 0 100-16 8 8 0 000 16zm-1-4a1 1 0 112 0 1 1 0 01-2 0zm1-9a1 1 0 00-1 1v4a1 1 0 002 0V6a1 1 0 00-1-1z"/>
                </svg>
                <span id="notificationMessage" class="font-medium"></span>
            </div>

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

                        <c:if test="${requestStatus ne 'Completed'}">
                            <form id="signSlipForm" method="post" action="taskUpdate" class="p-6">
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
                                                    <c:when test="${requestType eq 'Export'}">Stock</c:when>
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

                                <!-- BUTTONS -->
                                <div class="px-6 mt-6 flex justify-end space-x-4">
                                    <button type="button"
                                            onclick="openConfirmModal()"
                                            class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-lg text-sm font-medium shadow-sm">
                                        Sign & Save Slip
                                    </button>
                                    <button type="button" onclick="submitPreviewForm()"
                                            class="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg text-sm font-medium shadow-sm">
                                        Print Slip
                                    </button>
                                </div>
                            </form>

        <!-- Hidden preview form -->
        <form id="previewForm" method="post" action="slipPreview" target="_blank" class="hidden">
            <input type="hidden" name="requestId" value="${requestId}" />
            <input type="hidden" name="requestType" value="${requestType}" />
            <input type="hidden" name="note" value="${note}" />

            <c:forEach var="item" items="${requestDetails}">
                <input type="hidden" id="previewQty_${item.materialId}" name="actualQty_${item.materialId}" />
                <input type="hidden" name="materialName_${item.materialId}" value="${item.materialName}" />
            </c:forEach>
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
                        <div id="historyContainer" class="p-6 space-y-4 overflow-y-auto custom-scrollbar scroll-smooth transition-all duration-500 ease-in-out max-h-[60vh] min-h-[120px]">
                            <c:choose>
                                <c:when test="${not empty taskLogs}">
                                    <c:forEach var="log" items="${taskLogs}" varStatus="loop">
                                        <c:set var="slipNumber" value="${fn:length(taskLogs) - loop.index}" />
                                        <div class="border border-gray-200 rounded-md p-4">
                                            <div class="flex justify-between items-center mb-2">
                                                <div class="text-sm text-gray-800 font-semibold">
                                                    Slip #${slipNumber} – By ${log.staffName}
                                                </div>
                                                <div class="text-xs text-gray-500">
                                                    <fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </div>
                                            </div>
                                            <div class="text-xs text-gray-600 mt-1">
                                                Slip Code: <span class="font-mono">${log.slipCode}</span>
                                            </div>
                                            <ul class="text-sm text-gray-700 list-disc pl-5 mt-2">
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

        <!-- Confirmation Modal -->
        <div id="confirmModal" class="fixed inset-0 z-50 bg-black bg-opacity-40 hidden items-center justify-center">
            <div class="bg-white rounded-2xl shadow-xl max-w-md w-full p-6">
                <h2 class="text-lg font-bold text-gray-800 mb-3">Confirm Slip Submission</h2>
                <p class="text-gray-600 mb-6">Are you sure you want to sign and save this slip?</p>
                <div class="flex justify-end space-x-3">
                    <button onclick="closeConfirmModal()" class="px-4 py-2 rounded-lg bg-gray-300 text-gray-800 hover:bg-gray-400">
                        Cancel
                    </button>
                    <button id="confirmSubmitBtn" onclick="submitSignSlip()" class="px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700">
                        Confirm
                    </button>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script>
            function showNotification(type, message) {
                const box = document.getElementById("notificationBox");
                const msg = document.getElementById("notificationMessage");

                msg.textContent = message;
                box.classList.remove("hidden");
                box.className = "px-4 py-3 rounded-lg mb-6 text-sm flex items-start space-x-2";

                if (type === "error") {
                    box.classList.add("bg-red-50", "border", "border-red-200", "text-red-700");
                } else if (type === "success") {
                    box.classList.add("bg-green-50", "border", "border-green-200", "text-green-700");
                } else {
                    box.classList.add("bg-gray-100", "text-gray-800");
                }
            }

        function submitPreviewForm() {
            let hasValid = false;
            let exceeded = [];
            let invalid = [];

            document.getElementById("notificationBox").classList.add("hidden");

            document.querySelectorAll('input[name^="actualQty_"]').forEach(input => {
                const value = input.value.trim();
                const materialName = input.closest('tr')?.querySelector('td')?.innerText || "Unknown";
                const maxAttr = input.getAttribute('max');
                const max = maxAttr ? parseInt(maxAttr) : Infinity;

                const materialId = input.name.split("_")[1];
                const hiddenInput = document.getElementById("previewQty_" + materialId);
                if (hiddenInput) hiddenInput.value = value;

                if (value !== "") {
                    const num = parseInt(value);
                    if (isNaN(num) || num < 0) {
                        invalid.push(materialName);
                    } else if (num > max) {
                        exceeded.push(materialName);
                    } else if (num > 0) {
                        hasValid = true;
                    }
                }
            });

            if (invalid.length > 0) {
                showNotification("error", "Invalid quantity for: " + invalid.join(", "));
                return;
            }
            if (exceeded.length > 0) {
                showNotification("error", "Quantity exceeds allowed limit for: " + exceeded.join(", "));
                return;
            }
            if (!hasValid) {
                showNotification("error", "Please enter at least one valid actual quantity.");
                return;
            }

            document.getElementById("previewForm").submit();
        }


            function openConfirmModal() {
                document.getElementById("confirmModal").classList.remove("hidden");
                document.getElementById("confirmModal").classList.add("flex");
            }

            function closeConfirmModal() {
                document.getElementById("confirmModal").classList.remove("flex");
                document.getElementById("confirmModal").classList.add("hidden");
            }

            function submitSignSlip() {
                const confirmBtn = document.getElementById("confirmSubmitBtn");
                confirmBtn.disabled = true;
                confirmBtn.innerText = "Processing...";
                closeConfirmModal();
                document.getElementById("signSlipForm").submit();
            }

            window.addEventListener("DOMContentLoaded", () => {
                const container = document.getElementById("historyContainer");
                if (container) {
                    container.scrollTop = container.scrollHeight;
                }
            });
        </script>

        <style>
            .custom-scrollbar::-webkit-scrollbar {
                width: 6px;
            }
            .custom-scrollbar::-webkit-scrollbar-thumb {
                background-color: rgba(107, 114, 128, 0.4);
                border-radius: 4px;
            }
            .custom-scrollbar::-webkit-scrollbar-track {
                background: transparent;
            }
        </style>
