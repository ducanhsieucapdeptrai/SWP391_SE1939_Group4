<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied - 403</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100 min-h-screen flex items-center justify-center">
    <div class="max-w-md w-full bg-white rounded-lg shadow-lg p-8 text-center">
        <div class="mb-6">
            <i class="fas fa-shield-alt text-6xl text-red-500 mb-4"></i>
            <h1 class="text-3xl font-bold text-gray-800 mb-2">Access Denied</h1>
            <p class="text-gray-600">You don't have permission to access this resource</p>
        </div>
        
        <div class="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <p class="text-sm text-red-700">
                <strong>Error:</strong> 
                <c:choose>
                    <c:when test="${not empty param.error}">${param.error}</c:when>
                    <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                    <c:otherwise>Access denied</c:otherwise>
                </c:choose>
            </p>
            <c:if test="${not empty param.url or not empty requestedUrl}">
                <p class="text-xs text-red-600 mt-2">
                    Requested URL: 
                    <c:choose>
                        <c:when test="${not empty param.url}">${param.url}</c:when>
                        <c:otherwise>${requestedUrl}</c:otherwise>
                    </c:choose>
                </p>
            </c:if>
        </div>
        
        <div class="space-y-3">
            <a href="${pageContext.request.contextPath}/dashboard" 
               class="block w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition duration-200">
                <i class="fas fa-home mr-2"></i>Go to Dashboard
            </a>
            <button onclick="history.back()" 
                    class="block w-full bg-gray-500 text-white py-2 px-4 rounded hover:bg-gray-600 transition duration-200">
                <i class="fas fa-arrow-left mr-2"></i>Go Back
            </button>
        </div>
        
        <div class="mt-6 text-xs text-gray-500">
            <p>If you believe this is an error, please contact your system administrator.</p>
        </div>
    </div>
</body>
</html>
