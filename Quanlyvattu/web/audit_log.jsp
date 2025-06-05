<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Material Detail</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 p-6">
    <div class="max-w-4xl mx-auto bg-white p-8 rounded-xl shadow-md">
        <h1 class="text-2xl font-bold text-gray-800 mb-6">Material Details</h1>

        <div class="grid grid-cols-2 gap-6 text-gray-700">
            <div>
                <p class="font-semibold">Material Name</p>
                <p class="bg-gray-100 p-2 rounded">${m.materialName}</p>
            </div>

            <div>
                <p class="font-semibold">Category</p>
                <p class="bg-gray-100 p-2 rounded">${m.categoryName}</p>
            </div>

            <div>
                <p class="font-semibold">Subcategory</p>
                <p class="bg-gray-100 p-2 rounded">${m.subCategoryName}</p>
            </div>

            <div>
                <p class="font-semibold">Price</p>
                <p class="bg-gray-100 p-2 rounded">
                    <fmt:formatNumber value="${m.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                </p>
            </div>

            <div>
                <p class="font-semibold">Current Stock</p>
                <p class="bg-gray-100 p-2 rounded">${m.quantity}</p>
            </div>

            <div>
                <p class="font-semibold">Minimum Stock</p>
                <p class="bg-gray-100 p-2 rounded">${m.minQuantity}</p>
            </div>

            <div>
                <p class="font-semibold">Status</p>
                <p class="bg-gray-100 p-2 rounded">${m.statusName}</p>
            </div>
        </div>

        <!-- Nút điều hướng -->
        <div class="mt-8 flex justify-between">
            <!-- Quay lại danh sách -->
            <a href="${pageContext.request.contextPath}/materiallist" class="bg-gray-500 hover:bg-gray-600 text-white py-2 px-4 rounded">
                Back to List
            </a>

            <!-- Nút Edit -->
            <a href="${pageContext.request.contextPath}/editmaterial?materialId=${m.materialId}" 
               class="bg-blue-600 hover:bg-blue-700 text-white py-2 px-4 rounded">
                ✏️ Edit
            </a>
        </div>
    </div>
</body>
</html>
