# Car Backend – Resilienz- & Performance-Tests

Dieses Projekt stellt einen resilienten REST-Endpunkt für Autos bereit (`/cars/resilient`) und verwendet Kafka für Validierungslogik. Es enthält eine vollständige Testinfrastruktur mit Docker Compose und JMeter.

---

## 🚀 Schnellstart

### 1. Voraussetzungen

- Docker & Docker Compose
- Java 17+
- Maven
- JMeter (z. B. via Homebrew oder manuell)

---

## 🐳 Docker Umgebung starten

```bash
docker-compose up --build
```

Die folgenden Container werden dabei gestartet:

- `car-backend`: Das Quarkus Backend mit resilientem Endpunkt.
- `text-validation`: Der Kafka-Consumer zur Textvalidierung.
- `car-db`: PostgreSQL-Datenbank.
- `redpanda-1`: Kafka Broker (Redpanda).

Das Backend ist erreichbar unter:  
[http://localhost:8080](http://localhost:8080)

---

## 🧪 REST-API testen

### Resilienter Endpunkt
```bash
curl "http://localhost:8080/cars/resilient?simulateDelay=true"
```

Wenn die Simulation aktiviert ist, wird ein künstlicher Delay ausgelöst und der Fallback greift.

---

## 📈 JMeter Tests ausführen

### 1. Ausführung eines Lasttests mit Report-Erzeugung

```bash
chmod +x jmeter/run-jmeter-test.sh
./jmeter/run-jmeter-test.sh
```

Das Skript führt folgendes aus:

- Ruft das JMX-Testplan `CarApiResilienceTest.jmx` auf
- Erstellt eine Ergebnisdatei `results-<timestamp>.jtl`
- Generiert einen HTML-Report unter `report-<timestamp>/`

### 2. Report ansehen

Nach der Ausführung öffne den Report in deinem Browser:

```bash
open report-<timestamp>/index.html
# oder unter Windows:
start report-<timestamp>/index.html
```

---

## 🔧 Projektstruktur

```
car-backend/
│
├── jmeter/
│   ├── CarApiResilienceTest.jmx         # JMeter Testplan
│   ├── car-loadtest.jmx                 # Alternativer Loadtest
│   └── run-jmeter-test.sh               # Test Runner mit Report-Erstellung
│
├── src/                                 # Java Sourcecode (Quarkus Backend)
├── pom.xml                              # Maven Build-Datei
└── docker-compose.yml                   # Infrastruktur
```

---

## 📋 Hinweise

- Stelle sicher, dass Port `8080`, `5432` und `9092` frei sind.
- Die Kafka-Verbindung ist auf `localhost:9092` ausgelegt.
- Für persistente Tests verwende `simulateDelay=true` in der Query.

---

## 🧹 Cleanup

```bash
docker-compose down -v
```

---

## 📬 Kontakt

Für Rückfragen oder Hilfe: `r33bickard` via GitHub
