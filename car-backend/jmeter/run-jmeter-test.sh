#!/bin/bash

# Timestamp erstellen (z.B. 2025-03-23_1755)
timestamp=$(date +"%Y-%m-%d_%H%M%S")

# Dateinamen und Ordner definieren
jtl_file="results_$timestamp.jtl"
report_dir="report_$timestamp"

# Test ausführen
jmeter -n -t CarApiResilienceTest.jmx -l "$jtl_file" -e -o "$report_dir"

echo ""
echo "✅ Test abgeschlossen"
echo "📄 Ergebnisdatei: $jtl_file"
echo "📊 Report-Verzeichnis: $report_dir"
echo "🌐 Öffne den Report mit: file://$(pwd)/$report_dir/index.html"
