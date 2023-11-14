const newman = require('newman');

const collectionPath = '../../api/Test.postman_collection.json';
const reportPath = '../../api/reports';
const date = new Date();
const months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
let day = date.getDay().toString();
let month = months[date.getMonth()].substring(0,3);
let hours = date.getHours().toString();
let minutes = date.getMinutes().toString();
let time = hours + minutes;

const options = {
    collection: collectionPath,
    reporters: ['htmlextra'],
    iterationCount: 1,
    reporter: {
        htmlextra: {
            export: `${reportPath}/${time}-CollectionName-Test-${month}-${day}.html`,
            logs: true,
            testPaging: true,
            browserTitle: "Reporte - Altair",
            title: "Estado de los servicios",
            titleSize: 4,
            skipSensitiveData: true,
            showFolderDescription: true,
            timezone: "America/Bogota",
            displayProgressBar: true
        }
    }
};

newman.run(options, (err) => {
    if (err) {
        console.error('Error ejecutando la colección:', err);
        console.error('Detalles del error:', err.message, err.stack);
        process.exit(1);
    }
    console.log('La colección se ejecutó correctamente.');
    process.exit(0);
});