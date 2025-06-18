<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Request Detail</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body class="bg-gray-100">

        <div class="container mx-auto px-6 py-4 bg-white rounded shadow mt-6">

            <h2 class="text-2xl font-semibold mb-4">Request Detail</h2>
            <div class="mb-4">
                <p><strong>Request Date:</strong> ${requestInfo.requestDate}</p>
                <p><strong>Requester:</strong> ${requestInfo.requester.fullName}</p>
                <p><strong>Note:</strong> ${requestInfo.note}</p>
                <p><strong>Type:</strong> ${requestInfo.requestType.requestTypeName}</p>
            </div>

            <h3 class="text-lg font-semibold mt-4 mb-2">Materials Requested</h3>
            <table class="min-w-full bg-white border rounded shadow">
                <thead class="bg-blue-800 text-white">
                    <tr>
                        <th class="px-4 py-2 text-left">Material</th>
                        <th class="px-4 py-2 text-left">Quantity</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${details}">
                        <tr class="border-b">
                            <td class="px-4 py-2">${item.material.materialName}</td>
                            <td class="px-4 py-2">${item.quantity}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Buttons: Only show for Director -->
            <c:if test="${sessionScope.currentUser.role.roleName eq 'Director'}">
                <div class="mt-6 flex flex-col gap-4">
                    <div class="flex gap-4">
                        <button class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
                                onclick="openApproveModal()">Approve</button>
                        <button class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded"
                                onclick="openRejectDialog()">Reject</button>
                    </div>
                </div>
            </c:if>

            <div class="mt-4">
                <c:choose>
                    <c:when test="${sessionScope.currentUser.role.roleName eq 'Director'}">
                        <a href="${pageContext.request.contextPath}/pending-requests"
                           class="inline-block text-blue-600 hover:underline text-sm">
                            &larr; Back to Request List
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/employee-requests"
                           class="inline-block text-blue-600 hover:underline text-sm">
                            &larr; Back to My Request List
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

        <!-- Modal: Only for Director + Material Purchase -->
        <c:if test="${sessionScope.currentUser.role.roleName eq 'Director'}">
            <div id="approveModal" class="fixed inset-0 bg-black bg-opacity-50 hidden z-50 flex justify-center items-center">
                <div class="bg-white p-6 rounded shadow-lg w-3/4 max-w-2xl mr-0">
                    <h2 class="text-xl font-semibold mb-4">Approve & Edit Request</h2>
                    <form id="approveForm" action="request-detail" method="post" class="space-y-4">
                        <input type="hidden" name="action" value="approve"/>
                        <input type="hidden" name="id" value="${requestInfo.requestId}"/>

                        <table class="w-full border text-sm">
                            <thead class="bg-gray-200">
                                <tr>
                                    <th class="border px-2 py-1">Material</th>
                                    <th class="border px-2 py-1">Quantity</th>
                                </tr>
                            </thead>
                            <tbody id="approveTableBody">
                                <c:forEach var="item" items="${details}">
                                    <tr>
                                        <td class="border px-2 py-1">
                                            <select name="materialIds" class="w-full">
                                                <c:forEach var="m" items="${materialList}">
                                                    <option value="${m.materialId}"
                                                            ${m.materialId == item.material.materialId ? 'selected' : ''}>
                                                        ${m.materialName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="border px-2 py-1">
                                            <input type="number" name="quantities" value="${item.quantity}" min="1"
                                                   class="w-full border rounded px-1 py-0.5"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <button type="button" class="mt-2 text-blue-600 hover:underline" onclick="addRow()">+ Add Material</button>

                        <div>
                            <label class="block font-medium mb-1">Approval Note</label>
                            <textarea name="approveNote" rows="3" class="w-full border rounded px-2 py-1"
                                      placeholder="Enter any note..."></textarea>
                        </div>

                        <div class="text-right space-x-2">
                            <button type="button" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                                    onclick="closeApproveModal()">Cancel</button>
                            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                                Confirm Approve
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- JavaScript -->
        <script>
            function openApproveModal() {
                document.getElementById('approveModal').classList.remove('hidden');
                document.getElementById('approveModal').classList.add('flex');
            }

            function closeApproveModal() {
                document.getElementById('approveModal').classList.add('hidden');
                document.getElementById('approveModal').classList.remove('flex');
            }

            function addRow() {
                const tbody = document.getElementById('approveTableBody');
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td class="border px-2 py-1">
                        <select name="materialIds" class="w-full">
            <c:forEach var="m" items="${materialList}">
                            <option value="${m.materialId}">${m.materialName}</option>
            </c:forEach>
                        </select>
                    </td>
                    <td class="border px-2 py-1">
                        <input type="number" name="quantities" value="1" min="1" class="w-full border rounded px-1 py-0.5"/>
                    </td>`;
                tbody.appendChild(row);
            }

//            function simpleApprove() {
//                Swal.fire({
//                    title: 'Approve this request?',
//                    input: 'textarea',
//                    inputLabel: 'Approval Note',
//                    inputPlaceholder: 'Enter your note...',
//                    showCancelButton: true,
//                    confirmButtonText: 'Approve',
//                    cancelButtonText: 'Cancel',
//                    confirmButtonColor: '#16a34a'
//                }).then((res) => {
//                    if (res.isConfirmed) {
//                        const f = document.createElement('form');
//                        f.method = 'post';
//                        f.action = 'request-detail';
//                        f.innerHTML = `
//                            <input type="hidden" name="action" value="approve"/>
//                            <input type="hidden" name="id" value="${requestInfo.requestId}"/>
//                            <input type="hidden" name="approveNote" value="${res.value || ''}"/>`;
//                        document.body.appendChild(f);
//                        f.submit();
//                    }
//                });
//            }

            function openRejectDialog() {
                Swal.fire({
                    title: 'Reject this request?',
                    input: 'text',
                    inputLabel: 'Reason for rejection',
                    inputPlaceholder: 'Enter reason here...',
                    showCancelButton: true,
                    confirmButtonText: 'Reject',
                    cancelButtonText: 'Cancel',
                    confirmButtonColor: '#dc2626'
                }).then((res) => {
                    if (res.isConfirmed) {
                        const f = document.createElement('form');
                        f.method = 'post';
                        f.action = 'request-detail';
                        f.innerHTML = `
                            <input type="hidden" name="action" value="reject"/>
                            <input type="hidden" name="id" value="${requestInfo.requestId}"/>
                            <input type="hidden" name="reason" value="${res.value}"/>`;
                        document.body.appendChild(f);
                        f.submit();
                    }
                });
            }
        </script>

    </body>
</html>
