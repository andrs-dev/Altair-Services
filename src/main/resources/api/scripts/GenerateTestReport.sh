#!/bin/bash

# Obtener el directorio del script
SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
export TZ=UTC

MONTH=$(date +"%b" | tr '[:lower:]' '[:upper:]')
DAY=$(date +"%d")
TIME=$(date +"%H%M")

newman run "$SCRIPT_DIR/../Test.postman_collection.json" -r htmlextra --reporter-htmlextra-export "$TIME-CollectionName-Test-$DAY-$MONTH.html"
