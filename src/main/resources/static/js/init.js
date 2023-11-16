let htmlFileAnchor = document.getElementsByClassName("htmlFileAnchor");
let namelatestReport = document.getElementById("namelatestReport");
let dateLatestReport = document.getElementById("dateLatestReport");

fetch("/updated-file")
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
            console.log(data.htmlFilePath);
            for (let i = 0; i < htmlFileAnchor.length; i++) {
                htmlFileAnchor[i].setAttribute("href", data.htmlFilePath);
            }
        }
    })
    .catch(error => console.error("Error al obtener el último archivo:", error));

fetch('/updated-file')
    .then(response => response.text())
    .then(data => {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(data.xmlFilePath, 'application/xml');

        // Obtener el valor del atributo 'tests' en el elemento 'testsuite'
        const testCount = xmlDoc.querySelector('testsuite').getAttribute('failures');
        console.log('Valor del atributo tests:', testCount);

        // Obtener el valor del atributo "failures" en el elemento "testsuite"
        const failuresCount = xmlDoc.querySelector('testsuite').getAttribute('errors');
        console.log('Valor del atributo failures:', failuresCount);
    })
    .catch(error => {
        console.error('Error al cargar el archivo XML:', error);
    });

