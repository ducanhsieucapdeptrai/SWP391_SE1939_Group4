<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Material Request</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
  <div class="col-md-8 mx-auto">
    <div class="card">
      <div class="card-header bg-primary text-white">
        <h4>Create Material Request</h4>
      </div>
      <div class="card-body">

        <c:if test="${not empty error}">
          <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
          <div class="alert alert-success">${success}</div>
        </c:if>

        <form method="post" action="createrequest">
          <!-- Request Type -->
          <div class="mb-3">
            <label class="form-label">Request Type <span class="text-danger">*</span></label>
            <select name="requestTypeId" class="form-select" required>
              <option value="">-- Select a type --</option>
              <c:forEach var="type" items="${requestTypes}">
                <option value="${type.requestTypeId}">
                  ${type.requestTypeName}
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- Category & Sub-Category Filters -->
          <div class="row mb-3">
            <div class="col">
              <label class="form-label">Category</label>
              <select id="categoryFilter" class="form-select">
                <option value="">All Categories</option>
                <c:forEach var="cat" items="${categories}">
                  <option value="${cat.categoryId}">
                    ${cat.categoryName}
                  </option>
                </c:forEach>
              </select>
            </div>
            <div class="col">
              <label class="form-label">Sub‑Category</label>
              <select id="subCategoryFilter" class="form-select">
                <option value="">All Sub‑Categories</option>
                <c:forEach var="sc" items="${subCategories}">
                  <option value="${sc.subCategoryId}" data-cat="${sc.categoryId}">
                    ${sc.subCategoryName}
                  </option>
                </c:forEach>
              </select>
            </div>
          </div>

          <!-- Material List -->
          <div id="materialList" class="mb-3">
            <div class="material-item row mb-2">
              <div class="col-6">
                <label class="form-label">Material <span class="text-danger">*</span></label>
                <select name="materialId" class="form-select" required>
                  <option value="">-- Select material --</option>
                  <c:forEach var="m" items="${materials}">
                    <option value="${m.materialId}"
                            data-sub="${m.subCategoryId}"
                            data-stock="${m.quantity}">
                      ${m.materialName} (In stock: ${m.quantity})
                    </option>
                  </c:forEach>
                </select>
              </div>
              <div class="col-4">
                <label class="form-label">Quantity</label>
                <input type="number" name="quantity"
                       class="form-control"
                       min="1" required
                       placeholder="Enter quantity">
              </div>
              <div class="col-2 d-flex align-items-end">
                <button type="button"
                        class="btn btn-danger btn-sm remove-material"
                        style="display:none">
                  Remove
                </button>
              </div>
            </div>
          </div>
          <button type="button" class="btn btn-secondary btn-sm" onclick="addMaterial()">
            + Add another material
          </button>

          <!-- Note -->
          <div class="mb-3 mt-3">
            <label class="form-label">Note (optional)</label>
            <textarea name="note" class="form-control" rows="3"
                      placeholder="Reason for request..."></textarea>
          </div>

          <div class="d-flex justify-content-between">
            <a href="dashboard.jsp" class="btn btn-secondary">Back</a>
            <button type="submit" class="btn btn-primary">Submit Request</button>
          </div>
        </form>

      </div>
    </div>
  </div>
</div>

<script>
// Clone/remove material items
function addMaterial() {
  const list = document.getElementById('materialList');
  const item = list.firstElementChild.cloneNode(true);
  item.querySelectorAll('select, input').forEach(el => el.value = '');
  list.appendChild(item);
  updateRemoveButtons();
}
function removeMaterial(btn) {
  const items = document.querySelectorAll('.material-item');
  if (items.length > 1) {
    btn.closest('.material-item').remove();
  }
  updateRemoveButtons();
}
function updateRemoveButtons() {
  const items = document.querySelectorAll('.material-item');
  document.querySelectorAll('.remove-material').forEach(btn => {
    btn.style.display = items.length > 1 ? 'inline-block' : 'none';
    btn.onclick = () => removeMaterial(btn);
  });
}
document.addEventListener('DOMContentLoaded', updateRemoveButtons);

// Filter sub‑categories & materials
document.getElementById('categoryFilter').addEventListener('change', e => {
  const catId = e.target.value;
  document.querySelectorAll('#subCategoryFilter option').forEach(opt => {
    opt.hidden = catId && opt.getAttribute('data-cat') !== catId;
  });
  document.getElementById('subCategoryFilter').value = '';
  filterMaterials();
});
document.getElementById('subCategoryFilter').addEventListener('change', filterMaterials);
function filterMaterials() {
  const subId = document.getElementById('subCategoryFilter').value;
  document.querySelectorAll('select[name="materialId"] option[data-sub]').forEach(opt => {
    opt.hidden = subId && opt.getAttribute('data-sub') !== subId;
  });
}
</script>
</body>
</html>
