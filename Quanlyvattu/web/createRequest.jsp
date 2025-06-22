<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Material Request</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100">

        <div class="max-w-3xl mx-auto mt-8 px-4">
            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <div class="bg-blue-600 text-white p-4">
                    <h2 class="text-xl font-semibold">Create Material Request</h2>
                </div>
                <div class="p-6">

                    <!-- Alert messages -->
                    <c:if test="${not empty error}">
                        <div class="bg-red-100 text-red-700 p-3 mb-4 rounded">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="bg-green-100 text-green-700 p-3 mb-4 rounded">${success}</div>
                    </c:if>

                    <!-- Form -->
                    <form method="post" action="createrequest" class="space-y-6">

                        <!-- Request Type -->
                        <div>
                            <label class="block font-medium mb-1">Request Type <span class="text-red-500">*</span></label>
                            <select name="requestTypeId" required class="w-full border border-gray-300 rounded p-2">
                                <option value="">-- Select a type --</option>
                                <c:forEach var="type" items="${requestTypes}">
                                    <option value="${type.requestTypeId}">${type.requestTypeName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Category & SubCategory Filters -->
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <label class="block font-medium mb-1">Category</label>
                                <select id="categoryFilter" class="w-full border border-gray-300 rounded p-2">
                                    <option value="">All Categories</option>
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat.categoryId}">${cat.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label class="block font-medium mb-1">Sub‑Category</label>
                                <select id="subCategoryFilter" class="w-full border border-gray-300 rounded p-2">
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
                        <div id="materialList" class="space-y-4">
                            <div class="material-item grid grid-cols-1 md:grid-cols-12 gap-4 items-end">
                                <div class="md:col-span-6">
                                    <label class="block font-medium mb-1">Material <span class="text-red-500">*</span></label>
                                    <select name="materialId" class="form-select" required onchange="checkDup(this)">
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
                                <div class="md:col-span-4">
                                    <label class="block font-medium mb-1">Quantity</label>
                                    <input type="number" name="quantity" class="w-full border border-gray-300 rounded p-2" min="1" required placeholder="Enter quantity">
                                </div>
                                <div class="md:col-span-2">
                                    <button type="button" class="remove-material hidden text-white bg-red-500 hover:bg-red-600 rounded px-3 py-2 text-sm w-full">Remove</button>
                                </div>
                            </div>
                        </div>

                        <!-- Add Material Button -->
                        <button type="button" onclick="addMaterial()" class="bg-gray-200 text-gray-800 hover:bg-gray-300 rounded px-4 py-2 text-sm">
                            + Add another material
                        </button>

                        <!-- Note -->
                        <div>
                            <label class="block font-medium mb-1 mt-3">Note (optional)</label>
                            <textarea name="note" rows="3" class="w-full border border-gray-300 rounded p-2" placeholder="Reason for request..."></textarea>
                        </div>

                        <!-- Buttons -->
                        <div class="flex justify-between items-center mt-4">
                            <a href="dashboard" class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded">Back</a>
                            <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded">Submit Request</button>
                        </div>
                    </form>
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
                    btn.style.display = items.length > 1 ? 'block' : 'none';
                    btn.onclick = () => removeMaterial(btn);
                });
            }
            document.addEventListener('DOMContentLoaded', updateRemoveButtons);

            // Filter logic
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

       function checkDup(sel) {
                if (!sel.value)
                    return;
                // All select - id selected
                const others = Array.from(
                        document.getElementsByName('materialId')
                        ).filter(s => s !== sel);

                // Nếu có select khác đã chọn cùng giá trị
                if (others.some(s => s.value === sel.value)) {
                    alert('This material is already selected!');
                    sel.value = '';  // reset về “-- Select material --”
                }
            }
        </script>

    </body>
</html>
