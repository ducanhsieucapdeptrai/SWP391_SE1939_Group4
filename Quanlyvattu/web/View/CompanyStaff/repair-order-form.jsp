<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="max-w-5xl mx-auto bg-white rounded-lg shadow p-6">
    <h2 class="text-2xl font-bold text-yellow-800 mb-6">Create Repair Order</h2>

    <c:if test="${not empty error}">
        <c:if test="${not empty debugLogs}">
            <div class="bg-gray-100 text-sm text-gray-800 border border-gray-300 rounded p-3 mb-4">
                <strong>Debug Logs:</strong><br/>
                <c:out value="${debugLogs}" escapeXml="false" />
            </div>
        </c:if>

        <div class="bg-red-100 text-red-700 border border-red-300 rounded p-3 mb-4">
            ${error}
        </div>
    </c:if>


    <p class="text-sm text-gray-700 mb-4">
        <strong>Sender:</strong> ${senderName}
    </p>

    <form id="submitForm" action="create-repair-order" method="post">
        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="submitFlag" value="true" />

        <div class="overflow-x-auto">
            <table class="min-w-full bg-white border rounded text-sm">
                <thead class="bg-gray-100 text-gray-700">
                    <tr>
                        <th class="p-2 border text-center">No</th>
                        <th class="p-2 border text-left">Material</th>
                        <th class="p-2 border text-right">Quantity</th>
                        <th class="p-2 border text-right">Estimated Price</th>
                        <th class="p-2 border text-left">Note</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${detailList}" varStatus="loop">
                        <tr class="hover:bg-gray-50">
                            <td class="p-2 border text-center">${loop.index + 1}</td>
                            <td class="p-2 border">${m.materialName}</td>
                            <td class="p-2 border text-right">${m.quantity}</td>
                            <td class="p-2 border text-right">
                                <div class="flex items-center justify-end gap-1">
                                    <input type="text"
                                           name="estimatedPriceDisplay_${loop.index}"
                                           value="<fmt:formatNumber value='${m.unitPrice}' type='number' groupingUsed='true'/>"
                                           class="w-32 text-right border rounded p-2 text-base font-medium estimated-price-display"
                                           data-index="${loop.index}" required />
                                    <span class="text-gray-700 font-medium text-sm">VND</span>
                                </div>
                                <input type="hidden" name="estimatedPrice_${loop.index}" id="estimatedPrice_${loop.index}"
                                       value="${m.unitPrice}" />
                                <input type="hidden" name="materialId_${loop.index}" value="${m.materialId}" />
                                <input type="hidden" name="quantity_${loop.index}" value="${m.quantity}" />
                            </td>
                            <td class="p-2 border text-left">
                                <input type="text"
                                       name="note_${loop.index}"
                                       value="${m.mnote}"
                                       class="w-full border rounded p-2 text-sm"
                                       placeholder="Enter note (optional)" />
                            </td>


                        </tr>
                    </c:forEach>

                </tbody>
            </table>
        </div>

        <div class="flex items-start gap-2 text-sm text-gray-700 my-4">
            <input type="checkbox" id="confirmCheckbox" class="h-4 w-4 mt-1 text-yellow-600" />
            <label for="confirmCheckbox">
                I confirm that I take full responsibility for this repair request.
            </label>
        </div>

        <div class="flex justify-end gap-4">
            <button type="button"
                    onclick="confirmSubmit()"
                    class="bg-yellow-600 hover:bg-yellow-700 text-white px-6 py-2 rounded-lg shadow">
                Submit Repair Order
            </button>

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
                text: 'You must confirm responsibility before submitting the repair order.',
                confirmButtonText: 'OK'
            });
            return;
        }

        Swal.fire({
            title: 'Confirm Submit',
            text: 'Are you sure you want to submit this Repair Order?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#d97706',
            cancelButtonColor: '#6b7280',
            confirmButtonText: 'Yes, Submit'
        }).then((result) => {
            if (result.isConfirmed) {
                document.getElementById('submitForm').submit();
            }
        });
    }
</script>
<script>
    document.querySelectorAll('.estimated-price-display').forEach(input => {
        input.addEventListener('input', function () {
            // Lấy chỉ số index để xác định input ẩn
            const index = this.dataset.index;

            // Xoá dấu chấm và ký tự không hợp lệ
            const raw = this.value.replace(/\./g, '').replace(/[^0-9]/g, '');
            const number = parseFloat(raw || 0);

            // Hiển thị lại dạng có dấu chấm
            this.value = number.toLocaleString('vi-VN');

            // Gán giá trị vào hidden input
            document.getElementById('estimatedPrice_' + index).value = number;
        });
    });
</script>


<c:if test="${roCreated}">
    <script>
        Swal.fire({
            icon: 'success',
            title: 'Success!',
            text: 'Repair Order has been created successfully.',
            showConfirmButton: false,
            timer: 2000
        });

        setTimeout(() => {
            window.location.href = 'my-request';
        }, 2000);
    </script>
</c:if>
