<%-- 
    Document   : UserEditRole_Status
    Created on : May 30, 2025, 11:39:00 PM
    Author     : Admin
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Edit User</title>
  
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">


</head>
<body class="bg-light py-5">
  <div class="container">
    <div class="card shadow-sm">
      <div class="card-header">
        <h3 class="mb-0">Edit User</h3>
      </div>
      <div class="card-body">
        <form action="user-edit" method="post">
          <input type="hidden" name="userId" value="${user.userId}">
          
          <div class="mb-3 row">
            <label class="col-sm-3 col-form-label">Full Name:</label>
            <div class="col-sm-9">
              <input type="text" readonly class="form-control-plaintext" value="${user.fullName}">
            </div>
          </div>
          
          <div class="mb-3 row">
            <label class="col-sm-3 col-form-label">Email:</label>
            <div class="col-sm-9">
              <input type="email" readonly class="form-control-plaintext" value="${user.email}">
            </div>
          </div>
          
          <div class="mb-3 row">
            <label class="col-sm-3 col-form-label">Phone:</label>
            <div class="col-sm-9">
              <input type="text" readonly class="form-control-plaintext" value="${user.phone}">
            </div>
          </div>
          
          <div class="mb-3 row">
            <label for="roleId" class="col-sm-3 col-form-label">Role:</label>
            <div class="col-sm-9">
              <select id="roleId" name="roleId" class="form-select">
                <c:forEach var="role" items="${roles}">
                  <option value="${role.roleId}"
                    <c:if test="${role.roleId == user.roleId}">selected</c:if>>
                    ${role.roleName}
                  </option>
                </c:forEach>
              </select>
            </div>
          </div>
          
          <div class="mb-4 row">
            <label for="isActive" class="col-sm-3 col-form-label">Status:</label>
            <div class="col-sm-9">
              <select id="isActive" name="isActive" class="form-select">
                <option value="true"  ${user.isActive ? 'selected' : ''}>Active</option>
                <option value="false" ${!user.isActive ? 'selected' : ''}>Inactive</option>
              </select>
            </div>
          </div>
          
          <div class="d-flex justify-content-end">
            <a href="user-detail?id=${user.userId}" class="btn btn-outline-secondary me-2">
               Back to UserDetail
            </a>
            <button type="submit" class="btn btn-primary">
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>

  
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
