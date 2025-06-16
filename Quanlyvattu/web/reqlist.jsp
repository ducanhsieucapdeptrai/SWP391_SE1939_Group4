<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Request List</title>
    
</head>
<body>

<h2>Request List</h2>

<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Request Date</th>
            <th>Note</th>
            <th>Type</th>
            <th>Status</th>
            <th>Requested By</th>
            <th>Approved By</th>
            <th>Approved Date</th>
            <th>Approval Note</th>
            <th>Action</th> <!-- ✅ Cột mới -->
        </tr>
    </thead>
    <tbody>
        <c:forEach var="request" items="${requestList}">
            <tr>
                <td>${request.id}</td>
                <td>${request.requestDate}</td>
                <td>${request.note}</td>
                <td>${request.typeName}</td>
                <td>${request.status}</td>
                <td>${request.requestedBy}</td>
                <td>${request.approvedBy}</td>
                <td>${request.approvedDate}</td>
                <td>${request.approvalNote}</td>
                <td>
                    <!-- ✅ Link Detail -->
                    <a href="requestdetail.jsp?id=${request.id}" class="detail-link">Detail</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
