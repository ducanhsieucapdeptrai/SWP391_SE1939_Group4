<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Purchase Order</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <!-- ✅ SweetAlert -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body class="bg-gray-100">

        <!-- ✅ SweetAlert hiển thị thông báo khi tạo thành công -->
        <c:if test="${success == 'po_created'}">
            <script>
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Purchase Order has been created successfully.Waiting for director confirmation!',
                    confirmButtonColor: '#3085d6'
                });
            </script>
        </c:if>

        <div class="container mx-auto p-6 bg-white rounded shadow mt-6">
            <h2 class="text-2xl font-bold mb-4">Create Purchase Order</h2>

            <form action="${pageContext.request.contextPath}/create-po" method="post">
                <input type="hidden" name="requestId" value="${requestInfo.requestId}"/>

                <table class="min-w-full bg-white border rounded shadow mb-4">
                    <thead class="bg-gray-200">
                        <tr>
                            <th class="px-4 py-2 text-left">Material</th>
                            <th class="px-4 py-2 text-left">Quantity</th>
                            <th class="px-4 py-2 text-left">Unit Price (VND)</th>
                            <th class="px-4 py-2 text-left">Total Price (VND)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="grandTotal" value="0"/>
                        <c:forEach var="item" items="${details}">
                            <tr class="border-b">
                                <td class="px-4 py-2">${item.material.materialName}</td>
                                <td class="px-4 py-2">${item.quantity}</td>
                                <td class="px-4 py-2">${item.material.price}</td>
                                <td class="px-4 py-2">
                                    <c:set var="lineTotal" value="${item.material.price * item.quantity}"/>
                                    ${lineTotal}
                                    <c:set var="grandTotal" value="${grandTotal + lineTotal}"/>
                                </td>
                        <input type="hidden" name="materialIds" value="${item.material.materialId}"/>
                        <input type="hidden" name="quantities" value="${item.quantity}"/>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div class="text-right text-lg font-semibold mb-4">
                    Grand Total: <span class="text-green-600">${grandTotal} VND</span>
                </div>

                <!-- Note input -->
                <div class="mb-6">
                    <label for="note" class="block text-gray-700 font-semibold mb-2">Note (optional):</label>
                    <textarea name="note" id="note" rows="3"
                              class="w-full border border-gray-300 rounded px-4 py-2"
                              placeholder="Add a note to this purchase order..."></textarea>
                </div>

                <div class="flex justify-between items-center mt-6">
                    <div class="mt-4">
                        <a href="${pageContext.request.contextPath}/employee-requests"
                           class="bg-gray-300 text-black px-4 py-2 rounded hover:bg-gray-400">
                            ← Back to Request List
                        </a>
                    </div>

                    <!-- Nếu đã có status Approved thì ẩn nút submit -->
                    <c:choose>
                        <c:when test="${requestInfo.status == 'Approved'}">
                            <button type="button" class="bg-gray-500 text-white px-6 py-2 rounded cursor-not-allowed" disabled>
                                Pending...
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit"
                                    class="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700">
                                Submit Purchase Order
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </div>
    </body>
</html>
