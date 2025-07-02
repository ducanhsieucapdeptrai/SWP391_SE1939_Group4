<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="p-6 bg-gray-100 min-h-screen">
    <div class="max-w-4xl mx-auto bg-white p-8 rounded shadow-md">
        <h1 class="text-2xl font-bold mb-6 text-center text-blue-700">Add New User</h1>

        <c:if test="${not empty errorMessage}">
            <div class="mb-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/add-user" method="post" enctype="multipart/form-data"
              class="grid grid-cols-1 md:grid-cols-2 gap-6">

            <!-- Left Column -->
            <div>
                <label class="block mb-1 font-medium text-gray-700">Full Name</label>
                <input type="text" name="fullName" class="w-full border px-3 py-2 rounded" required autocomplete="off"/>

                <label class="block mt-4 mb-1 font-medium text-gray-700">Email</label>
                <input type="email" name="email" class="w-full border px-3 py-2 rounded" required autocomplete="off"/>

                <label class="block mt-4 mb-1 font-medium text-gray-700">Phone</label>
                <input type="text" name="phone" class="w-full border px-3 py-2 rounded" required minlength="9" maxlength="11"/>

                <label class="block mt-4 mb-1 font-medium text-gray-700">Password</label>
                <input type="password" name="password" class="w-full border px-3 py-2 rounded" required autocomplete="new-password"/>
            </div>

            <!-- Right Column -->
            <div>
                <label class="block mb-1 font-medium text-gray-700">Avatar</label>
                <div class="relative w-full">
                    <!-- Choose File Button -->
                    <label id="chooseFileLabel" for="avatar"
                           class="block w-full border px-3 py-2 rounded bg-white text-gray-700 cursor-pointer text-center">
                        Choose File
                    </label>
                    <input type="file" name="avatar" id="avatar" accept="image/*"
                           class="hidden" required onchange="handleFileChange(event)">

                    <img id="avatarPreview" src="#" alt="Preview"
                         class="hidden mt-2 object-cover border rounded" style="width: 170px; height: 113px;"/>

                    <button type="button" id="clearAvatarBtn"
                            onclick="clearAvatar()"
                            class="hidden absolute top-0 right-0 text-xl text-black hover:text-gray-700">
                        &times;
                    </button>
                </div>

                <label class="block mt-4 mb-1 font-medium text-gray-700">Status</label>
                <select name="status" class="w-full border px-3 py-2 rounded" required>
                    <option value="1">Active</option>
                    <option value="0">Inactive</option>
                </select>

                <label class="block mt-4 mb-1 font-medium text-gray-700">Role</label>
                <select name="roleId" class="w-full border px-3 py-2 rounded" required>
                    <c:forEach var="role" items="${roleList}">
                        <option value="${role.roleId}">${role.roleName}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Buttons -->
            <div class="col-span-2 flex justify-center gap-10 mt-6">
                <a href="${pageContext.request.contextPath}/userlist"
                   class="w-40 bg-gray-300 text-center py-2 rounded hover:bg-gray-400">Cancel</a>
                <button type="submit"
                        class="w-40 bg-blue-600 text-white py-2 rounded hover:bg-blue-700">Add User</button>
            </div>
        </form>
    </div>

    <c:if test="${not empty successMessage}">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
                                Swal.fire({
                                    icon: 'success',
                                    title: '${successMessage}',
                                    showConfirmButton: false,
                                    timer: 2000
                                }).then(() => {
                                    window.location.href = 'userlist';
                                });
        </script>
    </c:if>
</div>

<style>
    .custom-file-label:hover {
        background-color: #f0f0f0;
    }
</style>

<script>
    function handleFileChange(event) {
        const file = event.target.files[0];
        const preview = document.getElementById("avatarPreview");
        const label = document.getElementById("chooseFileLabel");
        const clearBtn = document.getElementById("clearAvatarBtn");

        if (file) {
            preview.src = URL.createObjectURL(file);
            preview.classList.remove("hidden");
            label.classList.add("hidden");
            clearBtn.classList.remove("hidden");
        }
    }

    function clearAvatar() {
        const avatarInput = document.getElementById("avatar");
        const preview = document.getElementById("avatarPreview");
        const label = document.getElementById("chooseFileLabel");
        const clearBtn = document.getElementById("clearAvatarBtn");

        avatarInput.value = "";
        preview.src = "#";
        preview.classList.add("hidden");
        clearBtn.classList.add("hidden");
        label.classList.remove("hidden");
    }
</script>
