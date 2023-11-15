let headerContainer = document.querySelector(".header-container");
let confirmMessage = document.getElementById("confirmMessage");
let generarEstado = document.getElementById("generarEstado");

generarEstado.addEventListener('click', (event) => {
    event.preventDefault();

    fetch('/execute-script')
        .then((response) => {
            console.log("Solicitud ejecutada: ", response)
        })
        .catch((error) => {
            console.log("Ha ocurrido un error en la solicitud: ", error);
        });
    generarEstado.disabled = true;
    generarEstado.setAttribute("class", "btn btn-dark pe-none");
    console.log("Botón deshabilitado por dos minutos")

    setTimeout(() => {
        generarEstado.disabled = false;
        generarEstado.setAttribute("class", "btn btn-secondary pe-auto");
        console.log("botón habilitado.");
    }, 120000)
    // 600000
    confirmMessage.innerText = "El reporte se está generando.\n Vuelve a visitar la pagina en unos minutos.";
    headerContainer.appendChild(confirmMessage)
})