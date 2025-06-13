<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Request Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <!-- SweetAlert2 CDN -->
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

            <div class="mt-6 flex gap-4">
                <!-- Approve form -->
                <form id="approveForm" action="request-detail" method="post">
                    <input type="hidden" name="action" value="approve" />
                    <input type="hidden" name="id" value="${requestInfo.requestId}" />
                    <button type="button" onclick="confirmApprove()"
                            class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded shadow">
                        Approve
                    </button>
                </form>

                <!-- Reject form -->
                <form id="rejectForm" action="request-detail" method="post">
                    <input type="hidden" name="action" value="reject" />
                    <input type="hidden" name="id" value="${requestInfo.requestId}" />
                    <input type="hidden" name="reason" id="rejectReason" />
                    <button type="button" onclick="confirmReject()"
                            class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded shadow">
                        Reject
                    </button>
                </form>

            </div>
        </div>

        <script>
            function confirmApprove() {
                Swal.fire({
                    title: 'Approve this request?',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonColor: '#16a34a',
                    cancelButtonColor: '#d1d5db',
                    confirmButtonText: 'Yes, approve it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('approveForm').submit();
                    }
                });
            }

            function confirmReject() {
                Swal.fire({
                    title: 'Reject this request?',
                    input: 'text',
                    inputLabel: 'Reason for rejection',
                    inputPlaceholder: 'Enter reason here...',
                    inputValidator: (value) => {
                        if (!value.trim()) {
                            return 'You must enter a reason!';
                        }
                    },
                    showCancelButton: true,
                    confirmButtonColor: '#dc2626',
                    cancelButtonColor: '#d1d5db',
                    confirmButtonText: 'Reject'
                }).then((result) => {
                    if (result.isConfirmed) {
                        document.getElementById('rejectReason').value = result.value;
                        document.getElementById('rejectForm').submit();
                    }
                });
            }
        </script>
    </body>
</html>
