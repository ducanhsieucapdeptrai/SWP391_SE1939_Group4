<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<c:if test="${not empty error}">
    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">${error}</div>
</c:if>
<c:if test="${not empty message}">
    <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">${message}</div>
</c:if>

<div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
    <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-semibold text-gray-800">Create Repair Request</h2>
        <a href="${pageContext.request.contextPath}/reqlist" class="text-blue-600 hover:text-blue-800 flex items-center">
            <i class="fas fa-arrow-left mr-2"></i> Back
        </a>
    </div>

    <form id="frmRequest" action="create-repair-request" method="post">
        <!-- Request Type: Fixed as Repair -->
        <div class="mb-4">
            <label class="block text-sm font-medium text-gray-700 mb-2">Request Type:</label>
            <input type="hidden" name="typeId" value="4" />
            <input type="text" value="Repair" readonly class="w-full px-3 py-2 bg-gray-100 border rounded-md" />
        </div>

        <!-- Material Dropdown -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Material:</label>
            <select id="materialId" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                <option value="">--Select--</option>
                <c:forEach var="m" items="${materials}">
                    <option value="${m.materialId}"
                            data-name="${m.materialName}"
                            data-img="${pageContext.request.contextPath}/assets/images/materials/${m.image}"
                            data-stock="${m.quantity}">
                        ${m.materialName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Stock & Quantity -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end mb-6">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Stock:</label>
                <input type="text" id="stock" readonly class="w-full px-3 py-2 bg-gray-100 border rounded-md">
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Quantity:</label>
                <input type="number" id="quantity" min="1" value="1" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
            </div>
            <div>
                <button type="button" id="btnAdd" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md w-full">Add Material</button>
            </div>
        </div>

        <!-- Selected Materials Table -->
        <div class="mb-6">
            <h3 class="text-lg font-medium text-gray-800 mb-4">Selected Materials</h3>
            <div class="overflow-x-auto border border-gray-200 rounded-lg">
                <table id="tblItems" class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">STT</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Image</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantity</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Action</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <!-- JS adds rows here -->
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Project -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Project:</label>
            <select name="projectId" id="projectId" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500">
                <option value="">-- No Project --</option>
                <c:forEach var="p" items="${projectList}">
                    <option value="${p.projectId}">${p.projectName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Note -->
        <div class="mb-6">
            <label class="block text-sm font-medium text-gray-700 mb-2">Note:</label>
            <textarea name="note" rows="3" class="w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 resize-vertical"></textarea>
        </div>

        <!-- Submit -->
        <div class="text-center">
            <button type="submit" class="bg-blue-500 hover:bg-blue-600 text-white px-8 py-3 rounded-md font-medium">Create Request</button>
        </div>
    </form>
</div>

<!-- Script -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const btnAdd = document.getElementById("btnAdd");
        const tblBody = document.querySelector("#tblItems tbody");
        const materialSelect = document.getElementById("materialId");
        const quantityInput = document.getElementById("quantity");
        const stockInput = document.getElementById("stock");
        let counter = 1;

        materialSelect.addEventListener("change", function () {
            const selected = this.options[this.selectedIndex];
            const stock = selected.getAttribute("data-stock");
            stockInput.value = stock || "";
        });

        btnAdd.addEventListener("click", function () {
            const selected = materialSelect.options[materialSelect.selectedIndex];
            const materialId = selected.value;
            const materialName = selected.getAttribute("data-name");
            const materialImg = selected.getAttribute("data-img");
            const quantity = parseInt(quantityInput.value);
            const stock = parseInt(stockInput.value);

            if (!materialId || isNaN(quantity) || quantity <= 0) {
                alert("Please select a material and enter a valid quantity.");
                return;
            }

            if (quantity > stock) {
                alert("Quantity cannot exceed available stock (" + stock + ").");
                return;
            }

            const existing = tblBody.querySelector(`tr[data-id="${materialId}"]`);
            if (existing) {
                alert("This material is already added.");
                return;
            }

            const row = document.createElement("tr");
            row.setAttribute("data-id", materialId);
            row.innerHTML =
                    '<td class="px-6 py-3">' + counter + '</td>' +
                    '<td class="px-6 py-3"><img src="' + materialImg + '" class="w-10 h-10 object-cover"></td>' +
                    '<td class="px-6 py-3">' + materialName + '</td>' +
                    '<td class="px-6 py-3">' + quantity + '</td>' +
                    '<td class="px-6 py-3"><button type="button" class="text-red-600 remove-btn">Remove</button></td>' +
                    '<input type="hidden" name="materialId[]" value="' + materialId + '">' +
                    '<input type="hidden" name="quantity[]" value="' + quantity + '">';
            tblBody.appendChild(row);
            counter++;
        });

        tblBody.addEventListener("click", function (e) {
            if (e.target.classList.contains("remove-btn")) {
                const row = e.target.closest("tr");
                row.remove();

                // Reset STT
                let i = 1;
                tblBody.querySelectorAll("tr").forEach(tr => {
                    tr.children[0].textContent = i++;
                });
                counter = i;
            }
        });
    });
</script>

