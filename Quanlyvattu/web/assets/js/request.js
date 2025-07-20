$(document).ready(function() {
    console.log('Request.js loaded and DOM ready');
    
    // Initialize form to clean state
    initializeForm();
    
    // Event handlers for dropdowns
    $('#typeId').on('change', loadSubTypes);
    $('#catId').on('change', loadSubCategories);
    $('#subCatId').on('change', loadMaterials);
    $('#materialId').on('change', updateStockInfo);
    $('#btnAdd').on('click', addMaterial);
    $('#btnFilter').on('click', resetFilter);

    // Form submit handler with validation
    $('#frmRequest').on('submit', function(e) {
        console.log('Form submission started');
        
        // Debug: Check materials data before submission
        const materialInputs = document.querySelectorAll('input[name="materialId[]"]');
        const quantityInputs = document.querySelectorAll('input[name="quantity[]"]');
        console.log('Materials found:', materialInputs.length);
        console.log('Quantities found:', quantityInputs.length);
        
        materialInputs.forEach((input, index) => {
            console.log(`Material ${index}: ${input.value}`);
        });
        quantityInputs.forEach((input, index) => {
            console.log(`Quantity ${index}: ${input.value}`);
        });
        
        if (!validateForm()) {
            console.log('Form validation failed');
            e.preventDefault();
        } else {
            console.log('Form validation passed, submitting...');
        }
    });
});

// Initialize form to clean state
function initializeForm() {
    console.log('Initializing form...');
    
    // Reset all dropdowns to default state
    $('#subTypeId').html('<option value="">--Select--</option>');
    $('#subCatId').html('<option value="">--All--</option>');
    $('#materialId').html('<option value="">--Select--</option>');
    
    // Clear material info
    clearMaterialInfo();
    
    // Clear selected materials table
    $('#tblItems tbody').empty();
    
    // Reset form fields
    $('#note').val('');
    
    // Show subtype field (might be hidden for purchase requests)
    $('#subTypeId').closest('div').show();
    
    console.log('Form initialized successfully');
}

function loadSubTypes() {
    const typeId = $('#typeId').val();
    const selectedType = $('#typeId option:selected').text().toLowerCase();
    
    if(!typeId) {
        $('#subTypeId').html('<option value="">--Select--</option>');
        return;
    }
    
    // Hide subtype field for Purchase requests
    if(selectedType.includes('purchase')) {
        $('#subTypeId').closest('div').hide();
        $('#subTypeId').html('<option value="0">--Not Required--</option>');
        $('#subTypeId').val('0'); // Set a default value for form submission
        return;
    } else {
        $('#subTypeId').closest('div').show();
    }
    
    $.get('createrequest', {
        action: 'getSubTypes',
        typeId: typeId
    })
    .done(function(data) {
        $('#subTypeId').html(data);
    });
}

function loadSubCategories() {
    const catId = $('#catId').val();
    if(!catId) return;
    
    $.get('createrequest', {
        action: 'getSubCategories',
        catId: catId
    })
    .done(function(data) {
        $('#subCatId').html(data);
        $('#materialId').html('<option value="">--Select--</option>');
        clearMaterialInfo();
    });
}

function loadMaterials() {
    const subCatId = $('#subCatId').val();
    if(!subCatId) return;
    
    $.get('createrequest', {
        action: 'getMaterials',
        subCatId: subCatId
    })
    .done(function(data) {
        $('#materialId').html(data);
        clearMaterialInfo();
    });
}

function updateStockInfo() {
    const selected = $('#materialId option:selected');
    if(!selected.val()) {
        clearMaterialInfo();
        return;
    }
    
    $('#stock').val(selected.data('stock'));
    $('#minQty').val(selected.data('min'));
}

function clearMaterialInfo() {
    $('#stock').val('');
    $('#minQty').val('');
    $('#quantity').val('1');
}

function addMaterial() {
    const selected = $('#materialId option:selected');
    if(!selected.val()) return;
    
    const quantity = parseInt($('#quantity').val());
    if(!quantity || quantity < 1) {
        alert('Please enter a valid quantity');
        return;
    }
    
    const materialId = selected.val();
    // Check if material already exists
    if($(`input[name="materialId[]"][value="${materialId}"]`).length > 0) {
        alert('This material is already added');
        return;
    }

    // Show warnings for export requests about stock levels
    const requestType = $('#typeId option:selected').text().toLowerCase();
    if(requestType.includes('export')) {
        const stock = parseInt($('#stock').val());
        const minQty = parseInt($('#minQty').val());
        
        let warnings = [];
        if(quantity > stock) {
            warnings.push('- The requested quantity (' + quantity + ') exceeds current stock level (' + stock + ')');
        }
        if(stock - quantity < minQty) {
            warnings.push('- This export will reduce stock (' + stock + ' - ' + quantity + ' = ' + (stock - quantity) + ') below minimum quantity level (' + minQty + ')');
        }
        
        if(warnings.length > 0) {
            alert('Please note the following warnings:\n\n' + warnings.join('\n') + '\n\nYour request will need director approval.');
        }
    }
    
    const tbody = $('#tblItems tbody');
    const rowCount = tbody.children().length;
    
    tbody.append(`
        <tr>
            <td class="text-center">${rowCount + 1}</td>
            <td><img src="${selected.data('img')}" style="width:50px;height:50px mx-auto rounded"></td>
            <td class="text-center" >${selected.data('name')}</td>
            <td class="text-center" >
                <span class="quantity-display ">${quantity}</span>
                <input type="hidden" name="materialId[]" value="${materialId}">
                <input type="hidden" name="quantity[]" value="${quantity}">
            </td>
            <td class="text-center" >
                <button type="button" class="action-btn" onclick="editQuantity(this)" style="background: #ffc107; margin-right: 5px;">Edit</button>
                <button type="button" class="action-btn" onclick="$(this).closest('tr').remove(); refreshNumbers();">Remove</button>
            </td>
        </tr>
    `);
}

