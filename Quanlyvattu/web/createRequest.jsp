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
                        <select name="requestTypeId" required class="w-full border border-gray-300 rounded p-2" onchange="toggleStockCheck()">
                            <option value="">-- Select a type --</option>
                            <c:forEach var="type" items="${requestTypes}">
                                 <c:if test="${type.requestTypeId != 3}">
                                    <option value="${type.requestTypeId}">${type.requestTypeName}</option>
                                </c:if>
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
                                <select name="materialId" class="form-select w-full border border-gray-300 rounded p-2" required>
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
                                <input type="number" name="quantity" class="w-full border border-gray-300 rounded p-2 quantity-input" min="1" required placeholder="Enter quantity" oninput="validateQuantity(this)">
                                <small class="text-gray-500 stock-info"></small>
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
                        <button type="submit" class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded" onclick="return validateForm()">Submit Request</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Global variables
        let stockCheckRequired = false;
        let materialStockData = {};

        // Khởi tạo khi DOM đã load
        document.addEventListener('DOMContentLoaded', function() {
            initFilters();
            updateRemoveButtons();
            filterMaterials();
            initMaterialStockData();
        });

        function initMaterialStockData() {
            // Initialize stock data from options
            const firstSelect = document.querySelector('select[name="materialId"]');
            const options = firstSelect.querySelectorAll('option');
            options.forEach(function(opt) {
                if (opt.value) {
                    const stock = opt.getAttribute('data-stock');
                    materialStockData[opt.value] = parseInt(stock) || 0;
                }
            });
        }

        function toggleStockCheck() {
            const requestTypeId = document.querySelector('select[name="requestTypeId"]').value;
            stockCheckRequired = (requestTypeId === '1' || requestTypeId === '4'); // Emergency or Special
            
            // Update all quantity inputs
            const quantityInputs = document.querySelectorAll('.quantity-input');
            quantityInputs.forEach(function(input) {
                validateQuantity(input);
            });
        }

        function validateQuantity(input) {
            const materialRow = input.closest('.material-item');
            const materialSelect = materialRow.querySelector('select[name="materialId"]');
            const stockInfo = materialRow.querySelector('.stock-info');
            const materialId = materialSelect.value;
            const quantity = parseInt(input.value) || 0;
            
            // Clear previous states
            stockInfo.textContent = '';
            input.classList.remove('border-red-500', 'border-green-500');
            
            // Only validate if material is selected and quantity is entered
            if (!materialId || !quantity) {
                return;
            }
            
            const availableStock = materialStockData[materialId] || 0;
            
            // Only show warning for Emergency/Special requests when quantity exceeds stock
            if (stockCheckRequired && quantity > availableStock) {
                input.classList.add('border-red-500');
                stockInfo.textContent = `Quantity exceeds available stock (${availableStock} available)`;
                stockInfo.className = 'text-red-500 text-sm stock-info';
            } else {
                // Don't show any message for valid quantities
                input.classList.add('border-green-500');
                stockInfo.textContent = '';
            }
        }

        function initFilters() {
            // Event listener cho category filter
            const categoryFilter = document.getElementById('categoryFilter');
            if (categoryFilter) {
                categoryFilter.addEventListener('change', function() {
                    const catId = this.value;
                    const subCategoryFilter = document.getElementById('subCategoryFilter');
                    
                    // Ẩn/hiện subcategory options
                    const subOptions = subCategoryFilter.querySelectorAll('option');
                    subOptions.forEach(function(opt) {
                        if (opt.value === '') {
                            opt.style.display = 'block'; // Always show "All Sub-Categories"
                        } else {
                            const optCat = opt.getAttribute('data-cat');
                            opt.style.display = (!catId || optCat === catId) ? 'block' : 'none';
                        }
                    });
                    
                    // Reset subcategory selection
                    subCategoryFilter.value = '';
                    filterMaterials();
                });
            }

            // Event listener cho subcategory filter
            const subCategoryFilter = document.getElementById('subCategoryFilter');
            if (subCategoryFilter) {
                subCategoryFilter.addEventListener('change', filterMaterials);
            }

            // Event delegation cho material selects
            const materialList = document.getElementById('materialList');
            if (materialList) {
                materialList.addEventListener('change', function(e) {
                    if (e.target.name === 'materialId') {
                        checkDuplicate(e.target);
                        filterMaterials();
                        // Update stock info when material changes
                        const quantityInput = e.target.closest('.material-item').querySelector('.quantity-input');
                        validateQuantity(quantityInput);
                    }
                });
            }
        }

        function addMaterial() {
            const list = document.getElementById('materialList');
            const firstItem = list.querySelector('.material-item');
            const newItem = firstItem.cloneNode(true);
            
            // Reset values
            const select = newItem.querySelector('select[name="materialId"]');
            const input = newItem.querySelector('input[name="quantity"]');
            const stockInfo = newItem.querySelector('.stock-info');
            if (select) select.value = '';
            if (input) {
                input.value = '';
                input.classList.remove('border-red-500', 'border-green-500');
            }
            if (stockInfo) stockInfo.textContent = '';
            
            list.appendChild(newItem);
            updateRemoveButtons();
            filterMaterials();
        }

        function removeMaterial(btn) {
            const items = document.querySelectorAll('.material-item');
            if (items.length > 1) {
                const item = btn.closest('.material-item');
                item.remove();
                updateRemoveButtons();
                filterMaterials();
            }
        }

        function updateRemoveButtons() {
            const items = document.querySelectorAll('.material-item');
            const removeButtons = document.querySelectorAll('.remove-material');
            
            removeButtons.forEach(function(btn) {
                if (items.length > 1) {
                    btn.style.display = 'block';
                    btn.onclick = function() { removeMaterial(btn); };
                } else {
                    btn.style.display = 'none';
                }
            });
        }

        function filterMaterials() {
            const catId = document.getElementById('categoryFilter').value;
            const subId = document.getElementById('subCategoryFilter').value;
            const selects = document.querySelectorAll('select[name="materialId"]');
            
            // Get all selected material IDs
            const selectedIds = [];
            selects.forEach(function(select) {
                if (select.value) {
                    selectedIds.push(select.value);
                }
            });

            selects.forEach(function(select) {
                const currentValue = select.value;
                const options = select.querySelectorAll('option');
                
                options.forEach(function(opt) {
                    if (!opt.value) {
                        // Always show placeholder option
                        opt.style.display = 'block';
                        return;
                    }

                    const optSub = opt.getAttribute('data-sub');
                    let optCat = '';
                    
                    // Find category for this subcategory
                    if (optSub) {
                        const subOption = document.querySelector('#subCategoryFilter option[value="' + optSub + '"]');
                        if (subOption) {
                            optCat = subOption.getAttribute('data-cat');
                        }
                    }

                    // Check filters
                    const matchesCategory = !catId || optCat === catId;
                    const matchesSubCategory = !subId || optSub === subId;
                    const isDuplicate = selectedIds.includes(opt.value) && opt.value !== currentValue;

                    // Show/hide option
                    if (matchesCategory && matchesSubCategory && !isDuplicate) {
                        opt.style.display = 'block';
                    } else {
                        opt.style.display = 'none';
                    }
                });
            });
        }

        function checkDuplicate(select) {
            if (!select.value) return;
            
            const allSelects = document.querySelectorAll('select[name="materialId"]');
            let isDuplicate = false;
            
            allSelects.forEach(function(otherSelect) {
                if (otherSelect !== select && otherSelect.value === select.value) {
                    isDuplicate = true;
                }
            });
            
            if (isDuplicate) {
                alert('This material is already selected!');
                select.value = '';
            }
        }

        function validateForm() {
            // Check if any materials are selected
            const materialSelects = document.querySelectorAll('select[name="materialId"]');
            let hasValidMaterial = false;
            
            for (let select of materialSelects) {
                if (select.value) {
                    hasValidMaterial = true;
                    break;
                }
            }
            
            if (!hasValidMaterial) {
                alert('Please select at least one material.');
                return false;
            }
            
            // Check stock validation for Emergency/Special requests
            if (stockCheckRequired) {
                const quantityInputs = document.querySelectorAll('.quantity-input');
                for (let input of quantityInputs) {
                    if (input.classList.contains('border-red-500')) {
                        alert('Please fix quantity issues before submitting.');
                        return false;
                    }
                }
            }
            
            return true;
        }
    </script>
</body>
</html>
