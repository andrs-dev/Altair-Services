fetch("/updated-file")
    .then(response => response.text())
    .then(data => {
        document.getElementById("latestReportFile").innerText = data;
    })
    .catch(error => console.error("Error al obtener último archivo:", error));
