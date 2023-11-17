let htmlFileAnchor = document.getElementsByClassName("htmlFileAnchor");

// LATEST REPORT
let namelatestReport = document.getElementById("namelatestReport");
let dateLatestReport = document.getElementById("dateLatestReport");
let testsLastReport = document.getElementById("testsLastReport");
let failuresLastReport = document.getElementById("failuresLastReport");
let errorsLastReport = document.getElementById("errorsLastReport");
let statusLastReport = document.getElementById("statusLastReport");
let timeLastReport = document.getElementById("timeLastReport");

// CARD
let containerLastReport = document.getElementById("lr-container");
let headerLastReport = document.getElementById("lr-header");

// HTML
fetch('updated-file')
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error(`Error al obtener el último archivo. Código de estado: ${response.status}`);
        }
    })
    .then(data => {
        console.log(data);

        if ("error" in data) {
            console.error(data.error);
        } else {
            namelatestReport.innerText = data.fileName;
            dateLatestReport.innerText = data.fileDate;
            //console.log(data.htmlFilePath);
            for (let i = 0; i < htmlFileAnchor.length; i++) {
                htmlFileAnchor[i].setAttribute("href", data.htmlFilePath);
            }


            // XML
            fetch(data.xmlFilePath)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Ha ocurrido un error cargando un archivo.');
                    }
                    return response.text();
                })
                .then(xmlContent => {
                    const parser = new DOMParser();
                    const xmlDoc = parser.parseFromString(xmlContent, 'application/xml');
                    console.log(xmlDoc);
                    const testsuite = xmlDoc.getElementsByTagName('testsuite');
                    const testsuites = xmlDoc.getElementsByTagName('testsuites');

                    let failuresCount = 0;
                    let errorsCount = 0;
                    for (let i = 0; i < testsuite.length; i++) {
                        const failureIterator = parseInt(testsuite[i].getAttribute("failures").toString());
                        const errorIterator = parseInt(testsuite[i].getAttribute("errors").toString());
                        failuresCount += failureIterator;
                        errorsCount += errorIterator;
                    }
                    testsLastReport.innerText = testsuites[0].getAttribute("tests").toString();
                    failuresLastReport.innerText = failuresCount.toString();
                    errorsLastReport.innerText = errorsCount.toString();
                    timeLastReport.innerText = testsuites[0].getAttribute("time").toString();

                    if (failuresCount <= 3 && errorsCount === 0) {
                        statusLastReport.innerText = "Funcionando";
                        containerLastReport.classList.add("border-success");
                        headerLastReport.classList.add("text-bg-success");
                        for (let i = 0; i < htmlFileAnchor.length; i++) {
                            htmlFileAnchor[i].classList.add("text-bg-success")
                        }
                    } else if (failuresCount > 3 && failuresCount <= 10 && errorsCount === 0) {
                        statusLastReport.innerText = "Con Algunos Defectos"
                        containerLastReport.classList.add("border-warning");
                        headerLastReport.classList.add("text-bg-warning")
                        for (let i = 0; i < htmlFileAnchor.length; i++) {
                            htmlFileAnchor[i].classList.add("text-bg-warning")
                        }
                    } else {
                        statusLastReport.innerText = "Fallando";
                        containerLastReport.classList.add("border-danger")
                        headerLastReport.classList.add("text-bg-danger")
                        for (let i = 0; i < htmlFileAnchor.length; i++) {
                            htmlFileAnchor[i].classList.add("text-bg-danger")
                        }
                    }
                }).catch(error => {
                console.error('Error al cargar el archivo XML:', error);
            });
        }
    })
    .catch(error => console.error("Error al obtener el último archivo:", error));

