<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>

        <meta charset="UTF-8">
        <title>Task List</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    </head>
    <body class="bg-gray-100 p-0 m-0">
        <div class="p-6">
            <div class="flex justify-between items-center mb-4">
                <h1 class="text-2xl font-bold">Task List</h1>
                <a href="${pageContext.request.contextPath}/dashboard" 
                   class="bg-gray-300 hover:bg-gray-400 text-black font-semibold py-2 px-4 rounded inline-block">
                    Back to Dashboard
                </a>

            </div>

            <!-- FORM FILTER -->
            <form action="tasklist" method="get" class="flex flex-wrap gap-4 items-end mb-4">
                <input type="hidden" name="view" value="${activeView}" />
                <div>
                    <label class="block font-semibold mb-1">Request Type:</label>
                    <select name="requestType" class="px-3 py-2 border rounded w-48">
                        <option value="">All</option>
                        <c:forEach var="type" items="${requestTypes}">
                            <option value="${type}" ${type == filterType ? 'selected' : ''}>${type}</option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <label class="block font-semibold mb-1">Requested By:</label>
                    <input type="text" name="requestedBy" list="requesterNames" value="${filterRequestedBy}"
                           class="px-3 py-2 border rounded w-48" />
                    <datalist id="requesterNames">
                        <c:forEach var="name" items="${requesterNames}">
                            <option value="${name}" />
                        </c:forEach>
                    </datalist>
                </div>
                <div>
                    <label class="block font-semibold mb-1">Created At:</label>
                    <input type="date" name="requestDate" value="${filterRequestDate}" class="px-3 py-2 border rounded w-48" />
                </div>
                <div class="self-end">
                    <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Find</button>
                </div>
                <a href="${pageContext.request.contextPath}/tasklist" 
                   class="bg-gray-500 hover:bg-gray-600 text-white font-semibold py-2 px-4 rounded inline-block">
                    Clear Filter
                </a>
            </form>

            <!-- FILTER VIEW BUTTONS -->
            <div class="mb-6 flex gap-4">
                <form action="tasklist" method="get">
                    <input type="hidden" name="view" value="ongoing" />
                    <button type="submit"
                            class="px-4 py-2 rounded font-semibold transition-colors
                            ${activeView == 'ongoing' || empty activeView ? 'bg-blue-600 text-white hover:bg-blue-700' : 'bg-white text-blue-600 border border-blue-600 hover:bg-blue-50'}">
                        Ongoing Tasks
                    </button>
                </form>
                <form action="tasklist" method="get">
                    <input type="hidden" name="view" value="upcoming" />
                    <button type="submit"
                            class="px-4 py-2 rounded font-semibold transition-colors
                            ${activeView == 'upcoming' ? 'bg-yellow-500 text-white hover:bg-yellow-600' : 'bg-white text-yellow-600 border border-yellow-500 hover:bg-yellow-50'}">
                        Upcoming Tasks
                    </button>
                </form>
            </div>

            <!-- MAIN TABLE FORM -->
            <form id="transferForm" onsubmit="submitTransfer(event)">
                <table class="w-full table-auto border-collapse bg-white shadow-md">
                    <thead class="bg-gray-200">
                        <tr>
                            <th class="border border-gray-300 px-3 py-2">No.</th>
                            <th class="border border-gray-300 px-3 py-2">Requested By</th>
                            <th class="border border-gray-300 px-3 py-2">Date</th>
                            <th class="border border-gray-300 px-3 py-2">Request Type</th>
                            <th class="border border-gray-300 px-3 py-2">Note</th>
                            <th class="border border-gray-300 px-3 py-2">Approved By</th>
                            <th class="border border-gray-300 px-3 py-2">Approval Note</th>

                            <c:if test="${activeView == 'upcoming'}">
                                <th class="border border-gray-300 px-3 py-2 text-center">IsTransfer</th>
                                </c:if>

                            <c:if test="${activeView == 'ongoing'}">
                                <th class="border border-gray-300 px-3 py-2 text-center">Revert Transfer</th>
                                </c:if>

                            <th class="border border-gray-300 px-3 py-2">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${approvedRequestList}" varStatus="loop">
                            <tr class="hover:bg-gray-100">
                                <td class="border border-gray-300 px-3 py-2">${loop.index + 1}</td>
                                <td class="border border-gray-300 px-3 py-2">${r.requestedByName}</td>
                                <td class="border border-gray-300 px-3 py-2">${r.requestDate}</td>
                                <td class="border border-gray-300 px-3 py-2">
                                    ${r.requestTypeName}
                                    <c:if test="${r.requestTypeName == 'Export'}"> (<i>${r.exportTypeName}</i>) </c:if>
                                    <c:if test="${r.requestTypeName == 'Import'}"> (<i>${r.importTypeName}</i>) </c:if>
                                    </td>
                                    <td class="border border-gray-300 px-3 py-2">${r.note}</td>
                                <td class="border border-gray-300 px-3 py-2">${r.approvedByName}</td>
                                <td class="border border-gray-300 px-3 py-2">${r.approvalNote}</td>

                                <!-- UPCOMING: hiển thị checkbox IsTransfer -->
                                <c:if test="${activeView == 'upcoming'}">
                                    <td class="border border-gray-300 px-3 py-2 text-center">
                                        <c:choose>
                                            <c:when test="${r.isTransferredToday}">
                                                ✅
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="requestIds" value="${r.requestId}" class="isTransferCheckbox" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:if>

                                <!-- ONGOING: hiển thị checkbox Revert -->
                                <c:if test="${activeView == 'ongoing'}">
                                    <td class="border border-gray-300 px-3 py-2 text-center">
                                        <input type="checkbox" name="revertRequestIds" value="${r.requestId}" class="revertTransferCheckbox" />
                                    </td>
                                </c:if>

                                <td class="border border-gray-300 px-3 py-2">
                                    <a href="warehousereport?requestId=${r.requestId}" class="text-blue-600 hover:underline">Detail</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- SAVE TRANSFER BUTTON -->
                <c:if test="${activeView == 'upcoming'}">
                    <button type="submit" id="saveTransferBtn"
                            class="hidden mt-4 bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                        Save Transfer
                    </button>
                </c:if>

                <!-- REVERT TRANSFER BUTTON -->
                <c:if test="${activeView == 'ongoing'}">
                    <button type="submit" id="revertTransferBtn"
                            class="hidden mt-4 bg-yellow-500 text-white px-4 py-2 rounded hover:bg-yellow-600">
                        Revert Transfer
                    </button>
                </c:if>
            </form>
        </div>

        <!-- SCRIPT -->
        <script>
            const checkboxes = document.querySelectorAll(".isTransferCheckbox");
            const saveBtn = document.getElementById("saveTransferBtn");

            const revertCheckboxes = document.querySelectorAll(".revertTransferCheckbox");
            const revertBtn = document.getElementById("revertTransferBtn");

            checkboxes.forEach(cb => {
                cb.addEventListener("change", () => {
                    const anyChecked = [...checkboxes].some(c => c.checked);
                    saveBtn?.classList.toggle("hidden", !anyChecked);
                });
            });

            revertCheckboxes.forEach(cb => {
                cb.addEventListener("change", () => {
                    const anyChecked = [...revertCheckboxes].some(c => c.checked);
                    revertBtn?.classList.toggle("hidden", !anyChecked);
                });
            });

            function submitTransfer(e) {
                e.preventDefault();

                const selectedTransfer = [...document.querySelectorAll(".isTransferCheckbox:checked")].map(cb => parseInt(cb.value));
                const selectedRevert = [...document.querySelectorAll(".revertTransferCheckbox:checked")].map(cb => parseInt(cb.value));

                if (selectedTransfer.length === 0 && selectedRevert.length === 0)
                    return;

                const confirmed = confirm("Are you sure you want to update the transfer status?");
                if (!confirmed)
                    return;

                fetch("tasklist?action=transfer", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        transferIds: selectedTransfer,
                        revertIds: selectedRevert
                    })
                })
                        .then(res => {
                            if (!res.ok)
                                throw new Error("Failed to update");
                            return res.json();
                        })
                        .then(data => {
                            if (data.success) {
                                alert("Transfer status updated successfully!");
                                location.reload();
                            } else {
                                alert("Failed to update transfer.");
                            }
                        })
                        .catch(err => {
                            console.error(err);
                            alert("Error occurred while updating.");
                        });
            }
        </script>

    </body>
</html>