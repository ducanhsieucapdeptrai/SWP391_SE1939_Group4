<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="max-w-5xl mx-auto bg-white rounded-lg shadow p-6">
    <h2 class="text-2xl font-bold text-blue-800 mb-6">Create Purchase Order</h2>

    <div class="overflow-x-auto">
        <table class="min-w-full bg-white border rounded text-sm">
            <thead class="bg-gray-100 text-gray-700">
                <tr>
                    <th class="p-2 border text-center">#</th>
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

    <div class="mt-6 flex justify-end gap-4">
        <form id="submitForm" action="create-po" method="post">
            <input type="hidden" name="requestId" value="${requestId}" />
            <input type="hidden" name="submit" value="true" />
            <button type="submit"
                    class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg shadow">
                Submit Purchase Order
            </button>
        </form>
        <form action="my-request" method="get">
            <button type="submit"
                    class="bg-gray-500 hover:bg-gray-600 text-white px-6 py-2 rounded-lg shadow">
                Cancel
            </button>
        </form>
    </div>
</div>

<script>
    function confirmSubmit() {
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