function refreshNumbers() {
    $('#tblItems tbody tr').each((idx, row) => {
        $(row).find('td:first').text(idx + 1);
    });
}

function resetFilter() {
    $('#catId').val('');
    $('#subCatId').html('<option value="">--All--</option>');
    $('#materialId').html('<option value="">--Select--</option>');
    clearMaterialInfo();
}

// Reset entire form to initial state
function resetFormToInitialState() {
    console.log('Resetting form to initial state...');
    
    // Reset all form fields
    $('#typeId').val('');
    $('#subTypeId').html('<option value="">--Select--</option>').closest('div').show();
    $('#catId').val('');
    $('#subCatId').html('<option value="">--All--</option>');
    $('#materialId').html('<option value="">--Select--</option>');
    $('#note').val('');
    
    // Clear material info and table
    clearMaterialInfo();
    $('#tblItems tbody').empty();
    
    console.log('Form reset completed');
}

function editQuantity(button) {
    const row = $(button).closest('tr');
    const quantityDisplay = row.find('.quantity-display');
    const quantityInput = row.find('input[name="quantity[]"]');
    const currentQty = quantityInput.val();
    
    // Create input for editing
    const editInput = $('<input>')
        .attr('type', 'number')
        .attr('min', '1')
        .val(currentQty)
        .css({
            'width': '60px',
            'padding': '3px',
            'margin-right': '5px'
        });
    
    // Create save button
    const saveBtn = $('<button>')
        .text('Save')
        .addClass('action-btn')
        .css({
            'background': '#28a745',
            'margin-right': '5px'
        });
        
    // Create cancel button
    const cancelBtn = $('<button>')
        .text('Cancel')
        .addClass('action-btn')
        .css('background', '#6c757d');
    
    // Save click handler
    saveBtn.click(function() {
        const newQty = parseInt(editInput.val());
        if(!newQty || newQty < 1) {
            alert('Please enter a valid quantity');
            return;
        }

        // Check stock warnings for export requests
        const requestType = $('#typeId option:selected').text().toLowerCase();
        if(requestType.includes('export')) {
            const stock = parseInt($('#stock').val());
            const minQty = parseInt($('#minQty').val());
            
            let warnings = [];
            if(newQty > stock) {
                warnings.push('- The requested quantity (' + newQty + ') exceeds current stock level (' + stock + ')');
            }
            if(stock - newQty < minQty) {
                warnings.push('- This export will reduce stock (' + stock + ' - ' + newQty + ' = ' + (stock - newQty) + ') below minimum quantity level (' + minQty + ')');
            }
            
            if(warnings.length > 0) {
                alert('Please note the following warnings:\n\n' + warnings.join('\n') + '\n\nYour request will need director approval.');
            }
        }

        quantityDisplay.text(newQty);
        quantityInput.val(newQty);
        restoreButtons();
    });
    
    // Cancel click handler
    cancelBtn.click(restoreButtons);
    
    // Save current buttons
    const originalContent = row.find('td:last-child').html();
    
    // Replace quantity display with input
    quantityDisplay.html(editInput);
    
    // Replace action buttons
    row.find('td:last-child').html('').append(saveBtn).append(cancelBtn);
    
    // Focus input
    editInput.focus();
    
    // Function to restore original buttons
    function restoreButtons() {
        quantityDisplay.html(quantityInput.val());
        row.find('td:last-child').html(originalContent);
    }
}

function validateForm() {
    if(!$('#typeId').val()) {
        alert('Please select a Request Type');
        return false;
    }
    
    // SubType is not required for Purchase requests
    const selectedType = $('#typeId option:selected').text().toLowerCase();
    if(!selectedType.includes('purchase') && (!$('#subTypeId').val() || $('#subTypeId').val() === '0')) {
        alert('Please select a SubType');
        return false;
    }
    
    if($('#tblItems tbody tr').length === 0) {
        alert('Please add at least one material');
        return false;
    }
    return true;
}
