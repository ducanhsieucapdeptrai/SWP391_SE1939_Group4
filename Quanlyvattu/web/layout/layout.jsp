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
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div id="app" class="min-h-screen flex flex-col">
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
                                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${sessionScope.userImage}"
                                     alt="Avatar"
                                     class="w-16 aspect-square rounded-full object-cover shadow-md border border-white" />

                            </button>

                            <div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden">
                                <a href="${pageContext.request.contextPath}/user-detail?id=${sessionScope.userId}" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Change password</a>
                                <a href="${pageContext.request.contextPath}/logout.jsp" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Logout</a>
                            </div>

                        </div>
                    </div>
                </div>
            </header>
            <div class="flex flex-1">
                <aside class="bg-blue-800 text-white w-64 min-h-screen p-4 hidden md:block">
                    <nav>
                        <ul>
                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/dashboard" class="block px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-tachometer-alt mr-2"></i> Dashboard
                                </a>
                            </li>

                            <li class="mb-1">
                                <a href="${pageContext.request.contextPath}/materiallist" class="block px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-boxes mr-2"></i> Inventory
                                </a>
                            </li>

                          

                            <c:if test="${userRole == 'Warehouse Staff'}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/approvedrequests" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Request List
                                    </a>
                                </li>
                            </c:if>


                            <c:if test="${userRole == 'Warehouse Manager'}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/reqlist" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-file-import mr-2"></i> Request
                                    </a>
                                </li>

                                <li class="mb-1">
                                    <a href="#" onclick="toggleSubmenu('userManagerSubmenu')" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-users mr-2"></i> User Manager
                                        <i class="fas fa-chevron-down float-right" id="userManagerChevron"></i>
                                    </a>
                                    <ul id="userManagerSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/userlist" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-list mr-2"></i> User List
                                            </a>
                                        </li>
                                        <li class="mb-1">
                                            <a href="${pageContext.request.contextPath}/user-matrix" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white">
                                                <i class="fas fa-key mr-2"></i> Authorization
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:if>

                            <li class="mb-1">
                                <a href="advanced-dashboard" class="block px-4 py-2 rounded hover:bg-gray-700">
                                    <i class="fas fa-clipboard-list mr-2"></i> More
                                </a>
                            </li>
                        </ul>
                    </nav>
                </aside>

                <main class="flex-1 bg-gray-100 p-6 overflow-auto">
                    <%-- Nếu dùng JSTL --%>
                    <c:if test="${not empty pageContent}">
                        <jsp:include page="${pageContent}" />
                    </c:if>




                </main>
            </div>
            <div class="md:hidden fixed bottom-4 right-4 z-50">
                <button id="mobileSidebarToggle" class="bg-blue-800 text-white p-3 rounded-full shadow-lg">
                    <i class="fas fa-bars"></i>
                </button>
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
                            <li><a href="${pageContext.request.contextPath}/homepage.jsp" class="block px-4 py-2 hover:bg-gray-700">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/materiallist.jsp" class="block px-4 py-2 hover:bg-gray-700">Inventory</a></li>
                            <li><a href="${pageContext.request.contextPath}/user-matrix.jsp" class="block px-4 py-2 hover:bg-gray-700">Authorization</a></li>
                            <li><a href="${pageContext.request.contextPath}/requestlist.jsp" class="block px-4 py-2 hover:bg-gray-700">Request</a></li>
                            <li><a href="#" class="block px-4 py-2 hover:bg-gray-700">More</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <script>
                function toggleSubmenu(id) {
                    const submenu = document.getElementById(id);
                    const chevron = document.getElementById("userManagerChevron");
                    submenu.classList.toggle("hidden");
                    chevron.classList.toggle("rotate-180");
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
            <!-- Popup hiển thị ảnh lớn -->
            <div id="image-popup" class="hidden fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50">
                <img id="popup-img" class="max-w-full max-h-full rounded-lg shadow-lg" />
            </div>
            <script>
                function showImage(src) {
                    const popup = document.getElementById("image-popup");
                    const img = document.getElementById("popup-img");
                    img.src = src;
                    popup.classList.remove("hidden");
                }

                function closeImage() {
                    document.getElementById("image-popup").classList.add("hidden");
                }

                // Đóng popup khi click ra ngoài ảnh
                document.addEventListener("DOMContentLoaded", function () {
                    const popup = document.getElementById("image-popup");
                    if (popup) {
                        popup.addEventListener("click", function (e) {
                            if (e.target.id === "image-popup") {
                                closeImage();
                            }
                        });
                    }
                });
            </script>

    </body>
</html>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">

