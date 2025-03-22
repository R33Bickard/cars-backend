# Car Management System - Messaging & Streaming

## Projektübersicht

Dieses Projekt demonstriert die Verwendung von Quarkus in einem verteilten Messaging & Streaming-System mit Kafka. Es besteht aus zwei Microservices. Der car-backend service ist an einer Datenbank angebunden.

###  Architektur

- **Car Backend**: 
  - Verwalten von Autos
  - Sendet Validierungsanfragen über Kafka an den `text-validation-service`
  - Speichert validierte/nicht validierte Autos in der Datenbank
  
- **Text Validation Service**: 
  - Empfängt Nachrichten aus `validation-requests`
  - Validiert den Namen und die Beschreibung eines Autos
  - Sendet die Validierungsantwort zurück über `validation-responses-out`

- **Kafka**:
  - Verwendet zwei Topics:
    - `validation-requests`: Zur Kommunikation vom Backend zum Validator
    - `validation-responses-out`: Zur Rückmeldung der Validierungsergebnisse

---

## Schnellstart mit Docker

Das gesamte System kann mit Docker Compose gestartet werden. (Docker Compose-File ist im Projekt enthalten)

### Repository klonen

```sh
git clone git@github.com:YourUsername/car-management-system.git
cd car-management-system
```

### Docker Images bauen oder herunterladen

Die Container-Images können entweder lokal gebaut oder von einer Registry bezogen werden:

#### Lokaler Build:
```sh
# car-backend bauen
cd car-backend
./mvnw package -Dquarkus.container-image.build=true
cd ..

# text-validation-service bauen
cd text-validation-service
./mvnw package -Dquarkus.container-image.build=true
cd ..
```

### Dienste mit Docker Compose starten

```sh
docker-compose up -d
```

Dadurch werden folgende Dienste gestartet:
- Car Backend (car-backend)
- Text Validation Service (text-validation-service)
- Kafka (redpanda)
- PostgreSQL (Datenbank für car-backend)

### Dienste stoppen
```sh
docker-compose down
```

---

## Entwicklungsmodus

Zum Entwickeln kannst du die Services lokal starten:

### Car Backend starten

```sh
cd car-backend
./mvnw compile quarkus:dev
```

Der Service ist dann unter [http://localhost:8080](http://localhost:8080) erreichbar.
Die Swagger UI findest du unter [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui).

### Text Validation Service starten

```sh
cd text-validation-service
./mvnw compile quarkus:dev
```

Der Service ist dann unter [http://localhost:8082](http://localhost:8082) erreichbar (wenn du den Port nicht geändert hast).

---

## Endpunkte

1. ***GET /cars*** 
- Gibt eine Liste aller gespeicherten Autos zurück.

2. ***POST /cars***
- Speichert ein neues Auto in der Datenbank und sendet eine Validierungsanfrage an den text-validation-service
- validated wird zu diesem Zeitpunkt immer "false" sein

3. ***GET /cars/validated***
- Gibt eine Liste aller validierten Autos zurück.

4. ***DELETE /cars/{id}***
- Löscht ein Auto anhand der ID aus der Datenbank.

## Validierungsregeln

Der ValidationService stellt sicher, dass die Daten eines Autos bestimmte Anforderungen erfüllen, bevor sie als gültig betrachtet werden. Die folgenden Validierungsfälle werden abgedeckt:

### Name-Validierung
- Falls der Name eines Autos eines der folgenden Wörter enthält, schlägt die Validierung fehl:
    - kaputt, defekt, schrott, müll, unfall, böse

### Beschreibung-Validierung
- Falls die Beschreibung Zahlen (0–9) enthält, schlägt die Validierung fehl.


## Anwendungsfälle

### Auto erstellen (mit korrekter Validierung)

Request: 
```sh
curl -X POST http://localhost:8080/cars \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Ford Fiesta",
           "description": "Ich bin ein korrektes Auto"
         }'
```

### Auto erstellen (mit misslungener Namensvalidierung) 

Request: 
```sh
curl -X POST http://localhost:8080/cars \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Schrott",
           "description": "Ich bin ein unvalidiertes Auto"
         }'
```

### Auto erstellen (mit misslungener Beschreibungsvalidierung) 

Request: 
```sh
curl -X POST http://localhost:8080/cars \
     -H "Content-Type: application/json" \
     -d '{
           "name": "BMW",
           "description": "Ich bin 150 PS stark, daher validated = false"
         }'
```

### Liste aller Autos abrufen

Request: 
```sh
curl -X GET http://localhost:8080/cars
```

### Liste aller validierten Autos abrufen

Request: 
```sh
curl -X GET http://localhost:8080/cars/validated
```

### Auto löschen

Request:
```sh
curl -X DELETE http://localhost:8080/cars/1
```

## Projektstruktur

```
car-management-system/
├── car-backend/                     # Backend-Service für Auto-Verwaltung
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── model/
│   │   │   │   │   └── Car.java     # Entity-Klasse für Autos
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── service/
│   │   │   │   │   └── CarService.java  # Business-Logik
│   │   │   │   └── CarResource.java     # REST-Endpunkte
│   │   │   └── resources/
│   │   │       └── application.properties  # Konfiguration
│   │   └── test/                    # Tests
│   └── pom.xml                      # Maven-Konfiguration
│
├── text-validation-service/         # Service zur Text-Validierung
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   └── service/
│   │   │   │       └── ValidationService.java  # Validierungslogik
│   │   │   └── resources/
│   │   │       └── application.properties  # Konfiguration
│   │   └── test/                    # Tests
│   └── pom.xml                      # Maven-Konfiguration
│
├── docker-compose.yml               # Docker Compose Konfiguration
└── README.md                        # Diese Datei
```

## Technologien

- **Quarkus**: Supersonic Subatomic Java Framework
- **Kafka**: Event-Streaming-Plattform (hier implementiert mit Redpanda)
- **PostgreSQL**: Relationale Datenbank
- **Docker & Docker Compose**: Container-Management
- **Maven**: Build-Tool und Dependency-Management
- **Java 17**: Programmiersprache

## Weiterentwicklung

Ideen für zukünftige Erweiterungen:

1. Frontend-Anwendung hinzufügen (z.B. mit Angular, React oder Vue)
2. Erweiterte Auto-Eigenschaften (Marke, Modell, Baujahr, Preis)
3. Benutzerauthentifizierung implementieren
4. Mehr Validierungsregeln hinzufügen
5. Testabdeckung verbessern
