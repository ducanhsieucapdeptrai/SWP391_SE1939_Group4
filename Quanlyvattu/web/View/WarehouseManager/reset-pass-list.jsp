<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="bg-white rounded-xl p-6 shadow max-w-6xl mx-auto">
    <h2 class="text-2xl font-bold mb-6 text-blue-800">Pending Password Reset Requests</h2>

    <c:if test="${empty resetRequests}">
        <div class="bg-blue-100 text-blue-800 px-4 py-3 rounded shadow-sm">
            There are no pending requests.
        </div>
    </c:if>

    <c:if test="${not empty resetRequests}">
        <div class="overflow-x-auto">
            <table class="min-w-full text-sm border border-gray-200 rounded shadow-sm">
                <thead class="bg-gray-100">
                    <tr>
                        <th class="border px-4 py-2">No.</th>
                        <th class="border px-4 py-2">Full Name</th>
                        <th class="border px-4 py-2">Email</th>
                        <th class="border px-4 py-2">Phone</th>
                        <th class="border px-4 py-2">Requested At</th>
                        <th class="border px-4 py-2 text-center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${resetRequests}" varStatus="i">
                        <tr class="hover:bg-gray-50">
                            <td class="border px-4 py-2">${i.index + 1}</td>
                            <td class="border px-4 py-2">${r.fullName}</td>
                            <td class="border px-4 py-2">${r.email}</td>
                            <td class="border px-4 py-2">${r.phone}</td>
                            <td class="border px-4 py-2">${r.requestedAt}</td>
                            <td class="border px-4 py-2 text-center">
                                <button class="bg-blue-600 hover:bg-blue-700 text-white px-3 py-1 rounded text-sm"
                                        onclick="openResetModal(${r.userId}, ${r.requestId}, '${fn:escapeXml(r.fullName)}')">
                                    Reset Password
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<!-- Modal -->
<div id="resetModal" class="fixed inset-0 bg-black bg-opacity-30 hidden items-center justify-center z-50">
    <div class="bg-white rounded-xl p-6 shadow w-full max-w-md">
        <h3 class="text-xl font-bold mb-4">Reset Password for <span id="modalUserName"></span></h3>
        <form id="resetForm" method="post" action="reset-pass-list">
            <input type="hidden" name="userId" id="modalUserId" />
            <input type="hidden" name="requestId" id="modalRequestId" />
            <div class="mb-4">
                <label class="block font-semibold mb-1">New Password:</label>
                <input type="password" id="newPassword" name="newPassword" class="w-full border px-3 py-2 rounded" required />
            </div>
            <div class="mb-4">
                <label class="block font-semibold mb-1">Confirm Password:</label>
                <input type="password" id="confirmPassword" class="w-full border px-3 py-2 rounded" required />
                <p id="passwordMismatchMsg" class="text-red-500 text-sm hidden mt-1">Passwords do not match.</p>
            </div>
            <div class="flex justify-end gap-2">
                <button type="button" onclick="closeResetModal()" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Cancel</button>
                <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Submit</button>
            </div>
        </form>
    </div>
</div>

<!-- SweetAlert + Modal logic -->
<script>
    function openResetModal(userId, requestId, fullName) {
        document.getElementById('modalUserId').value = userId;
        document.getElementById('modalRequestId').value = requestId;
        document.getElementById('modalUserName').innerText = fullName;
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmPassword').value = '';
        document.getElementById('passwordMismatchMsg').classList.add('hidden');
        document.getElementById('resetModal').classList.remove('hidden');
        document.getElementById('resetModal').classList.add('flex');
    }

    function closeResetModal() {
        document.getElementById('resetModal').classList.add('hidden');
        document.getElementById('resetModal').classList.remove('flex');
    }

    document.getElementById("resetForm").addEventListener("submit", function (e) {
        const pass1 = document.getElementById("newPassword").value.trim();
        const pass2 = document.getElementById("confirmPassword").value.trim();
        const msg = document.getElementById("passwordMismatchMsg");

        if (pass1.length < 6) {
            e.preventDefault();
            msg.textContent = "Password must be at least 6 characters.";
            msg.classList.remove("hidden");
        } else if (pass1 !== pass2) {
            e.preventDefault();
            msg.textContent = "Passwords do not match.";
            msg.classList.remove("hidden");
        } else {
            msg.classList.add("hidden");
        }
    });
</script>

<c:if test="${not empty alertMessage}">
    <script>
        Swal.fire({
            icon: '${alertType}',
            title: '${alertMessage}',
            showConfirmButton: false,
            timer: 3000
        }).then(() => {
            window.location.href = 'reset-pass-list';
        });
    </script>
</c:if>
