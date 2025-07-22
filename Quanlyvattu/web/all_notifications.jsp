<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">Tất cả thông báo</h2>

    <c:if test="${empty notificationList}">
        <p class="text-gray-500">Hiện không có thông báo nào.</p>
    </c:if>

    <ul class="divide-y" style="max-height: 500px; overflow-y: auto;">
        <c:forEach var="n" items="${notificationList}">
            <li class="py-3 px-2 hover:bg-gray-50 flex justify-between items-start gap-4">
                <div class="flex-1">
                    <a href="${n.url}" class="${n.isRead ? 'text-gray-800' : 'text-black font-semibold'} block">
                        ${n.title}
                    </a>
                    <p class="text-gray-700 text-sm line-clamp-2">${n.message}</p>
                    <p class="text-xs text-gray-500 mt-1">
                        <c:choose>
                            <c:when test="${not empty n.createdAt}">
                                ${n.createdAt}
                            </c:when>
                            <c:otherwise>Không rõ thời gian</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <c:if test="${!n.isRead}">
                    <span title="Chưa đọc" class="mt-2 w-2 h-2 bg-blue-600 rounded-full block shrink-0"></span>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>
