#!/bin/bash

# Obtener el directorio del script
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")

# Obtener el crontab actual del usuario
crontab -l > mycron

# Agregar nuevas entradas al archivo temporal
echo "27 17 * * * $SCRIPT_DIR/../GenerateTestReport.sh" >> mycron
echo "30 10 * * * $SCRIPT_DIR/../GenerateTestReport.sh" >> mycron
echo "00 13 * * * $SCRIPT_DIR/../GenerateTestReport.sh" >> mycron
echo "30 16 * * * $SCRIPT_DIR/../GenerateTestReport.sh" >> mycron

# Establecer el crontab actualizado
crontab mycron

# Eliminar el archivo temporal
rm mycron
