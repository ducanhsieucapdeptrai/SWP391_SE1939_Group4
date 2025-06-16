<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Warehouse Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <div id="app" class="min-h-screen flex flex-col">
            <!-- Header -->
            <header class="bg-blue-900 text-white shadow-md">
                <div class="container mx-auto px-4 py-3 flex justify-between items-center">
                    <div class="flex items-center">
                        <img src="${pageContext.request.contextPath}/assets/images/LogoSlogan.png" class="w-8 h-8 mr-2">
                    </div>
                    <div class="flex items-center">
                        <div class="mr-4 hidden md:block">
                            <span>${sessionScope.currentUser.fullName}</span>
                            <span class="mx-2">|</span>
                            <span class="bg-blue-700 px-2 py-1 rounded text-xs">
                                ${sessionScope.currentUser.role.roleName}
                            </span>
                        </div>

                        <c:if test="${sessionScope.currentUser.role.roleName == 'Director'}">
                            <div class="mr-4 relative">
                                <a href="${pageContext.request.contextPath}/pending-po">
                                    <i class="fas fa-bell text-white text-xl"></i>
                                    <span class="absolute -top-1 -right-1 bg-red-600 text-white text-xs rounded-full px-1">!</span>
                                </a>
                            </div>
                        </c:if>

                        <div class="relative">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${sessionScope.currentUser.userImage}"
                                     class="w-16 aspect-square rounded-full object-cover shadow-md border border-white" />
                            </button>
                            <div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden">
                                <a href="${pageContext.request.contextPath}/user-detail?id=${sessionScope.currentUser.userId}" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Change password</a>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Sidebar + Main -->
            <div class="flex flex-1">
                <!-- Sidebar -->
                <aside class="bg-blue-800 text-white w-64 min-h-screen p-4 block">
                    <nav>
                        <ul>
                            <li>
                                <a href="${pageContext.request.contextPath}/dashboard" class="block px-4 py-2 hover:bg-gray-700">
                                    <i class="fas fa-tachometer-alt mr-2"></i>Dashboard
                                </a>
                            </li>

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Director'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/pending-requests" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-clipboard-list mr-2"></i>List Request
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Company Staff'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/employee-requests" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-list-alt mr-2"></i>My Request List
                                    </a>
                                </li>
                                <li>
                                    <a href="${pageContext.request.contextPath}/createrequest" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-file-import mr-2"></i>Create Request
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Warehouse Manager' || sessionScope.currentUser.role.roleName == 'Warehouse Staff'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/materiallist" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-boxes mr-2"></i>Inventory
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Warehouse Staff'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/submitwarehousereport" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-file-upload mr-2"></i>Submit Report
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.currentUser.role.roleName != 'Director' && sessionScope.currentUser.role.roleName != 'Company Staff'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/requestlist.jsp" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-file-alt mr-2"></i>Request
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Warehouse Manager'}">
                                <li>
                                    <a href="#" onclick="toggleSubmenu('userManagerSubmenu')" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-users mr-2"></i>User Manager
                                        <i class="fas fa-chevron-down float-right" id="userManagerChevron"></i>
                                    </a>
                                    <ul id="userManagerSubmenu" class="hidden ml-4 mt-1">
                                        <li>
                                            <a href="${pageContext.request.contextPath}/userlist" class="block px-3 py-2 hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-list mr-2"></i>User List
                                            </a>
                                        </li>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/user-matrix" class="block px-3 py-2 hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-key mr-2"></i>Authorization
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:if>

                            <li>
                                <a href="${pageContext.request.contextPath}/advanced-dashboard" class="block px-4 py-2 hover:bg-gray-700">
                                    <i class="fas fa-tools mr-2"></i>More
                                </a>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <!-- Main content -->
                <main class="flex-1 bg-gray-100 p-6 overflow-auto">
                    <c:if test="${not empty pageContent}">
                        <jsp:include page="${pageContent}" />
                    </c:if>
                </main>
            </div>
        </div>

        <script>
            function toggleSubmenu(id) {
                const submenu = document.getElementById(id);
                const chevron = document.getElementById("userManagerChevron");
                submenu.classList.toggle("hidden");
                chevron.classList.toggle("rotate-180");
            }

            document.getElementById("userMenuBtn")?.addEventListener("click", () => {
                document.getElementById("userMenu").classList.toggle("hidden");
            });
        </script>
    </body>
</html>