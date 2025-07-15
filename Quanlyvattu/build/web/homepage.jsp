<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
    /*
        ================================================================
        THE MAGIC: Replicate Google Calendar's Week-Contained Events
        ================================================================
    */

    /*
     * TARGET 1: The end of the event segment that falls on a Saturday.
     * We select the event harness inside any cell with the 'fc-day-sat' class.
    */
    .fc-day-sat .fc-daygrid-event-harness {
        /*
         * Force the right corners to be rounded, making it look like the event ends here.
         * The !important is needed to override FullCalendar's default inline styles.
        */
        border-top-right-radius: 6px !important;
        border-bottom-right-radius: 6px !important;
        /* Add a small gap to create a visual break before Sunday */
        margin-right: 2.5px !important;
    }

    /*
     * TARGET 2: The start of the event segment that falls on a Sunday.
     * We select the event harness inside any cell with the 'fc-day-sun' class.
    */
    .fc-day-sun .fc-daygrid-event-harness {
        /*
         * Force the left corners to be rounded, making it look like a new event is starting.
        */
        border-top-left-radius: 6px !important;
        border-bottom-left-radius: 6px !important;
    }

    /*
     * TARGET 3 (Cleanup): The main event element itself.
     * We remove its default border-radius so our harness styles can take over.
    */
    .fc-daygrid-event {
        border-radius: 0 !important;
    }

    /* Optional: Add a subtle box-shadow for a more modern, lifted feel like Google's */
    .fc-daygrid-event-harness {
        box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.08);
    }
</style>

<div class="container mx-auto">
    <h2 class="text-2xl font-semibold mb-6">Dashboard</h2>

    <!-- Ví dụ: Biểu đồ -->
    <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <h3 class="text-lg font-semibold mb-2">Material Flow Chart</h3>
        
        <!-- Statistics Dashboard -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-6">
            <!-- Total Material -->
            <div class="bg-blue-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Material</p>
                    <p class="text-xl font-bold">${totalMaterials}</p>
                </div>
            </div>
            
            <!-- Total Import -->
            <div class="bg-green-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-green-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4h13M3 8h9m-9 4h6m4 0l4-4m0 0l4 4m-4-4v12" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Import</p>
                    <p class="text-xl font-bold">${totalImports}</p>
                </div>
            </div>
            
            <!-- Total Export -->
            <div class="bg-indigo-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4h13M3 8h9m-9 4h9m5-4v12m0 0l-4-4m4 4l4-4" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Total Export</p>
                    <p class="text-xl font-bold">${totalExports}</p>
                </div>
            </div>
            
            <!-- Pending Requests -->
            <div class="bg-yellow-50 p-4 rounded-lg shadow flex items-center">
                <div class="mr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-yellow-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <div>
                    <p class="text-sm text-gray-600">Pending Requests</p>
                    <p class="text-xl font-bold">${pendingRequests}</p>
                </div>
            </div>
        </div>
        
        <canvas id="materialChart" width="400" height="200"></canvas>
    </div>

    <!-- Advanced Dashboard Button -->
    <div class="text-center mb-6">
        <a href="${pageContext.request.contextPath}/advanced-dashboard" class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-6 rounded-lg inline-flex items-center transition duration-300">
            <i class="fas fa-chart-line mr-2"></i> More Advanced Features
        </a>
    </div>

    <!-- Recent Material Entries -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <!-- FullCalendar Integration -->
        <link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css' rel='stylesheet' />
        <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>

        <!-- Recent Imports -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4 text-blue-600">Recent Material Imports</h3>
            <c:choose>
                <c:when test="${not empty recentImports}">
                    <table class="w-full">
                        <thead>
                            <tr class="bg-gray-100 text-left">
                                <th class="p-2">Material</th>
                                <th class="p-2">Quantity</th>
                                <th class="p-2">Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${recentImports}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="p-2">
                                        <div class="font-medium">${material.materialName}</div>
                                        <div class="text-sm text-gray-500">${material.categoryName} / ${material.subCategoryName}</div>
                                    </td>
                                    <td class="p-2 font-medium">${material.importQuantity}</td>
                                    <td class="p-2">
                                        <div class="font-medium">
                                            <fmt:formatDate value="${material.importDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <div class="text-sm text-gray-500">
                                            <fmt:formatDate value="${material.importDate}" pattern="HH:mm"/>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-center">No recent imports found</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <!-- Recent Exports -->
        <div class="bg-white rounded-lg shadow-md p-6">
            <h3 class="text-lg font-semibold mb-4 text-red-600">Recent Material Exports</h3>
            <c:choose>
                <c:when test="${not empty recentExports}">
                    <table class="w-full">
                        <thead>
                            <tr class="bg-gray-100 text-left">
                                <th class="p-2">Material</th>
                                <th class="p-2">Quantity</th>
                                <th class="p-2">Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${recentExports}" varStatus="status">
                                <tr class="${status.index % 2 == 0 ? 'bg-white' : 'bg-gray-50'}">
                                    <td class="p-2">
                                        <div class="font-medium">${material.materialName}</div>
                                        <div class="text-sm text-gray-500">${material.categoryName} / ${material.subCategoryName}</div>
                                    </td>
                                    <td class="p-2 font-medium">${material.exportQuantity}</td>
                                    <td class="p-2">
                                        <div class="font-medium">
                                            <fmt:formatDate value="${material.exportDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <div class="text-sm text-gray-500">
                                            <fmt:formatDate value="${material.exportDate}" pattern="HH:mm"/>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <p class="text-gray-500 text-center">No recent exports found</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Calendar Section -->
    <div class="bg-white rounded-lg shadow-md p-6 mt-8">
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold text-gray-700">Your Calendar</h2>
            <button onclick="openAddEventModal()" class="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700">Add Event</button>
        </div>
        <div id="calendar"></div>
    </div>

    <!-- Add Event Modal -->
    <div id="addEventModal" class="fixed z-10 inset-0 overflow-y-auto hidden">
        <div class="flex items-center justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
            <div class="fixed inset-0 transition-opacity" aria-hidden="true">
                <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
            </div>
            <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
            <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                <form action="event-action" method="POST" class="mt-4">
                    <input type="hidden" name="action" value="add">
                    <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                        <h3 class="text-lg leading-6 font-medium text-gray-900">Add New Event</h3>
                        <div class="mt-4">
                            <label for="eventType" class="block text-sm font-medium text-gray-700">Event Type</label>
                            <select id="eventType" name="eventType" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
                                <option>Appointment</option>
                                <option>Work</option>
                                <option>Business Trip</option>
                                <option>Leave Request</option>
                            </select>
                        </div>
                        <div class="mt-4">
                            <label for="title" class="block text-sm font-medium text-gray-700">Title</label>
                            <input type="text" name="title" id="title" class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" required>
                        </div>
                        <div class="mt-4">
                            <label for="description" class="block text-sm font-medium text-gray-700">Description</label>
                            <textarea id="description" name="description" rows="3" class="mt-1 shadow-sm focus:ring-indigo-500 focus:border-indigo-500 block w-full sm:text-sm border border-gray-300 rounded-md"></textarea>
                        </div>
                        <div class="mt-4">
                            <label for="startTime" class="block text-sm font-medium text-gray-700">Start Time</label>
                            <input type="datetime-local" name="startTime" id="startTime" class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" required>
                        </div>
                        <div class="mt-4">
                            <label for="endTime" class="block text-sm font-medium text-gray-700">End Time</label>
                            <input type="datetime-local" name="endTime" id="endTime" class="mt-1 focus:ring-indigo-500 focus:border-indigo-500 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md">
                        </div>
                    </div>
                    <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                        <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-indigo-600 text-base font-medium text-white hover:bg-indigo-700 focus:outline-none sm:ml-3 sm:w-auto sm:text-sm">
                            Add Event
                        </button>
                        <button type="button" onclick="closeModal()" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm">
                            Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

