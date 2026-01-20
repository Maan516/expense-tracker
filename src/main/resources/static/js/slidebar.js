document.addEventListener("DOMContentLoaded", function () {

    /* ===============================
       SIDEBAR TOGGLE (MOBILE)
    =============================== */
    const toggleBtn = document.getElementById("sidebarToggle");
    const sidebar = document.getElementById("sidebar");

    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener("click", () => {
            sidebar.classList.toggle("sidebar-open");
        });
    }

    /* ===============================
       SIDEBAR SUBMENU TOGGLE
    =============================== */
    const submenuToggles = document.querySelectorAll(".submenu-toggle");

    submenuToggles.forEach(toggle => {
        toggle.addEventListener("click", function (e) {
            e.preventDefault(); // stop page jump
            const parent = this.closest(".has-submenu");
            parent.classList.toggle("open");
        });
    });

    /* ===============================
       AUTO HIDE TOAST (SUCCESS / ERROR)
    =============================== */
    const toast = document.getElementById("loginSuccessToast")
              || document.getElementById("successToast")
              || document.getElementById("errorToast");

    if (toast) {
        setTimeout(() => {
            toast.style.opacity = "0";
            setTimeout(() => toast.remove(), 400);
        }, 3500);
    }

});
