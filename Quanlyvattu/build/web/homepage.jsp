<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Inventory Manangement Sollutionn</title>

        <title>Construction materials warehouse management system</title>

        <script src="https://cdn.tailwindcss.com"></script>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
        <!-- Link CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        
        
    </head>
    <body>
        <div id="app" class="min-h-screen flex flex-col">
            <!-- Header -->
            <header class="bg-blue-900 text-white shadow-md">
                <div class="container mx-auto px-4 py-3 flex justify-between items-center">
                    <div class="flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                        </svg>
                        <h1 class="text-xl font-bold">Material Management</h1>
                    </div>
                    <div class="flex items-center">
                        <div class="mr-4 hidden md:block">
                            <span id="currentUserName">Duc Anh dtr</span>
                            <span class="mx-2">|</span>
                            <span id="currentUserRole" class="bg-blue-700 px-2 py-1 rounded text-xs">Warehouse Manager</span>
                        </div>
                        <div class="relative">
                            <button id="userMenuBtn" class="flex items-center focus:outline-none">
                                <div class="w-8 h-8 rounded-full bg-blue-700 flex items-center justify-center text-white font-bold mr-2">
                                    N
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                                </svg>
                            </button>
                            <div id="userMenu" class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 z-50 hidden">
                                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Profile</a>
                                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Change password</a>
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            <div class="flex flex-1">
                <!-- Sidebar -->
                <aside id="sidebar" class="bg-blue-800 text-white w-64 min-h-screen p-4 hidden md:block">
                    <nav>
                        <div class="mb-8">
                            <ul>
                                <li class="mb-1">
                                    <a href="#" class="sidebar-item active-sidebar-item block px-4 py-2 rounded text-white" data-page="dashboard">
                                        <i class="fas fa-tachometer-alt mr-2"></i> Dashboard
                                    </a>
                                </li>
                                <li class="mb-1">
                                    <a href="materiallist" class="sidebar-item block px-4 py-2 rounded text-white" data-page="inventory">
                                        <i class="fas fa-boxes mr-2"></i> Inventory
                                    </a>
                                </li>
                                <li class="mb-1">
                                    <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="request">
                                        <i class="fas fa-file-import mr-2"></i> Request
                                    </a>
                                </li>
                                <li class="mb-1">
                                    <!-- UserManager  -->
                                    <a href="#" class="sidebar-item block px-4 py-2 rounded text-white hover:bg-gray-700" onclick="toggleSubmenu('userManagerSubmenu')">
                                        <i class="fas fa-users mr-2"></i> UserManager
                                        <i class="fas fa-chevron-down float-right mt-1 transition-transform duration-200" id="userManagerChevron"></i>
                                    </a>

                                    <!-- Submenu -->
                                    <ul id="userManagerSubmenu" class="hidden ml-4 mt-1">
                                        <li class="mb-1">
                                            <a href="userlist" class="sidebar-item block px-3 py-2 rounded text-gray-300 hover:text-white hover:bg-gray-600" data-page="userlist">
                                                <i class="fas fa-list mr-2"></i> UserList
                                            </a>
                                        </li>
                                        <li class="mb-1">
                                            <a href="user-matrix" class="sidebar-item block px-3 py-2 rounded text-gray-300 hover:text-white hover:bg-gray-600" data-page="userauthorization">
                                                <i class="fas fa-key mr-2"></i> UserAuthorization
                                            </a>
                                        </li>
                                    </ul>
                                </li>

                                <script>
                                    function toggleSubmenu(submenuId) {
                                        const submenu = document.getElementById(submenuId);
                                        const chevron = document.getElementById('userManagerChevron');

                                        if (submenu.classList.contains('hidden')) {
                                            submenu.classList.remove('hidden');
                                            chevron.classList.add('rotate-180');
                                        } else {
                                            submenu.classList.add('hidden');
                                            chevron.classList.remove('rotate-180');
                                        }
                                    }
                                </script>
                                <li class="mb-1">
                                    <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="more">
                                        <i class="fas fa-clipboard-list mr-2"></i> More
                                    </a>
                                </li>

                            </ul>
                        </div>


                    </nav>
                </aside>

                <!-- Mobile sidebar toggle -->
                <div class="md:hidden fixed bottom-4 right-4 z-50">
                    <button id="mobileSidebarToggle" class="bg-blue-800 text-white p-3 rounded-full shadow-lg">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                    </button>
                </div>

                <!-- Mobile sidebar -->
                <div id="mobileSidebar" class="fixed inset-0 bg-gray-800 bg-opacity-75 z-40 hidden">
                    <div class="bg-blue-800 text-white w-64 min-h-screen p-4 transform transition-transform">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-xl font-bold">Menu</h2>
                            <button id="closeMobileSidebar" class="text-white">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                                </svg>
                            </button>
                        </div>
                        <nav>
                            <div class="mb-8">
                                <ul>
                                    <li class="mb-1">
                                        <a href="#" class="sidebar-item active-sidebar-item block px-4 py-2 rounded text-white" data-page="dashboard">
                                            <i class="fas fa-tachometer-alt mr-2"></i> Dashboard
                                        </a>
                                    </li>
                                    <li class="mb-1">
                                        <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="inventory">
                                            <i class="fas fa-boxes mr-2"></i> Inventory
                                        </a>
                                    </li>
                                    <li class="mb-1">
                                        <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="userauthorization">
                                            <i class="fas fa-file-import mr-2"></i> User Authorization
                                        </a>
                                    </li>

                                    <li class="mb-1">
                                        <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="requests">
                                            <i class="fas fa-clipboard-list mr-2"></i> Request
                                        </a>
                                    </li>
                                    <li class="mb-1">
                                        <a href="#" class="sidebar-item block px-4 py-2 rounded text-white" data-page="more">
                                            <i class="fas fa-chart-bar mr-2"></i> More
                                        </a>
                                    </li>
                                </ul>
                            </div>


                        </nav>
                    </div>
                </div>

                <!-- Main Content -->
                <main class="flex-1 p-4 overflow-auto">
                    <!-- Dashboard Page -->
                    <div id="dashboard-page" class="page active-page">
                        <div class="mb-6">
                            <h2 class="text-2xl font-bold text-gray-800">Dashboard</h2>
                            <p class="text-gray-600">Overview</p>
                        </div>

                        <!-- Stats Cards -->
                        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                            <div class="bg-white rounded-lg shadow-md p-6 card">
                                <div class="flex items-center">
                                    <div class="rounded-full bg-blue-100 p-3 mr-4">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                                        </svg>
                                    </div>
                                    <div>
                                        <p class="text-gray-500 text-sm">Total Material</p>
                                        <h3 class="text-2xl font-bold text-gray-800">999</h3>
                                    </div>
                                </div>
                            </div>

                            <div class="bg-white rounded-lg shadow-md p-6 card">
                                <div class="flex items-center">
                                    <div class="rounded-full bg-green-100 p-3 mr-4">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 11l5-5m0 0l5 5m-5-5v12" />
                                        </svg>
                                    </div>
                                    <div>
                                        <p class="text-gray-500 text-sm">Import stock this month</p>
                                        <h3 class="text-2xl font-bold text-gray-800">32</h3>
                                    </div>
                                </div>
                            </div>

                            <div class="bg-white rounded-lg shadow-md p-6 card">
                                <div class="flex items-center">
                                    <div class="rounded-full bg-red-100 p-3 mr-4">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 13l-5 5m0 0l-5-5m5 5V6" />
                                        </svg>
                                    </div>
                                    <div>
                                        <p class="text-gray-500 text-sm">Export stock this month</p>
                                        <h3 class="text-2xl font-bold text-gray-800">28</h3>
                                    </div>
                                </div>
                            </div>

                            <div class="bg-white rounded-lg shadow-md p-6 card">
                                <div class="flex items-center">
                                    <div class="rounded-full bg-yellow-100 p-3 mr-4">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-yellow-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                        </svg>
                                    </div>
                                    <div>
                                        <p class="text-gray-500 text-sm">Pending request</p>
                                        <h3 class="text-2xl font-bold text-gray-800">20</h3>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Charts Row -->
                        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                            <div class="bg-white rounded-lg shadow-md p-6">
                                <h3 class="text-lg font-semibold text-gray-800 mb-4">Stock Movement Chart</h3>
                                <div class="h-64 flex items-end justify-around">
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 120px;"></div>
                                        <span class="text-xs mt-2">T1</span>
                                    </div>
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 150px;"></div>
                                        <span class="text-xs mt-2">T2</span>
                                    </div>
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 100px;"></div>
                                        <span class="text-xs mt-2">T3</span>
                                    </div>
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 180px;"></div>
                                        <span class="text-xs mt-2">T4</span>
                                    </div>
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 140px;"></div>
                                        <span class="text-xs mt-2">T5</span>
                                    </div>
                                    <div class="flex flex-col items-center">
                                        <div class="w-12 bg-blue-500 rounded-t" style="height: 160px;"></div>
                                        <span class="text-xs mt-2">T6</span>
                                    </div>
                                </div>
                            </div>

                            <div class="bg-white rounded-lg shadow-md p-6">
                                <h3 class="text-lg font-semibold text-gray-800 mb-4"></h3>
                                <div class="overflow-x-auto">
                                    <table class="min-w-full">
                                        <thead>
                                            <tr class="bg-gray-100">
                                                <th class="py-2 px-4 text-left text-sm font-medium text-gray-600">Material ID</th>
                                                <th class="py-2 px-4 text-left text-sm font-medium text-gray-600">Name</th>
                                                <th class="py-2 px-4 text-left text-sm font-medium text-gray-600">Quantity</th>
                                                <th class="py-2 px-4 text-left text-sm font-medium text-gray-600">Minimum Order Quantity</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="table-row border-b">
                                                <td class="py-2 px-4 text-sm text-gray-800">VT001</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">Xi mang</td>
                                                <td class="py-2 px-4 text-sm text-red-600 font-medium">5 ton</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">10 ton</td>
                                            </tr>
                                            <tr class="table-row border-b">
                                                <td class="py-2 px-4 text-sm text-gray-800">VT023</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">Thep cuon </td>
                                                <td class="py-2 px-4 text-sm text-red-600 font-medium">200 kg</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">500 kg</td>
                                            </tr>
                                            <tr class="table-row border-b">
                                                <td class="py-2 px-4 text-sm text-gray-800">VT045</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">Gach ep tuong 30x60</td>
                                                <td class="py-2 px-4 text-sm text-red-600 font-medium">120 m2</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">200 m²</td>
                                            </tr>
                                            <tr class="table-row border-b">
                                                <td class="py-2 px-4 text-sm text-gray-800">VT102</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">San lot ngoi</td>
                                                <td class="py-2 px-4 text-sm text-red-600 font-medium">15 thung</td>
                                                <td class="py-2 px-4 text-sm text-gray-800">30 thung</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>