<!-- Chart data preparation -->
<c:set var="chartLabelsJson" value="${chartLabels != null ? chartLabels : '[]'}" scope="request" />
<c:set var="chartDataJson" value="${chartData != null ? chartData : '[]'}" scope="request" />

<!-- Scripts for Calendar and Chart -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Calendar Script
    document.addEventListener('DOMContentLoaded', function() {
        var calendarEl = document.getElementById('calendar');
        if(calendarEl) {
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay'
                },
                events: 'calendar-data',
                dateClick: function(info) {
                    calendar.changeView('timeGridDay', info.dateStr);
                },
                eventContent: function(arg) {
                    let bullet = document.createElement('div');
                    bullet.classList.add('w-2', 'h-2', 'bg-blue-500', 'rounded-full', 'inline-block', 'mr-1');
                    let title = document.createElement('div');
                    title.classList.add('inline-block', 'truncate');
                    title.innerText = arg.event.title;
                    let container = document.createElement('div');
                    container.appendChild(bullet);
                    container.appendChild(title);
                    return { domNodes: [container] };
                },
                eventClick: function(info) {
                    alert('Event: ' + info.event.title + '\n' + 'Description: ' + info.event.extendedProps.description);
                    // Here you can open a more detailed modal for viewing/editing the event
                }
            });
            calendar.render();
        }
    });

    function openAddEventModal() {
        // Set current date and time as default for the modal
        const now = new Date();
        const year = now.getFullYear();
        const month = (now.getMonth() + 1).toString().padStart(2, '0');
        const day = now.getDate().toString().padStart(2, '0');
        const hours = now.getHours().toString().padStart(2, '0');
        const minutes = now.getMinutes().toString().padStart(2, '0');
        document.getElementById('startTime').value = `${year}-${month}-${day}T${hours}:${minutes}`;
        document.getElementById('addEventModal').classList.remove('hidden');
    }

    function closeModal() {
        document.getElementById('addEventModal').classList.add('hidden');
    }

    // Chart.js initialization
    document.addEventListener('DOMContentLoaded', function() {
        const ctx = document.getElementById('materialChart').getContext('2d');
        
        let chartLabelsData = [];
        let chartValuesData = [];
        
        try {
            chartLabelsData = JSON.parse('${chartLabelsJson}');
        } catch (e) {
            console.error('Error parsing chart labels:', e);
        }
        
        try {
            chartValuesData = JSON.parse('${chartDataJson}');
        } catch (e) {
            console.error('Error parsing chart data:', e);
        }
        
        const materialChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: chartLabelsData,
                datasets: [{
                    label: 'Materials',
                    data: chartValuesData,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)'
                }]
            },
           options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });
</script>
