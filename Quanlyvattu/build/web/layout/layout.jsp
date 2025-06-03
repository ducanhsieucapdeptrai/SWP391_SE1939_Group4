<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Construction Materials Warehouse Management System</title>

        <!-- Tailwind & Fonts -->
        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

        <!-- Custom CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    </head>
    <body>
        <div id="app" class="min-h-screen flex flex-col">
            <!-- Header -->
            <header class="bg-blue-900 text-white shadow-md">
                <div class="container mx-auto px-4 py-3 flex justify-between items-center">
                    <div class="flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"/>
                        </svg>
                        <h1 class="text-xl font-bold">Material Management</h1>
                    </div>
                    <div class="flex items-center">
                        <div class="mr-4 hidden md:block">
                            <span id="currentUserName">${sessionScope.userName}</span>
                            <span class="mx-2">|</span>
                            <span id="currentUserRole"
                                  class="bg-blue-700 px-2 py-1 rounded text-xs">${sessionScope.userRole}</span>
                        </div>
                        <div class="relative">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <div class="w-8 h-8 rounded-full bg-blue-700 flex items-center justify-center text-white font-bold mr-2">
                                    ${fn:substring(sessionScope.userName, 0, 1)}
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24"
                                     stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                      d="M19 9l-7 7-7-7"/>
                                </svg>
                            </button>
                            <div id="userMenu"
                                 class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden">
                                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp"
   class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
   Change password
</a>

                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <div class="flex flex-1">
                <!-- Sidebar -->
                <aside id="sidebar" class="bg-blue-800 text-white w-64 min-h-screen p-4 hidden md:block">
                    <nav>
                        <ul>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/homepage.jsp" class="sidebar-item block px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-tachometer-alt mr-2"></i> Dashboard
                                </a>
                            </li>
                            <li class="mb-1">
                                <div class="sidebar-group">
    <button class="sidebar-item block px-4 py-2 rounded hover:bg-gray-700 w-full text-left"
            onclick="toggleSubmenu('inventory-submenu')">
        <i class="fas fa-boxes mr-2"></i> Inventory
    </button>
    <div id="inventory-submenu" class="ml-6 mt-1 hidden">
        <a href="${pageContext.request.contextPath}/materiallist" class="block px-4 py-2 text-sm text-white hover:bg-gray-600 rounded">Material List</a>
        
    </div>
</div>


                            </li>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/requestlist.jsp" class="sidebar-item block px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-file-import mr-2"></i> Request
                                </a>
                            </li>
                            <li class="mb-1">
                                <a href="#" class="sidebar-item block px-4 py-2 rounded hover:bg-gray-700"
                                   onclick="toggleSubmenu('userManagerSubmenu')">
                                    <i class="fas fa-users mr-2"></i> User Manager
                                    <i class="fas fa-chevron-down float-right mt-1" id="userManagerChevron"></i>
                                </a>
                                <ul id="userManagerSubmenu" class="hidden ml-4 mt-1">
                                    <li class="mb-1">
                                        <a href="${pageContext.request.contextPath}/UserList.jsp" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                            <i class="fas fa-list mr-2"></i> User List
                                        </a>
                                    </li>
                                    <li class="mb-1">
                                        <a href="${pageContext.request.contextPath}/user-matrix.jsp" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                            <i class="fas fa-key mr-2"></i> User Authorization
                                        </a>
                                    </li>
                                </ul>
                            </li>
                            <li class="mb-1">
                                <a href="#" class="sidebar-item block px-4 py-2 rounded hover:bg-gray-700">
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

            <!-- Mobile Sidebar Toggle -->
            <div class="md:hidden fixed bottom-4 right-4 z-50">
                <button id="mobileSidebarToggle" class="bg-blue-800 text-white p-3 rounded-full shadow-lg">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                         stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M4 6h16M4 12h16M4 18h16"/>
                    </svg>
                </button>
            </div>

            <!-- Mobile Sidebar -->
            <div id="mobileSidebar" class="fixed inset-0 bg-gray-800 bg-opacity-75 z-40 hidden">
                <div class="bg-blue-800 text-white w-64 min-h-screen p-4">
                    <div class="flex justify-between items-center mb-6">
                        <h2 class="text-xl font-bold">Menu</h2>
                        <button id="closeMobileSidebar" class="text-white">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                 viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M6 18L18 6M6 6l12 12"/>
                            </svg>
                        </button>
                    </div>
                    <nav>
                        <ul>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/homepage.jsp" class="block px-4 py-2 rounded hover:bg-gray-700">Dashboard</a>
                            </li>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/materiallist.jsp" class="block px-4 py-2 rounded hover:bg-gray-700">Inventory</a>
                            </li>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/user-matrix.jsp" class="block px-4 py-2 rounded hover:bg-gray-700">User Authorization</a>
                            </li>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/requestlist.jsp" class="block px-4 py-2 rounded hover:bg-gray-700">Request</a>
                            </li>
                            <li class="mb-1">
                                <a href="#" class="block px-4 py-2 rounded hover:bg-gray-700">More</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>

        <!-- Script -->
        <script>
            function toggleSubmenu(submenuId) {
                const submenu = document.getElementById(submenuId);
                const chevron = document.getElementById('userManagerChevron');
                submenu.classList.toggle('hidden');
                chevron.classList.toggle('rotate-180');
            }

            document.getElementById('mobileSidebarToggle')?.addEventListener('click', () => {
                document.getElementById('mobileSidebar').classList.remove('hidden');
            });

            document.getElementById('closeMobileSidebar')?.addEventListener('click', () => {
                document.getElementById('mobileSidebar').classList.add('hidden');
            });

            document.getElementById('userMenuBtn')?.addEventListener('click', () => {
                document.getElementById('userMenu').classList.toggle('hidden');
            });
        </script>
    </body>
</html>
<script>
    function toggleSubmenu(id) {
        const submenu = document.getElementById(id);
        submenu.classList.toggle("hidden");
    }
</script>

