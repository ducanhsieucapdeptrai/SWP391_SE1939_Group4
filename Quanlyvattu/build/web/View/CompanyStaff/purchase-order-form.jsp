<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Purchase Order</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100 p-6">

        <div class="max-w-4xl mx-auto bg-white shadow-md rounded p-6">
            <h2 class="text-2xl font-semibold mb-6">Create Purchase Order</h2>

            <form action="create-po" method="post">
                <input type="hidden" name="requestId" value="${request.requestId}" />

                <!-- Request Info -->
                <div class="mb-4">
                    <label class="block font-medium mb-1">Request ID:</label>
                    <input type="text" value="${request.requestId}" class="w-full border px-3 py-2 rounded bg-gray-100" readonly />
                </div>

                <div class="mb-4">
                    <label class="block font-medium mb-1">Request Date:</label>
                    <input type="text" value="<fmt:formatDate value='${request.requestDate}' pattern='yyyy-MM-dd'/>"
                           class="w-full border px-3 py-2 rounded bg-gray-100" readonly />
                </div>

                <div class="mb-4">
                    <label class="block font-medium mb-1">Request Type:</label>
                    <input type="text" value="${request.requestType.requestTypeName}" class="w-full border px-3 py-2 rounded bg-gray-100" readonly />
                </div>

                <!-- Material List -->
                <div class="mb-6">
                    <h3 class="text-lg font-semibold mb-2">Material Details</h3>
                    <table class="min-w-full bg-white border rounded">
                        <thead class="bg-gray-200">
                            <tr>
                                <th class="px-3 py-2 text-left">Material Name</th>
                                <th class="px-3 py-2 text-left">Quantity</th>
                                <th class="px-3 py-2 text-left">Unit</th>
                                <th class="px-3 py-2 text-left">Note</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="detail" items="${request.requestDetails}">
                                <tr class="border-b">
                                    <td class="px-3 py-2">${detail.material.materialName}</td>
                                    <td class="px-3 py-2">${detail.quantity}</td>
                                    <td class="px-3 py-2">${detail.material.unit}</td>
                                    <td class="px-3 py-2">${detail.material.note}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Submit -->
                <div class="flex justify-end">
                    <button type="submit" class="bg-green-600 text-white px-5 py-2 rounded hover:bg-green-700">
                        Submit Purchase Order
                    </button>
                </div>
            </form>
        </div>

    </body>
</html>
