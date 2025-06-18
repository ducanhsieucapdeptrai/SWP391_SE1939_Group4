<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Purchase Order Detail</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body class="p-6 bg-gray-100">

        <h1 class="text-2xl font-semibold mb-4">Purchase Order Detail</h1>

        <div class="bg-white shadow p-4 rounded">
            <p><strong>ID:</strong> ${po.poId}</p>
            <p><strong>Created By:</strong> ${po.createdByName}</p>
            <p><strong>Status:</strong> ${po.status}</p>
            <p><strong>Date:</strong> ${po.createdDate}</p>
            <p><strong>Note:</strong> ${po.note}</p>
        </div>

        <div class="mt-6 flex gap-4">
            <button onclick="approve()" class="bg-green-600 text-white px-4 py-2 rounded">Approve</button>
            <button onclick="reject()" class="bg-red-600 text-white px-4 py-2 rounded">Reject</button>
        </div>

        <form id="actionForm" method="post" action="${pageContext.request.contextPath}/po-detail" style="display:none;">
            <input type="hidden" name="id" value="${po.poId}" />
            <input type="hidden" name="action" id="actionType" />
            <input type="hidden" name="note" id="noteInput" />
        </form>

        <script>
            // Hiển thị thông báo từ request (nếu có)
            window.onload = function () {
                const successMessage = "${successMessage}";
                const errorMessage = "${errorMessage}";

                if (successMessage) {
                    Swal.fire({
                        icon: 'success',
                        title: successMessage,
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        // Tự động chuyển về trang danh sách sau khi hiển thị thông báo
                        window.location.href = "${pageContext.request.contextPath}/pending-po";
                    });
                }

                if (errorMessage) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: errorMessage
                    });
                }
            };

            function approve() {
                Swal.fire({
                    title: 'Approve this order?',
                    input: 'text',
                    inputLabel: 'Note',
                    inputPlaceholder: 'Optional note...',
                    showCancelButton: true,
                    confirmButtonText: 'Approve',
                    preConfirm: (note) => {
                        document.getElementById("actionType").value = "approve";
                        document.getElementById("noteInput").value = note;
                        document.getElementById("actionForm").submit();
                    }
                });
            }

            function reject() {
                Swal.fire({
                    title: 'Reject this order?',
                    input: 'text',
                    inputLabel: 'Reason',
                    inputPlaceholder: 'Enter reason...',
                    showCancelButton: true,
                    confirmButtonText: 'Reject',
                    preConfirm: (note) => {
                        document.getElementById("actionType").value = "reject";
                        document.getElementById("noteInput").value = note;
                        document.getElementById("actionForm").submit();
                    }
                });
            }
        </script>

    </body>
</html>