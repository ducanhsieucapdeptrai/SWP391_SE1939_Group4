<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Request Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
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

            <!-- Buttons & Back link -->
            <div class="mt-6 flex flex-col gap-4">
                <div class="flex gap-4">
                    <!-- Approve button -->
                    <c:choose>
                        <c:when test="${requestInfo.requestType.requestTypeName == 'Material Purchase'}">
                            <button
                                class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
                                onclick="openApproveModal()">
                                Approve
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button
                                class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded"
                                onclick="simpleApprove()">
                                Approve
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <!-- Reject button -->
                    <button
                        class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded"
                        onclick="openRejectDialog()">
                        Reject
                    </button>
                </div>

                <!-- Back to List -->
                <div>
                    <a href="${pageContext.request.contextPath}/pending-requests"
                       class="inline-block text-blue-600 hover:underline text-sm">
                        &larr; Back to Request List
                    </a>
                </div>
            </div>

        </div>

        <!-- Approve Modal (unchanged) -->
        <div id="approveModal" class="fixed inset-0 bg-black bg-opacity-50 hidden items-center justify-center z-50">
            <!-- ... modal nội dung như đã định nghĩa trước ... -->
        </div>

        <script>
            function openApproveModal() { /* ... */
            }
            function closeApproveModal() { /* ... */
            }
            function addRow() { /* ... */
            }

            function simpleApprove() {
                Swal.fire({
                    title: 'Approve this request?',
                    input: 'textarea',
                    inputLabel: 'Approval Note',
                    showCancelButton: true,
                    confirmButtonText: 'Approve'
                }).then(res => {
                    if (res.isConfirmed) {
                        const f = document.createElement('form');
                        f.method = 'post';
                        f.action = 'request-detail';
                        f.innerHTML = `
                            <input name="action" value="approve" hidden/>
                            <input name="id" value="${requestInfo.requestId}" hidden/>
                            <input name="approveNote" value="${res.value || ''}" hidden/>`;
                        document.body.appendChild(f);
                        f.submit();
                    }
                });
            }

            function openRejectDialog() {
                Swal.fire({
                    title: 'Reject this request?',
                    input: 'text',
                    inputLabel: 'Reason for rejection',
                    showCancelButton: true,
                    confirmButtonText: 'Reject'
                }).then(res => {
                    if (res.isConfirmed) {
                        const f = document.createElement('form');
                        f.method = 'post';
                        f.action = 'request-detail';
                        f.innerHTML = `
                            <input name="action" value="reject" hidden/>
                            <input name="id"     value="${requestInfo.requestId}" hidden/>
                            <input name="reason" value="${res.value}" hidden/>`;
                        document.body.appendChild(f);
                        f.submit();
                    }
                });
            }
        </script>
    </body>
</html>
