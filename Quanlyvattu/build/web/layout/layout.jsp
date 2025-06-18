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
                        <!-- Biểu tượng chuông -->
                        <div class="relative">
                            <button id="bell-icon" class="text-white text-xl focus:outline-none">
                                <i class="fas fa-bell text-white text-xl"></i>
                            </button>

                            <!-- Dropdown thông báo -->
                            <div id="notificationDropdown" class="hidden absolute right-0 mt-2 w-80 bg-gray-900 border border-gray-700 rounded-lg shadow-lg z-50">
                                <ul id="notification-list" class="max-h-80 overflow-y-auto text-white divide-y divide-gray-700">
                                    <!-- Dữ liệu sẽ được đổ vào đây -->
                                    <li class="px-4 py-2 text-center text-gray-400">Loading...</li>
                                </ul>
                            </div>
                        </div>

                        <!-- Script fetch thông báo -->
                        <script>
                            const bellIcon = document.getElementById("bell-icon");
                            const dropdown = document.getElementById("notificationDropdown");
                            const list = document.getElementById("notification-list");

                            bellIcon.addEventListener("click", async () => {
                                // Toggle dropdown
                                dropdown.classList.toggle("hidden");

                                if (!dropdown.classList.contains("hidden")) {
                                    // Gọi API lấy danh sách thông báo
                                    try {
                                        const res = await fetch('/notifications');
                                        const data = await res.json();

                                        list.innerHTML = "";

                                        if (data.length === 0) {
                                            const emptyItem = document.createElement("li");
                                            emptyItem.className = "px-4 py-2 text-gray-400";
                                            emptyItem.textContent = "No ";
                                            list.appendChild(emptyItem);
                                        } else {
                                            data.forEach(n => {
                                                const li = document.createElement("li");
                                                li.className = "px-4 py-2 hover:bg-gray-700 cursor-pointer";
                                                li.textContent = n.message;
                                                list.appendChild(li);
                                            });
                                        }

                                    } catch (error) {
                                        console.error("Lỗi lấy thông báo:", error);
                                        list.innerHTML = '<li class="px-4 py-2 text-red-400">Lỗi tải thông báo</li>';
                                    }
                                }
                            });

                            // Tắt dropdown khi click ra ngoài
                            window.addEventListener('click', function (e) {
                                if (!bellIcon.contains(e.target) && !dropdown.contains(e.target)) {
                                    dropdown.classList.add("hidden");
                                }
                            });
                        </script>


                        <div class="relative">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${sessionScope.currentUser.userImage}"
                                     class="w-16 aspect-square rounded-full object-cover shadow-md border border-white" />
                            </button>

                            <div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden">
                                <a href="${pageContext.request.contextPath}/user-detail?id=${sessionScope.currentUser.userId}" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Change password</a>
                                <a href="${pageContext.request.contextPath}/logout.jsp" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Logout</a>
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

                            <c:if test="${sessionScope.currentUser.role.roleName == 'Director'}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/pending-po" class="block px-4 py-2 hover:bg-gray-700">
                                        <i class="fas fa-clipboard-check mr-2"></i>List Purchase Order
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

        <!-- Scripts -->
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

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <c:if test="${not empty param.status}">
            <script>
            document.addEventListener("DOMContentLoaded", function () {
                <c:choose>
                    <c:when test="${param.status == 'approve'}">
                Swal.fire({
                    icon: 'success',
                    title: 'Duyệt thành công!',
                    text: 'Yêu cầu đã được duyệt.',
                    timer: 2000,
                    showConfirmButton: false
                });
                    </c:when>
                    <c:when test="${param.status == 'reject'}">
                Swal.fire({
                    icon: 'success',
                    title: 'Từ chối thành công!',
                    text: 'Yêu cầu đã bị từ chối.',
                    timer: 2000,
                    showConfirmButton: false
                });
                    </c:when>
                </c:choose>
            });
            </script>
        </c:if>

    </body>
</html>
