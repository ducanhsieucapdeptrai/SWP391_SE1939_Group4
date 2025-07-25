<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isErrorPage="true" %>

<div class="p-6 bg-white rounded shadow">
    <div class="flex justify-between items-center mb-4">
        <h2 class="text-2xl font-semibold">Unit Management</h2>
        <button id="toggleAddUnitBtn" class="bg-blue-600 hover:bg-blue-700 text-white py-1 px-4 rounded">
            + Add Unit
        </button>
    </div>



    <!-- Unit Table -->
    <div class="overflow-x-auto">
        <table class="min-w-full text-sm text-left border">
            <thead class="bg-gray-100 text-gray-600">
                <tr>
                    <th class="py-2 px-4 border">#</th>
                    <th class="py-2 px-4 border">Name</th>
                    <th class="py-2 px-4 border">Status</th>
                    <th class="py-2 px-4 border">In Used</th>
                    <th class="py-2 px-4 border">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${units}" varStatus="loop">
                    <tr class="border-b hover:bg-gray-50">
                        <td class="py-2 px-4 border">${(currentPage - 1) * 5 + loop.index + 1}</td>
                        <td class="py-2 px-4 border">${u.name}</td>
                        <td class="py-2 px-4 border">
                            <c:choose>
                                <c:when test="${u.status eq 'active'}">
                                    <span class="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs font-semibold">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="bg-gray-200 text-gray-700 px-2 py-1 rounded-full text-xs font-semibold">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="py-2 px-4 border text-center">
                            <c:choose>
                                <c:when test="${u.isUsedInMaterials}">✅</c:when>
                                <c:otherwise>❌</c:otherwise>
                            </c:choose>
                        </td>
                        <td class="py-2 px-4 border space-x-2">
                            <button onclick="viewUnit(${u.unitId})" class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-sm">
                                View
                            </button>
                            <button onclick="editUnit(${u.unitId})" class="bg-yellow-400 hover:bg-yellow-500 text-white px-3 py-1 rounded text-sm">
                                Edit
                            </button>
                            <button onclick="deactivateUnit(${u.unitId})" class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-sm">
                                Deactivate
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty units}">
                    <tr>
                        <td colspan="5" class="text-center py-4 text-gray-500">No units found.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <!-- Pagination -->
    <div class="flex justify-between items-center mt-4">
        <span>Total: ${totalUnits} units</span>
        <div>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="unit?page=${i}" class="px-3 py-1 border rounded mx-1
                   <c:if test='${i == currentPage}'>bg-blue-600 text-white</c:if>
                   <c:if test='${i != currentPage}'>bg-white text-gray-700 hover:bg-gray-200</c:if>">
                    ${i}
                </a>
            </c:forEach>
        </div>
    </div>
</div>

<!-- View Unit Modal -->
<div id="viewModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center hidden z-50">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-sm relative animate-fade-in">
        <h2 class="text-lg font-bold mb-4 text-center">Unit Details</h2>
        <div class="space-y-2 text-sm text-gray-700">
            <p><strong>Name:</strong> <span id="viewName"></span></p>
            <p><strong>Status:</strong> <span id="viewStatus"></span></p>
            <p><strong>Created At:</strong> <span id="viewCreatedAt"></span></p>
            <p><strong>Updated At:</strong> <span id="viewUpdatedAt"></span></p>
        </div>
        <button onclick="closeViewModal()"
                class="absolute top-2 right-2 text-gray-500 hover:text-black text-xl font-bold">×</button>
    </div>
</div>
<!-- Edit Unit Modal -->
<div id="editModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center hidden z-50">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-sm relative animate-fade-in">
        <h2 class="text-lg font-bold mb-4 text-center">✏️ Edit Unit</h2>
        <form id="editUnitForm" class="space-y-4">
            <input type="hidden" id="editUnitId">
            <div>
                <label class="block text-sm font-medium text-gray-600">Name</label>
                <input type="text" id="editName" class="w-full mt-1 px-3 py-2 border rounded" required />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-600">Status</label>
                <select id="editStatus" class="w-full mt-1 px-3 py-2 border rounded">
                    <option value="active">Active</option>
                    <option value="inactive">Inactive</option>
                </select>
            </div>
            <div class="flex justify-end gap-2">
                <button type="button" onclick="closeEditModal()" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Cancel</button>
                <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Save</button>
            </div>
        </form>
        <button onclick="closeEditModal()" class="absolute top-2 right-2 text-gray-500 hover:text-black text-xl font-bold">×</button>
    </div>
</div>
<!-- Add Unit Modal -->
<div id="addModal" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center hidden z-50">
    <div class="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative animate-fade-in">
        <h2 class="text-lg font-bold mb-4 text-center">➕ Add Unit</h2>
        <form id="addUnitFormModal" class="space-y-4 text-sm text-gray-700">
            <div>
                <label for="addName" class="block font-medium">Unit Name</label>
                <input type="text" id="addName" name="name" required
                       class="w-full mt-1 px-3 py-2 border rounded" />
            </div>
            <div>
                <label for="addStatus" class="block font-medium">Status</label>
                <select id="addStatus" name="status" class="w-full mt-1 px-3 py-2 border rounded">
                    <option value="active">Active</option>
                    <option value="inactive">Inactive</option>
                </select>
            </div>
            <div class="flex justify-end space-x-2 pt-2">
                <button type="button" onclick="closeAddModal()" class="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded">
                    Cancel
                </button>
                <button type="submit" class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded">
                    Save
                </button>
            </div>
        </form>
        <button onclick="closeAddModal()"
                class="absolute top-2 right-2 text-gray-500 hover:text-black text-xl font-bold">×</button>
    </div>
