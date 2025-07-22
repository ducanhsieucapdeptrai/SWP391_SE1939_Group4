<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="Helper.AuthorizationHelper" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="max-w-5xl mx-auto bg-white rounded-lg shadow p-6">
    <h2 class="text-2xl font-bold text-blue-800 mb-6">Create Purchase Order</h2>

    <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 border border-red-300 rounded p-3 mb-4">
            ${error}
        </div>
    </c:if>

    <p class="text-sm text-gray-700 mb-4">
        <strong>Sender:</strong> ${senderName}
    </p>

    <div class="overflow-x-auto">
        <table class="min-w-full bg-white border rounded text-sm">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="p-2 border text-center">No</th>
                    <th class="p-2 border text-left">Material</th>
                    <th class="p-2 border text-right">Quantity</th>
                    <th class="p-2 border text-right">Price</th>
                    <th class="p-2 border text-right">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="m" items="${detailList}" varStatus="loop">
                    <tr class="hover:bg-gray-50">
                        <td class="p-2 border text-center">${loop.index + 1}</td>
                        <td class="p-2 border">${m.materialName}</td>
                        <td class="p-2 border text-right">${m.quantity}</td>
                        <td class="p-2 border text-right">
                            <fmt:formatNumber value="${m.price}" type="number" groupingUsed="true"/>
                        </td>
                        <td class="p-2 border text-right text-green-700 font-semibold">
                            <fmt:formatNumber value="${m.total}" type="number" groupingUsed="true"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr class="bg-gray-100 font-bold text-right">
                    <td colspan="4" class="p-2 border text-right">Total</td>
                    <td class="p-2 border text-green-800">
                        <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/>
                    </td>
                </tr>
            </tfoot>
        </table>
    </div>

    <form id="submitForm" action="create-po" method="post" class="mt-6">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="submitFlag" value="true" />

        <div class="flex items-start gap-2 text-sm text-gray-700 mb-4">
            <input type="checkbox" id="confirmCheckbox" class="h-4 w-4 mt-1 text-green-600" />
            <label for="confirmCheckbox">
                I confirm that I take full responsibility for this purchase request.
            </label>
        </div>

        <div class="flex justify-end gap-4">
            <!-- Submit button -->
                            <% if (AuthorizationHelper.hasPermission(request, "/create-po")) { %>

            <button type="button"
                    onclick="confirmSubmit()"
                    class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg shadow">
                Submit Purchase Order
            </button>
                                                        <% } %>

            <!-- Cancel button -->
            <button type="button"
                    onclick="window.location.href = 'my-request'"
                    class="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg shadow">
                Cancel
            </button>
        </div>
    </form>
</div>

<script>
    function confirmSubmit() {
        const checkbox = document.getElementById('confirmCheckbox');
        if (!checkbox.checked) {
            Swal.fire({
                icon: 'warning',
                title: 'Confirmation Required',
                text: 'You must confirm responsibility before submitting the order.',
                confirmButtonText: 'OK'
            });
            return;
        }

        Swal.fire({
            title: 'Confirm Submit',
            text: 'Are you sure you want to submit this Purchase Order?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#16a34a',
            cancelButtonColor: '#6b7280',
            confirmButtonText: 'Yes, Submit'
        }).then((result) => {
            if (result.isConfirmed) {
                document.getElementById('submitForm').submit();
            }
        });
    }
</script>

<c:if test="${poCreated}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Success!',
            text: 'Purchase Order has been created successfully.',
            showConfirmButton: false,
            timer: 2000
        });

        setTimeout(() => {
            window.location.href = 'my-request';
        }, 2000);
    </script>
</c:if>
