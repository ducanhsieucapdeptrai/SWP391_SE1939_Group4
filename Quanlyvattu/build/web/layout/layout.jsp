<!-- Updated layout.jsp with improvements for consistency and clarity -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Construction Materials Warehouse Management System</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body class="font-roboto">
        <div id="app" class="min-h-screen flex flex-col">
            <!-- Header -->
            <header class="bg-blue-900 text-white shadow-md">
                <div class="container mx-auto px-4 py-3 flex justify-between items-center">
                    <div class="flex items-center">
                        <img src="${pageContext.request.contextPath}/assets/images/LogoSlogan.png" alt="Logo" class="w-8 h-8 mr-2">
                    </div>
                    <div class="flex items-center">
                        <div class="mr-4 hidden md:block">
                            <span>${sessionScope.userName}</span>
                            <span class="mx-2">|</span>
                            <span class="bg-blue-700 px-2 py-1 rounded text-xs">${sessionScope.userRole}</span>
                        </div>
                        <!-- Notification Icon -->
                        <div class="relative">
                            <button id="bell-icon" class="text-xl focus:outline-none">
                                <i class="fas fa-bell"></i>
                            </button>
                            <div id="notificationDropdown" class="hidden absolute right-0 mt-2 w-80 bg-white text-black border rounded shadow-lg z-50">
                                <ul id="notification-list" class="max-h-80 overflow-y-auto divide-y">
                                    <li class="px-4 py-2 text-center text-gray-400">Loading...</li>
                                </ul>
                            </div>
                        </div>
                        <!-- User Avatar Dropdown -->
                        <div class="relative ml-4">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${sessionScope.userImage}"
                                     alt="Avatar" class="w-10 h-10 rounded-full border border-white shadow">
                            </button><div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-md py-1 z-50 hidden">
                                <a href="${pageContext.request.contextPath}/user-detail?id=${sessionScope.userId}" class="block px-4 py-2 text-black hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp" class="block px-4 py-2 text-black hover:bg-gray-100">Change password</a>
                                <a href="${pageContext.request.contextPath}/logout.jsp" class="block px-4 py-2 text-black hover:bg-gray-100">Logout</a>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Main layout -->
            <div class="flex flex-1">
                <!-- Sidebar -->
                <aside class="bg-blue-800 text-white w-64 p-4 hidden md:block">
                    <nav>
                        <ul>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/dashboard" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-tachometer-alt mr-2"></i> Dashboard
                                </a>
                            </li>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/materiallist" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-boxes mr-2"></i> Inventory
                                </a>
                            </li>



                            <c:if test="${sessionScope.userRole == 'Warehouse Staff'}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/approvedrequests" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Request List
                                    </a>
                                </li>
                            </c:if>


                            <c:if test="${sessionScope.userRole == 'Warehouse Manager'|| sessionScope.userRole == 'Director'}">

                                <li class="mb-1">
                                    <a href="#" onclick="toggleSubmenu('reqSubmenu')" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Request
                                        <i class="fas fa-chevron-down float-right" id="taskChevron"></i>
                                    </a>

                                    <ul id="reqSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/reqlist" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-user-plus mr-2"></i> Request List
                                            </a>
                                        </li>
                                        <li class="mb-2">
                                            <a href="${pageContext.request.contextPath}/purchase-request-list" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-shopping-cart mr-2"></i> Purchase Requests
                                            </a>
                                        </li>

                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/completed-tasks" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-check-circle mr-2"></i> My Request
                                            </a>
                                        </li>


                                    </ul>
                                </li>


                                <li class="mb-1">
                                    <a href="#" onclick="toggleSubmenu('taskSubmenu')" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Task
                                        <i class="fas fa-chevron-down float-right" id="taskChevron"></i>
                                    </a>

                                    <ul id="taskSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/tasklist" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-user-plus mr-2"></i> Task List
                                            </a>
                                        </li>
                                        
                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/completed-tasks" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-check-circle mr-2"></i> Manage Task
                                            </a>
                                        </li>

                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/completed-tasks" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-check-circle mr-2"></i> Completed Task
                                            </a>
                                        </li>


                                    </ul>
                                </li>


                            </c:if>
                            <c:if test="${sessionScope.userRole == 'Warehouse Manager'}">
                                <li class="mb-2">
                                    <a href="#" onclick="toggleSubmenu('userManagerSubmenu')" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-users mr-2"></i> User Manager
                                        <i class="fas fa-chevron-down ml-auto" id="userManagerChevron"></i>
                                    </a>
                                    <ul id="userManagerSubmenu" class="ml-4 mt-2 hidden">
                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/userlist" class="block px-3 py-2 rounded hover:bg-gray-600">User List</a>
                                        </li>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/user-matrix" class="block px-3 py-2 rounded hover:bg-gray-600">Authorization</a>
                                        </li>
                                    </ul>
                                </li>
                            </c:if>
                            <c:if test="${sessionScope.userRole == 'Company Staff'}">
                                <li class="mb-2">
                                    <a href="${pageContext.request.contextPath}/my-request" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-envelope-open-text mr-2"></i> My Request
                                    </a>
                                </li>
                            </c:if>
                            <li>
                                <a href="advanced-dashboard" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-clipboard-list mr-2"></i> More
                                </a>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <!-- Main Content -->
                <main class="flex-1 bg-gray-100 p-6 overflow-auto">
                    <c:if test="${not empty pageContent}">
                        <jsp:include page="${pageContent}" />
                    </c:if>
                </main>
            </div>

            <!-- Mobile Sidebar -->
            <div class="md:hidden fixed bottom-4 right-4 z-50">
                <button id="mobileSidebarToggle" class="bg-blue-800 text-white p-3 rounded-full shadow-lg">
                    <i class="fas fa-bars"></i></button>
            </div>
            <div id="mobileSidebar" class="fixed inset-0 bg-gray-800 bg-opacity-75 z-40 hidden">
                <div class="bg-blue-800 text-white w-64 min-h-screen p-4">
                    <div class="flex justify-between items-center mb-6">
                        <h2 class="text-xl font-bold">Menu</h2>
                        <button id="closeMobileSidebar" class="text-white">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    <nav>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/dashboard" class="block px-4 py-2 hover:bg-gray-700">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/materiallist" class="block px-4 py-2 hover:bg-gray-700">Inventory</a></li>
                            <li><a href="${pageContext.request.contextPath}/user-matrix" class="block px-4 py-2 hover:bg-gray-700">Authorization</a></li>
                            <li><a href="${pageContext.request.contextPath}/requestlist" class="block px-4 py-2 hover:bg-gray-700">Request</a></li>
                            <li><a href="#" class="block px-4 py-2 hover:bg-gray-700">More</a></li>
                        </ul>
                    </nav>
                </div>
            </div>

            <!-- Scripts -->
            <script>
                function toggleSubmenu(id) {
                    const submenu = document.getElementById(id);
                    submenu.classList.toggle("hidden");

                    const chevron = submenu.previousElementSibling.querySelector("i.fas.fa-chevron-down");
                    if (chevron) {
                        chevron.classList.toggle("rotate-180");
                    }
                }

                document.getElementById("mobileSidebarToggle")?.addEventListener("click", () => {
                    document.getElementById("mobileSidebar").classList.remove("hidden");
                });

                document.getElementById("closeMobileSidebar")?.addEventListener("click", () => {
                    document.getElementById("mobileSidebar").classList.add("hidden");
                });

                document.getElementById("userMenuBtn")?.addEventListener("click", () => {
                    document.getElementById("userMenu").classList.toggle("hidden");
                });
            </script>
        </div>
    </body>
</html>