</div>

<style>
    @keyframes fade-in {
        from {
            opacity: 0;
            transform: scale(0.95);
        }
        to   {
            opacity: 1;
            transform: scale(1);
        }
    }

    .animate-fade-in {
        animation: fade-in 0.2s ease-out;
    }
</style>

<!-- Scripts -->
<script>
    function viewUnit(id) {
        console.log("DEBUG: viewUnit called with ID =", id);

        if (!id || isNaN(id)) {
            console.warn("Invalid or missing unit ID", id);
            alert("❌ Invalid unit ID!");
            return;
        }

        fetch("unit?action=view&id=" + id)
                .then(res => {
                    if (!res.ok)
                        throw new Error("HTTP error " + res.status);
                    return res.json();
                })
                .then(data => {
                    if (data.success) {
                        const u = data.unit;
                        document.getElementById("viewName").textContent = u.name;
                        document.getElementById("viewStatus").textContent = u.status === "active" ? " Active" : " Inactive";
                        document.getElementById("viewCreatedAt").textContent = u.createdAt;
                        document.getElementById("viewUpdatedAt").textContent = u.updatedAt;
                        document.getElementById("viewModal").classList.remove("hidden");
                    } else {
                        alert("❌ " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Error fetching unit details:", error);
                    alert("❌ Failed to load unit data.");
                });
    }

    function closeViewModal() {
        document.getElementById("viewModal").classList.add("hidden");
    }

    document.getElementById("toggleAddUnitBtn").addEventListener("click", function () {
        document.getElementById("addUnitForm").classList.toggle("hidden");
    });

    function editUnit(id) {
        alert("Edit unit: " + id);
    }

    function deactivateUnit(id) {
        alert("Deactivate unit: " + id);
    }
</script>
<script>
    function editUnit(id) {
        if (!id || isNaN(id)) {
            alert("❌ Invalid unit ID");
            return;
        }

        fetch("unit?action=edit&id=" + id)
                .then(res => {
                    if (!res.ok)
                        throw new Error("HTTP error " + res.status);
                    return res.json();
                })
                .then(data => {
                    if (data.success) {
                        const u = data.unit;
                        document.getElementById("editUnitId").value = u.unitId;
                        document.getElementById("editName").value = u.name;
                        document.getElementById("editStatus").value = u.status;
                        document.getElementById("editModal").classList.remove("hidden");
                    } else {
                        alert("❌ " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Error loading unit for edit:", error);
                    alert("❌ Failed to load unit data.");
                });
    }

    function closeEditModal() {
        document.getElementById("editModal").classList.add("hidden");
    }

    document.getElementById("editUnitForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const id = document.getElementById("editUnitId").value;
        const name = document.getElementById("editName").value;
        const status = document.getElementById("editStatus").value;

        fetch("unit?action=edit&id=" + id, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                id: id,
                name: name,
                status: status
            })
        })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        alert("✅ " + data.message);
                        window.location.reload();
                    } else {
                        alert("❌ " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Error saving unit:", error);
                    alert("❌ Failed to update unit.");
                });
    });
</script>
<script>
    function deactivateUnit(id) {
        if (!id || isNaN(id)) {
            alert("❌ Invalid unit ID");
            return;
        }

        if (!confirm("⚠️ Are you sure you want to deactivate this unit?")) {
            return;
        }

        fetch("unit?action=deactivate&id=" + id, {
            method: "POST"
        })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        alert("✅ " + data.message);
                        window.location.reload();
                    } else {
                        alert("❌ " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Error deactivating unit:", error);
                    alert("❌ Failed to deactivate unit.");
                });
    }

</script>

<script>
// Hiện modal Add
    document.getElementById("toggleAddUnitBtn").addEventListener("click", function () {
        document.getElementById("addModal").classList.remove("hidden");
    });

// Đóng modal Add
    function closeAddModal() {
        document.getElementById("addModal").classList.add("hidden");
        document.getElementById("addUnitFormModal").reset(); // Clear form
    }

// Gửi dữ liệu tạo mới unit
    document.getElementById("addUnitFormModal").addEventListener("submit", function (e) {
        e.preventDefault();

        const name = document.getElementById("addName").value.trim();
        const status = document.getElementById("addStatus").value;

        if (!name) {
            alert("❌ Unit name is required!");
            return;
        }

        fetch("${pageContext.request.contextPath}/unit?action=add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "name=" + encodeURIComponent(name) + "&status=" + encodeURIComponent(status)
        })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        alert("✅ " + data.message);
                        closeAddModal();
                        window.location.reload(); // hoặc load lại bảng ajax
                    } else {
                        alert("❌ " + data.message);
                    }
                })
                .catch(error => {
                    console.error("Error adding unit:", error);
                    alert("❌ Failed to add unit.");
                });
    });

</script>