<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Lấy context path để build đường dẫn ảnh
    String cp = request.getContextPath();
%>


<div class="max-w-5xl mx-auto my-8 p-6 bg-white rounded shadow">
    <h2 class="text-2xl font-semibold mb-6">Create Export / Purchase Request</h2>

    <!-- Hiển thị lỗi hoặc thông báo thành công -->
    <c:if test="${not empty error}">
        <div class="mb-4 p-3 bg-red-100 text-red-700 rounded">${error}</div>
    </c:if>
    <c:if test="${param.success == '1'}">
        <div class="mb-4 p-3 bg-green-100 text-green-700 rounded">
            Request created successfully!
        </div>
    </c:if>

    <form id="requestForm" method="post" action="createexportpurchase">

        <!-- 1. Request Type -->
        <div class="mb-4">
            <label for="typeId" class="block mb-1 font-medium">Request Type:</label>
            <select name="typeId" id="typeId" required
                    class="w-full px-3 py-2 border rounded-md focus:ring focus:ring-blue-300">
                <option value="">-- Select Type --</option>
                <c:forEach var="type" items="${types}">
                    <option value="${type.requestTypeId}">${type.requestTypeName}</option>
                </c:forEach>
            </select>
        </div>

        <!-- 2. Material + Quantity + Add -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 items-end mb-6">
            <!-- Material -->
            <div>
                <label class="block mb-1 font-medium">Material:</label>
                <select id="materialId"
                        class="w-full px-3 py-2 border rounded-md focus:ring focus:ring-blue-300">
                    <option value="">-- Select Material --</option>
                    <c:forEach var="m" items="${materials}">
                        <option value="${m.materialId}"
                                data-name="${m.materialName}"
                                data-img="<%= cp %>/assets/images/materials/${m.image}">
                            ${m.materialName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Quantity -->
            <div>
                <label class="block mb-1 font-medium">Quantity:</label>
                <input type="number" id="quantity" min="1" placeholder="Enter quantity"
                       class="w-full px-3 py-2 border rounded-md focus:ring focus:ring-blue-300" />
            </div>

            <!-- Add Button -->
            <div>
                <button type="button" id="addBtn"
                        class="w-full bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md">
                    Add Material
                </button>
            </div>
        </div>

        <!-- 3. Selected Materials Table -->
        <div class="mb-6">
            <h3 class="text-lg font-semibold mb-2">Selected Materials</h3>
            <div class="overflow-x-auto border rounded">
                <table id="materialTable" class="min-w-full text-left text-sm">
                    <thead class="bg-gray-100">
                        <tr>
                            <th class="p-2">#</th>
                            <th class="p-2">Image</th>
                            <th class="p-2">Name</th>
                            <th class="p-2">Quantity</th>
                            <th class="p-2">Action</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>

        <!-- 4. Note -->
        <div class="mb-6">
            <label for="note" class="block mb-1 font-medium">Note <span class="text-red-600">*</span>:</label>
            <textarea name="note" id="note" rows="3" required
                      class="w-full px-3 py-2 border rounded-md resize-vertical focus:ring focus:ring-blue-300"
                      placeholder="Enter request note..."></textarea>
        </div>

        <!-- 5. Submit -->
        <div class="text-center">
            <button type="submit"
                    class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded-md font-medium">
                Create Request
            </button>
        </div>
    </form>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
  const addBtn      = document.getElementById("addBtn");
  const materialSel = document.getElementById("materialId");
  const qtyInput    = document.getElementById("quantity");
  const tableBody   = document.querySelector("#materialTable tbody");
  let counter       = 1;

  addBtn.addEventListener("click", function() {
    const opt     = materialSel.options[materialSel.selectedIndex];
    const matId   = opt.value;
    const matName = opt.getAttribute("data-name");
    const imgSrc  = opt.getAttribute("data-img");
    const qty     = parseInt(qtyInput.value);

    // 1) Validate chọn vật tư
    if (!matId) {
      alert("❌ Vui lòng chọn vật tư!");
      return;
    }
    // 2) Validate quantity
    if (!qty || qty <= 0) {
      alert("❌ Số lượng phải là số nguyên lớn hơn 0");
      return;
    }
    // 3) Duplicate check: kiểm tra trong tất cả hidden inputs materialId[]
    const existingIds = Array.from(tableBody.querySelectorAll('input[name="materialId[]"]'))
                             .map(i => i.value);
    if (existingIds.includes(matId)) {
      alert("❌ Vật tư này đã được thêm rồi!");
      return;
    }

    // Tạo row mới như trước
    const tr = document.createElement("tr");
    tr.setAttribute("data-id", matId);
    // STT
    const tdIdx = document.createElement("td");
    tdIdx.className = "p-2";
    tdIdx.textContent = counter++;
    // Ảnh
    const tdImg = document.createElement("td");
    tdImg.className = "p-2";
    const img = document.createElement("img");
    img.src = imgSrc;
    img.alt = matName;
    img.className = "w-12 h-12 object-cover rounded border";
    tdImg.appendChild(img);
    // Tên
    const tdName = document.createElement("td");
    tdName.className = "p-2";
    tdName.textContent = matName;
    // Số lượng + edit input
    const tdQty = document.createElement("td");
    tdQty.className = "p-2";
    const spanQty = document.createElement("span");
    spanQty.className = "qty-text";
    spanQty.textContent = qty;
    const inpQty = document.createElement("input");
    inpQty.type = "number";
    inpQty.min = "1";
    inpQty.value = qty;
    inpQty.className = "edit-qty hidden w-20 border px-1 py-1 rounded";
    tdQty.append(spanQty, inpQty);
    // Action
    const tdAct = document.createElement("td");
    tdAct.className = "p-2";
    const btnEdit = document.createElement("button");
    btnEdit.type = "button";
    btnEdit.textContent = "Edit";
    btnEdit.className = "editBtn text-blue-600 mr-2";
    const btnSave = document.createElement("button");
    btnSave.type = "button";
    btnSave.textContent = "Save";
    btnSave.className = "saveBtn text-green-600 mr-2 hidden";
    const btnRem = document.createElement("button");
    btnRem.type = "button";
    btnRem.textContent = "Remove";
    btnRem.className = "removeBtn text-red-600";
    tdAct.append(btnEdit, btnSave, btnRem);
    // Hidden inputs
    const hidMat = document.createElement("input");
    hidMat.type = "hidden"; hidMat.name = "materialId[]"; hidMat.value = matId;
    const hidQty = document.createElement("input");
    hidQty.type = "hidden"; hidQty.name = "quantity[]"; hidQty.className = "qty-hidden"; hidQty.value = qty;
    // Append
    tr.append(tdIdx, tdImg, tdName, tdQty, tdAct, hidMat, hidQty);
    tableBody.appendChild(tr);

    // Clear quantity input
    qtyInput.value = "";
  });

  // Edit/Save/Remove như trước...
  tableBody.addEventListener("click", function(e) {
    const btn = e.target;
    if (btn.tagName !== "BUTTON") return;
    const tr = btn.closest("tr");

    if (btn.classList.contains("removeBtn")) {
      tr.remove();
      // Re-index
      counter = 1;
      tableBody.querySelectorAll("tr").forEach(r => {
        r.children[0].textContent = counter++;
      });
      return;
    }
    if (btn.classList.contains("editBtn")) {
      tr.querySelector(".qty-text").classList.add("hidden");
      tr.querySelector(".edit-qty").classList.remove("hidden");
      btn.classList.add("hidden");
      tr.querySelector(".saveBtn").classList.remove("hidden");
    }
    if (btn.classList.contains("saveBtn")) {
      const newVal = parseInt(tr.querySelector(".edit-qty").value);
      if (!newVal || newVal <= 0) {
        alert("❌ Quantity must be greater than 0");
        return;
      }
      tr.querySelector(".qty-text").textContent = newVal;
      tr.querySelector(".qty-text").classList.remove("hidden");
      tr.querySelector(".edit-qty").classList.add("hidden");
      btn.classList.add("hidden");
      tr.querySelector(".editBtn").classList.remove("hidden");
      tr.querySelector(".qty-hidden").value = newVal;
    }
  });
});
</script>
