<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="max-w-5xl mx-auto bg-white rounded-lg shadow p-6">
    <h2 class="text-2xl font-bold text-blue-800 mb-6">Create Purchase Order</h2>

    <c:if test="${not empty error}">
        <div class="bg-red-100 text-red-700 border border-red-300 rounded p-3 mb-4">
            ${error}
        </div>
    </c:if>

    <p class="text-sm text-gray-700 mb-4">
        <strong>Sender Request:</strong> ${senderName}
    </p>

    <form id="submitForm" action="create-purchase-order" method="post">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="submitFlag" value="true" />

        <div class="overflow-x-auto">
            <table class="min-w-full bg-white border rounded text-sm">
                <thead class="bg-gray-100 text-gray-700">
                    <tr>
                        <th class="p-2 border text-center">No</th>
                        <th class="p-2 border text-left">Material</th>
                        <th class="p-2 border text-right">Quantity</th>
                        <th class="p-2 border text-right">Unit Price</th>
                        <th class="p-2 border text-right">Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${detailList}" varStatus="loop">
                        <tr class="hover:bg-gray-50">
                            <td class="p-2 border text-center">${loop.index + 1}</td>
                            <td class="p-2 border">${m.materialName}</td>
                            <td class="p-2 border text-right" id="qty_${m.materialId}">${m.quantity}</td>
                            <td class="p-2 border">
                                <div class="flex justify-end">
                                    <div class="relative w-1/2"> <%-- 1/2 chiều rộng td --%>
                                        <input type="number"
                                               name="unitPrice_${m.materialId}"
                                               id="unitPrice_${m.materialId}"
                                               step="1000"
                                               min="0"
                                               required
                                               placeholder="0"
                                               class="w-full pl-2 pr-10 py-1.5 rounded-md border border-gray-300 shadow-sm text-sm text-right focus:ring-2 focus:ring-blue-500 focus:outline-none"
                                               oninput="updateTotal(${m.materialId}, ${m.quantity})" />
                                        <span class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500 text-xs font-medium">VND</span>
                                    </div>
                                </div>
                            </td>
                            <td class="p-2 border text-right text-green-700 font-semibold" id="total_${m.materialId}">0</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr class="bg-gray-100 font-bold">
                        <td colspan="4" class="p-2 border text-right">Total Value (VND)</td>
                        <td class="p-2 border text-right text-green-800" id="grandTotal">0</td>
                    </tr>
                </tfoot>
            </table>
        </div>


        <div class="flex items-start gap-2 text-sm text-gray-700 my-4">
            <input type="checkbox" id="confirmCheckbox" class="h-4 w-4 mt-1 text-green-600" />
            <label for="confirmCheckbox">
                I confirm that I take full responsibility for this purchase request.
            </label>
        </div>

        <div class="flex justify-end gap-4">
            <button type="button"
                    onclick="confirmSubmit()"
                    class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-lg shadow">
                Submit Purchase Order
            </button>

            <button type="button"
                    onclick="window.location.href = '${pageContext.request.contextPath}/purchase-order'"
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
    function updateTotal(materialId, quantity) {
        const unitPriceInput = document.getElementById('unitPrice_' + materialId);
        const totalCell = document.getElementById('total_' + materialId);

        const unitPrice = parseFloat(unitPriceInput.value) || 0;
        const total = unitPrice * quantity;

        totalCell.textContent = total.toLocaleString('en-US');

        updateGrandTotal();
    }

    function updateGrandTotal() {
        let grandTotal = 0;
        document.querySelectorAll('[id^="total_"]').forEach(cell => {
            const val = parseFloat(cell.textContent.replace(/,/g, '')) || 0;
            grandTotal += val;
        });
        document.getElementById('grandTotal').textContent = grandTotal.toLocaleString('en-US');
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
            window.location.href = 'purchase-order';
        }, 2000);
    </script>
</c:if>
