<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="max-w-4xl mx-auto p-6 bg-white rounded shadow-md">
    <h2 class="text-2xl font-semibold text-blue-600 mb-4">Create Repair Request</h2>

    <form method="post" action="createRepairRequest" class="space-y-6">
        <!-- Filters -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block font-medium">Category</label>
                <select id="categoryFilter" class="w-full border rounded p-2">
                    <option value="">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}">${cat.categoryName}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label class="block font-medium">SubCategory</label>
                <select id="subCategoryFilter" class="w-full border rounded p-2">
                    <option value="" data-cat="">All SubCategories</option>
                    <c:forEach var="sc" items="${subCategories}">
                        <option value="${sc.subCategoryId}" data-cat="${sc.categoryId}">${sc.subCategoryName}</option>
                    </c:forEach>


                </select>
            </div>
        </div>

        <!-- Material selection -->
        <div class="grid grid-cols-1 md:grid-cols-12 gap-4 items-end">
            <div class="md:col-span-6">
                <label class="block font-medium">Material</label>
                <select id="materialSelect" class="w-full border rounded p-2">
                    <option value="">-- Select Material --</option>
                    <c:forEach var="m" items="${materials}">
                        <option value="${m.materialId}"
                                data-cat="${m.categoryId}"
                                data-sub="${m.subCategoryId}"
                                data-stock="${m.quantity != null ? m.quantity : 0}"
                                data-name="${m.materialName != null ? m.materialName : 'Unnamed'}">
                            ${m.materialName != null ? m.materialName : 'Unnamed'} (In stock: ${m.quantity != null ? m.quantity : 0})
                        </option>
                    </c:forEach>
                </select>



            </div>
            <div class="md:col-span-3">
                <label class="block font-medium">Quantity</label>
                <input type="number" id="materialQuantity" class="w-full border p-2 rounded" min="1" value="1" />
            </div>
            <div class="md:col-span-3">
                <button type="button" id="addMaterialBtn" class="bg-yellow-600 hover:bg-yellow-700 text-white px-4 py-2 rounded w-full">
                    + Add Material
                </button>
            </div>
        </div>

        <!-- Material Table -->
        <div class="mt-6">
            <table class="w-full border text-sm" id="deviceTable">
                <thead class="bg-gray-100 font-medium">
                    <tr>
                        <th class="border p-2">No</th>
                        <th class="border p-2">Material</th>
                        <th class="border p-2">Quantity</th>
                        <th class="border p-2">Status</th>
                        <th class="border p-2">Estimated Budget</th>
                        <th class="border p-2">Delete</th>
                    </tr>
                </thead>
                <tbody id="deviceTableBody"></tbody>



            </table>
        </div>

        <!-- Note -->
        <div>
            <label class="block font-medium">Note (optional)</label>
            <textarea name="note" rows="3" class="w-full border rounded p-2"></textarea>
        </div>

        <!-- Submit -->
        <div class="flex justify-end space-x-2">
            <a href="javascript:history.back()" class="bg-gray-300 px-4 py-2 rounded">Back</a>
            <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">Submit</button>
        </div>
    </form>
</div>

<script>
    function filterMaterials(catId, subId) {
        const options = document.querySelectorAll('#materialSelect option');

        options.forEach(opt => {
            const matCat = opt.getAttribute('data-cat');
            const matSub = opt.getAttribute('data-sub');

            let show = true;

            if (catId && subId) {
                show = matCat === catId && matSub === subId;
            } else if (catId) {
                show = matCat === catId;
            } else {
                show = true;
            }

            // Ẩn/hiện option
            opt.style.display = show ? "" : "none";
        });

        // Reset lựa chọn
        document.getElementById('materialSelect').value = "";
    }

    document.getElementById('categoryFilter').addEventListener('change', function () {
        const catId = this.value;

        // Lọc lại SubCategory
        document.querySelectorAll('#subCategoryFilter option').forEach(opt => {
            const optCat = opt.getAttribute('data-cat');
            opt.style.display = !optCat || catId === "" || optCat === catId ? "" : "none";
        });

        // Reset SubCategory và lọc material
        document.getElementById('subCategoryFilter').value = "";
        filterMaterials(catId, "");
    });

    document.getElementById('subCategoryFilter').addEventListener('change', function () {
        const catId = document.getElementById('categoryFilter').value;
        const subId = this.value;
        filterMaterials(catId, subId);
    });

    document.getElementById('addMaterialBtn').addEventListener('click', () => {
        const matSelect = document.getElementById('materialSelect');
        const quantityInput = document.getElementById('materialQuantity');

        const selectedIndex = matSelect.selectedIndex;
        const selected = matSelect.options[selectedIndex];
        console.log("Selected Index:", selectedIndex); // Kiểm tra index
        console.log("Selected Option:", selected); // Kiểm tra option
        console.log("Selected Dataset:", selected?.dataset); // Kiểm tra dataset của option được chọn

        const matId = selected?.value || '';
        const matName = selected?.dataset?.name || 'No Name';
        const stock = selected?.dataset?.stock || '0';
        const quantity = quantityInput.value || '0';

        console.log("Raw Data:", {matId, matName, stock, quantity}); // Log dữ liệu thô
        if (!matId || matId === "" || parseInt(quantity) <= 0) {
            alert("Vui lòng chọn vật tư và nhập số lượng hợp lệ. matId: " + matId + ", Quantity: " + quantity);
            return;
        }

        console.log("Validated Data:", {matId, matName, stock, quantity}); // Log dữ liệu sau điều kiện

        const tbody = document.getElementById('deviceTableBody');
        const exists = [...tbody.querySelectorAll("input[name='materialId[]']")]
                .some(input => input.value === matId);
        if (exists) {
            alert("Vật tư này đã được thêm.");
            return;
        }

        const row = tbody.insertRow();
        const cell1 = row.insertCell(0);
        const cell2 = row.insertCell(1);
        const cell3 = row.insertCell(2);
        const cell4 = row.insertCell(3);
        const cell5 = row.insertCell(4);
        const cell6 = row.insertCell(5);

        cell1.innerHTML = `${tbody.rows.length}`;
        cell2.innerHTML = `${matName}`;
        cell3.innerHTML = `${quantity}`;
        cell4.innerHTML = `In stock: ${stock}`;
        cell5.innerHTML = `<input type="number" name="budget[]" class="w-full border p-1 rounded" placeholder="₫">`;
        cell6.innerHTML = `<button type="button" class="removeRow bg-red-500 text-white px-2 py-1 rounded">X</button>`;

        row.querySelector('.removeRow').onclick = function () {
            row.remove();
            updateIndexes();
        };

        updateIndexes();
        matSelect.value = "";
        quantityInput.value = "";
    });

    function updateIndexes() {
        const rows = document.querySelectorAll('#deviceTable tbody tr');
        console.log("Updating indexes for " + rows.length + " rows");
        rows.forEach((row, i) => {
            row.cells[0].innerText = i + 1;
        });
    }
</script>


