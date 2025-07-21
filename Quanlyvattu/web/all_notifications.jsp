<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">Tất cả thông báo</h2>
    <ul class="divide-y">
        <c:forEach var="n" items="${notificationList}">
            <li class="py-3 px-2 hover:bg-gray-50 flex justify-between items-start">
                <div>
                    <a href="${n.url}" class="text-black font-medium">${n.title}</a>
                    <p class="text-gray-700 text-sm">${n.message}</p>
                    <p class="text-xs text-gray-500">
                        <c:choose>
                            <c:when test="${not empty n.createdAt}">
                                ${n.createdAt}
                            </c:when>
                            <c:otherwise>Không rõ thời gian</c:otherwise>
                        </c:choose>
                    </p>
                </div>
                <c:if test="${!n.isRead}">
                    <span class="mt-2 w-2 h-2 bg-blue-600 rounded-full block"></span>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</div>
