<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="max-w-6xl mx-auto bg-white p-6 mt-6 rounded shadow">

    <h2 class="text-2xl font-bold text-blue-800 mb-4">Purchase Order Detail - PO#${po.poId}</h2>

    <div class="grid md:grid-cols-2 gap-4 text-sm mb-6">
        <p><strong>Request ID:</strong> ${po.requestId}</p>
        <p><strong>Created By:</strong> ${po.createdByName}</p>
        <p><strong>Created Date:</strong>
            <fmt:formatDate value="${po.createdDate}" pattern="yyyy-MM-dd HH:mm"/>
        </p>
        <p><strong>Status:</strong>
            <span class="
                  px-2 py-1 rounded-full text-xs font-semibold
                  ${po.status == 'Pending' ? 'bg-yellow-100 text-yellow-700' : ''}
                  ${po.status == 'Approved' ? 'bg-green-100 text-green-700' : ''}
                  ${po.status == 'Rejected' ? 'bg-red-100 text-red-700' : ''}
                  ">
                ${po.status}
            </span>
        </p>
        <p class="col-span-2"><strong>Note:</strong> ${po.note}</p>
        <p class="col-span-2"><strong>Total Price:</strong>
            <span class="text-red-600 font-semibold">
                <fmt:formatNumber value="${po.totalPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> VND
            </span>
        </p>
    </div>

    <div class="overflow-x-auto mb-10">
        <table class="w-full border-collapse text-sm text-gray-800">
            <thead>
                <tr class="bg-blue-100 text-left">
                    <th class="border px-4 py-2 w-10">No</th>
                    <th class="border px-4 py-2">Material Name</th>
                    <th class="border px-4 py-2 w-28 text-center">Quantity</th>
                    <th class="border px-4 py-2 w-32 text-right">Unit Price</th>
                    <th class="border px-4 py-2 w-32 text-right">Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${po.details}" varStatus="i">
                    <tr class="hover:bg-gray-50">
                        <td class="border px-4 py-2">${i.index + 1}</td>
                        <td class="border px-4 py-2">${item.materialName}</td>
                        <td class="border px-4 py-2 text-center">${item.quantity}</td>
                        <td class="border px-4 py-2 text-right">
                            <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> VND
                        </td>
                        <td class="border px-4 py-2 text-right">
                            <fmt:formatNumber value="${item.total}" type="number" groupingUsed="true" maxFractionDigits="0"/> VND
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty po.details}">
                    <tr><td colspan="5" class="text-center text-gray-400 py-4">No materials found.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="flex justify-between items-center">
        <a href="purchase-request-list" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">Back</a>
        <c:if test="${po.status == 'Pending' and sessionScope.userRole == 'Director'}">
            <form method="post" action="purchase-order-detail" class="flex gap-3">
                <input type="hidden" name="poId" value="${po.poId}">
                <button type="submit" name="action" value="approve"
                        class="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded shadow">Approve</button>
                <button type="submit" name="action" value="reject"
                        class="bg-red-600 hover:bg-red-700 text-white px-5 py-2 rounded shadow">Reject</button>
            </form>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <c:if test="${not empty alertMessage}">
        <script>
            Swal.fire({
                icon: 'success',
                title: '${alertMessage}',
                showConfirmButton: false,
                timer: 3000
            });

            setTimeout(() => {
                window.location.href = '${pageContext.request.contextPath}/purchase-request-list';
            }, 3000);
        </script>
    </c:if>

</div>
