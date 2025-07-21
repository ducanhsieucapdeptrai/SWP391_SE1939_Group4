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
                                <span id="notification-count"
                                      class="absolute top-0 right-0 bg-red-600 text-white rounded-full text-xs px-1 hidden">0</span>
                            </button>

                            <div id="notificationDropdown"
                                 class="hidden absolute right-0 mt-2 w-80 bg-white text-black border rounded shadow-lg z-50">
                                <ul id="notification-list" class="max-h-96 overflow-y-auto divide-y"></ul>

                                <ul class="divide-y">
                                    <li class="px-4 py-2 text-sm text-right">
                                        <button onclick="markAllNotificationsAsRead()"
                                                class="text-blue-600 hover:underline">Mark All As Read</button>
                                    </li>
                                    <li class="px-4 py-2 text-center">
                                        <a href="${pageContext.request.contextPath}/all-notifications"
                                           class="text-blue-600 hover:underline text-sm font-medium">Show all notifications</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <script>
                            document.getElementById('bell-icon').addEventListener('click', function () {
                                const dropdown = document.getElementById('notificationDropdown');
                                dropdown.classList.toggle('hidden');

                                const list = document.getElementById('notification-list');
                                list.innerHTML = '<li class="px-4 py-2 text-center text-gray-400">Đang tải...</li>';

                                fetch('get-notifications')
                                        .then(res => res.json())
                                        .then(data => {
                                            const countSpan = document.getElementById('notification-count');
                                            list.innerHTML = '';
                                            const unreadCount = data.filter(n => !n.isRead).length;
                                            countSpan.classList.toggle('hidden', unreadCount === 0);
                                            countSpan.textContent = unreadCount;

                                            if (data.length === 0) {
                                                list.innerHTML = '<li class="px-4 py-2 text-center text-gray-400">Không có thông báo</li>';
                                                return;
                                            }

                                            const now = new Date();
                                            const today = [], earlier = [];

                                            data.forEach(noti => {
                                                const created = new Date(noti.createdAt);
                                                const hoursDiff = (now - created) / 1000 / 3600;
                                                (hoursDiff <= 20 ? today : earlier).push(noti);
                                            });

                                            const formatTimeAgo = (ts) => {
                                                const created = new Date(Date.parse(ts));
                                                const diff = (new Date() - created) / 1000;
                                                if (diff < 60)
                                                    return "Vừa xong";
                                                if (diff < 3600)
                                                    return Math.floor(diff / 60) + " phút trước";
                                                if (diff < 86400)
                                                    return Math.floor(diff / 3600) + " giờ trước";
                                                return Math.floor(diff / 86400) + " ngày trước";
                                            };

                                            const renderGroup = (title, items) => {
                                                if (items.length === 0)
                                                    return '';
                                                let html = `<li class="px-4 py-2 font-semibold text-gray-600 bg-gray-100">${title}</li>`;
                                                items.forEach(n => {
                                                    html += `
        <li class="px-4 py-2 hover:bg-gray-100 text-sm cursor-pointer flex items-start">
            <a href="${n.url}" class="block flex-1"
               onclick="markAsRead(${n.notificationId}, '${n.url}'); return false;">
                <div class="font-semibold text-black">\${n.title || 'Thông báo'}</div>
                <div class="text-gray-700">\${n.message}</div>
                <div class="text-xs text-gray-500">\${formatTimeAgo(n.createdAt)}</div>
            </a>
            \${!n.isRead ? '<span class="mt-2 w-2 h-2 bg-blue-600 rounded-full flex-shrink-0 ml-2"></span>' : ''}
        </li>`;
                                                });
                                                return html;
                                            };


                                            list.innerHTML =
                                                    renderGroup("Hôm nay", today) +
                                                    renderGroup("Trước đó", earlier);
                                        })
                                        .catch(() => {
                                            list.innerHTML = '<li class="px-4 py-2 text-center text-red-500">Lỗi tải thông báo</li>';
                                        });
                            });

                            function markAsRead(id, url) {
                                fetch('mark-notification-read', {
                                    method: 'POST',
                                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                                    body: 'id=' + encodeURIComponent(id)
                                }).then(() => {
                                    window.location.href = url;
                                }).catch(() => {
                                    window.location.href = url;
                                });
                            }

                            function markAllNotificationsAsRead() {
                                fetch('mark-all-notifications-read', {method: 'POST'})
                                        .then(res => {
                                            if (res.ok) {
                                                document.getElementById('notification-count')?.classList.add('hidden');
                                                // Reload bell dropdown content (optional)
                                                document.getElementById('bell-icon').click(); // Close
                                                document.getElementById('bell-icon').click(); // Reopen
                                            } else {
                                                console.error('Không thể mark all as read:', res.status);
                                            }
                                        }).catch(err => {
                                    console.error('Lỗi khi gọi mark-all-notifications-read:', err);
                                });
                            }

                            // On load, hide bell count if 0
                            fetch('${pageContext.request.contextPath}/get-notifications')
                                    .then(res => res.json())
                                    .then(data => {
                                        const countSpan = document.getElementById('notification-count');
                                        const unreadCount = data.filter(n => !n.isRead).length;
                                        countSpan.classList.toggle('hidden', unreadCount === 0);
                                        countSpan.textContent = unreadCount;
                                    });
                        </script>



                        <!-- User Menu -->
                        <div class="relative ml-4">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <img src="${pageContext.request.contextPath}/assets/images/UserImage/${sessionScope.userImage}" alt="Avatar" class="w-10 h-10 rounded-full border border-white shadow">
                            </button>
                            <div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-md py-1 z-50 hidden">
                                <a href="${pageContext.request.contextPath}/user-detail?id=${sessionScope.userId}" class="block px-4 py-2 text-black hover:bg-gray-100">Profile</a>
                                <a href="${pageContext.request.contextPath}/change_password.jsp" class="block px-4 py-2 text-black hover:bg-gray-100">Change password</a>
                                <a href="${pageContext.request.contextPath}/logout.jsp" class="block px-4 py-2 text-black hover:bg-gray-100">Logout</a>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <!-- Layout -->
            <div class="flex flex-1">
                <!-- Sidebar -->
                <aside class="bg-blue-800 text-white w-64 p-4 hidden md:block">
                    <nav>
                        <ul>
                            <li class="mb-2"><a href="${pageContext.request.contextPath}/dashboard" class="flex items-center px-4 py-2 rounded hover:bg-gray-700"><i class="fas fa-tachometer-alt mr-2"></i> Dashboard</a></li>
                            <li class="mb-2"><a href="${pageContext.request.contextPath}/materiallist" class="flex items-center px-4 py-2 rounded hover:bg-gray-700"><i class="fas fa-boxes mr-2"></i> Inventory</a></li>

                            <c:if test="${sessionScope.userRole == 'Warehouse Staff'}">
                                <li class="mb-1">
                                    <a href="${pageContext.request.contextPath}/tasklist" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Task List
                                    </a>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.userRole == 'Warehouse Manager' || sessionScope.userRole == 'Director'}">
                                <li class="mb-1">
                                    <a href="#" onclick="toggleSubmenu('reqSubmenu')" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Request
                                        <i class="fas fa-chevron-down float-right"></i>
                                    </a>
                                    <ul id="reqSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/reqlist" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-user-plus mr-2"></i> All Request</a></li>
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/purchase-request-list" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-shopping-cart mr-2"></i> Purchase Order Requests</a></li>
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/repair-request-list" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-shopping-cart mr-2"></i> Repair Order Requests</a></li>
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/my-request" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-check-circle mr-2"></i> My Request</a></li>
                                    </ul>
                                </li>

                                <li class="mb-1">
                                    <a href="#" onclick="toggleSubmenu('taskSubmenu')" class="block px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-tasks mr-2"></i> Task
                                        <i class="fas fa-chevron-down float-right"></i>
                                    </a>
                                    <ul id="taskSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/tasklist" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-user-plus mr-2"></i> Task List</a></li>
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/completedTasks" class="block px-3 py-2 rounded hover:bg-gray-600 text-gray-300 hover:text-white"><i class="fas fa-check-circle mr-2"></i> Completed Task</a></li>
                                    </ul>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.userRole == 'Warehouse Manager'}">
                                <li class="mb-2">
                                    <a href="#" onclick="toggleSubmenu('userManagerSubmenu')" class="flex items-center px-4 py-2 rounded hover:bg-gray-700">
                                        <i class="fas fa-users mr-2"></i> User Manager
                                        <i class="fas fa-chevron-down ml-auto"></i>
                                    </a>
                                    <ul id="userManagerSubmenu" class="ml-4 mt-2 hidden">
                                        <li class="mb-1"><a href="${pageContext.request.contextPath}/userlist" class="block px-3 py-2 rounded hover:bg-gray-600">User List</a></li>
                                        <li><a href="${pageContext.request.contextPath}/reset-pass-list" class="block px-3 py-2 rounded hover:bg-gray-600">Requests Reset Password</a></li>
                                        <li><a href="${pageContext.request.contextPath}/user-matrix" class="block px-3 py-2 rounded hover:bg-gray-600">Authorization</a></li>
                                    </ul>
                                </li>
                            </c:if>

                            <c:if test="${sessionScope.userRole == 'Company Staff'}">
                                <li class="mb-2"><a href="${pageContext.request.contextPath}/my-request" class="flex items-center px-4 py-2 rounded hover:bg-gray-700"><i class="fas fa-envelope-open-text mr-2"></i> My Request</a></li>
                                </c:if>

                            <li><a href="advanced-dashboard" class="flex items-center px-4 py-2 rounded hover:bg-gray-700"><i class="fas fa-clipboard-list mr-2"></i> More</a></li>
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
        </div>
        <script>
            function toggleSubmenu(id) {
                const submenu = document.getElementById(id);
                submenu.classList.toggle("hidden");

                const chevron = event.currentTarget.querySelector("i.fas.fa-chevron-down");
                if (chevron) {
                    chevron.classList.toggle("rotate-180");
                }
            }

            document.getElementById("userMenuBtn")?.addEventListener("click", () => {
                document.getElementById("userMenu")?.classList.toggle("hidden");
            });
        </script>
    </body>
</html>
