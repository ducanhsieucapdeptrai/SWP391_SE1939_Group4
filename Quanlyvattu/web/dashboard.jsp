<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container mx-auto">
    <h2 class="text-2xl font-semibold mb-6">Advanced Dashboard</h2>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">


        <!-- Statistics Button Card -->
        <div class="bg-white rounded-lg shadow-md p-6 transition-transform transform hover:scale-105">
            <div class="flex flex-col items-center text-center">
                <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mb-4">
                    <i class="fas fa-chart-pie text-green-600 text-2xl"></i>
                </div>
                <h3 class="text-lg font-semibold mb-2">Statistics</h3>
                <p class="text-gray-600 mb-4">Xem thống kê chi tiết về nhập, xuất, tồn kho vật tư.</p>
                <a href="${pageContext.request.contextPath}/material-statistics" class="bg-green-600 hover:bg-green-700 text-white py-2 px-4 rounded-lg transition duration-300">
                    View Statistics
                </a>
            </div>
        </div>

        <!-- Catalog Management Button Card -->
        <c:if test="${userRole == 'Warehouse Manager' }">

            <div class="bg-white rounded-lg shadow-md p-6 transition-transform transform hover:scale-105">
                <div class="flex flex-col items-center text-center">
                    <div class="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mb-4">
                        <i class="fas fa-book text-purple-600 text-2xl"></i>
                    </div>
                    <h3 class="text-lg font-semibold mb-2">Catalog Management</h3>
                    <p class="text-gray-600 mb-4">Manage catalog items and their descriptions.</p>
                    <a href="${pageContext.request.contextPath}/catalog-management" class="bg-purple-600 hover:bg-purple-700 text-white py-2 px-4 rounded-lg transition duration-300">
                        Manage Catalog
                    </a>
                </div>
            </div>
        </c:if>
        </div>



        <!-- Back to Dashboard Button -->
        <div class="text-center mt-8">
            <a href="${pageContext.request.contextPath}/dashboard" class="inline-flex items-center text-blue-600 hover:text-blue-800">
                <i class="fas fa-arrow-left mr-2"></i> Back to Main Dashboard
            </a>
        </div>
    </div>
