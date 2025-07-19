<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="max-w-6xl mx-auto bg-white p-6 mt-6 rounded shadow">

    <h2 class="text-2xl font-bold text-blue-800 mb-4">Repair Order Detail - RO#${ro.roId}</h2>

    <div class="grid md:grid-cols-2 gap-4 text-sm mb-6">
        <p><strong>Request ID:</strong> ${ro.requestId}</p>
        <p><strong>Created By:</strong> ${ro.createdByName}</p>
        <p><strong>Created Date:</strong>
            <fmt:formatDate value="${ro.createdDate}" pattern="yyyy-MM-dd HH:mm"/>
        </p>
        <p><strong>Status:</strong>
            <span class="
                  px-2 py-1 rounded-full text-xs font-semibold
                  ${ro.status == 'Pending' ? 'bg-yellow-100 text-yellow-700' : ''}
                  ${ro.status == 'Approved' ? 'bg-green-100 text-green-700' : ''}
                  ${ro.status == 'Rejected' ? 'bg-red-100 text-red-700' : ''}
                  ">
                ${ro.status}
            </span>
        </p>
        <p class="col-span-2"><strong>Note:</strong> ${ro.note}</p>
    </div>

    <div class="overflow-x-auto mb-10">
        <table class="w-full border-collapse text-sm text-gray-800">
            <thead>
                <tr class="bg-blue-100 text-left">
                    <th class="border px-4 py-2 w-10">No</th>
                    <th class="border px-4 py-2">Material Name</th>
                    <th class="border px-4 py-2 w-28 text-center">Quantity</th>
                    <th class="border px-4 py-2 w-32 text-right">Unit Price</th>
                    <th class="border px-4 py-2">Note</th> <!-- 🆕 Thêm cột -->
                </tr>
            </thead>

            <tbody>
                <c:forEach var="item" items="${ro.details}" varStatus="i">
                    <tr class="hover:bg-gray-50">
                        <td class="border px-4 py-2">${i.index + 1}</td>
                        <td class="border px-4 py-2">${item.materialName}</td>
                        <td class="border px-4 py-2 text-center">${item.quantity}</td>
                        <td class="border px-4 py-2 text-right">
                            <fmt:formatNumber value="${item.unitPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> VND
                        </td>
                        <td class="border px-4 py-2">${item.mnote}</td> <!-- 🆕 Hiển thị ghi chú -->
                    </tr>

                </c:forEach>
                <c:if test="${empty ro.details}">
                    <tr><td colspan="4" class="text-center text-gray-400 py-4">No materials found.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="flex justify-between items-center">
        <a href="${pageContext.request.contextPath}/repair-request-list" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">Back</a>
        <c:if test="${ro.status == 'Pending'}">
            <form method="post" action="repair-order-detail" class="flex gap-3">
                <input type="hidden" name="roId" value="${ro.roId}">
                <button type="submit" name="action" value="approve"
                        class="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded shadow">Approve</button>
                <button type="submit" name="action" value="reject"
                        class="bg-red-600 hover:bg-red-700 text-white px-5 py-2 rounded shadow">Reject</button>
            </form>
        </c:if>
    </div>
    <c:if test="${not empty debugLog}">
        <pre class="bg-gray-100 p-4 mt-6 border text-xs rounded text-red-700">
            ${debugLog}
        </pre>
    </c:if>
    <c:if test="${not empty debugLog}">
        <pre class="bg-gray-100 p-4 mt-6 border text-xs rounded text-red-700 overflow-auto whitespace-pre-wrap">
            ${debugLog}
        </pre>
    </c:if>


</div>
