<div class="bg-white p-6 rounded shadow">
  <h2 class="text-xl font-bold mb-4">Inventory Check for: ${material.materialName}</h2>

  <form method="post" action="inventory-check">
    <input type="hidden" name="materialId" value="${material.materialId}"/>

    <div class="grid grid-cols-1 gap-4 md:grid-cols-3">
      <div>
        <label class="block text-sm font-medium text-gray-700">New</label>
        <input type="number" name="newQty" value="${inventoryMap['New']}" class="w-full border rounded px-3 py-2"/>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700">Used</label>
        <input type="number" name="usedQty" value="${inventoryMap['Used']}" class="w-full border rounded px-3 py-2"/>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700">Damaged</label>
        <input type="number" name="damagedQty" value="${inventoryMap['Damaged']}" class="w-full border rounded px-3 py-2"/>
      </div>
    </div>

    <div class="mt-6">
      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700">
        Save Inventory
      </button>
    </div>
  </form>
</div>
