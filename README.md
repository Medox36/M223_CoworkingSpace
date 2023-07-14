# Prüfungsprojekt: Coworking Space API

Die Coworking Space API ist ein Verwaltungssystem, zum reservieren und verwalten eines Coworking Spaces.  
Das projekt wurde/wird mit quarkus entwickelt.

## Erste Schritte und aufsetzen

1. Erstelle eine Kopie (fork) von diesem Projekt.
2. Stelle sicher, dass Docker installiert ist und läuft.
3. Stelle sicher, dass Visual Studio Code und die Erweiterung Remote Container installiert ist.
4. Klone (clone) das Projekt lokal, um damit arbeiten zu können.
5. Öffne das Projekt mit Visual Studio Code.
6. Öffne das Projekt im Entwicklungscontainer.

## Projekt starten

1. Öffne eine Kommandozeile im Proketordner.
2. Starte das Projekt mit dem Kommando `./mvnw quarkus:dev`
3. Die Applikation ist nun unter http://localhost:8080 verfügbar.
4. Die API kan auf http://localhost:8080/q/swagger-ui/ eingesehen werden.

## Datenbank

Die Daten werden in einer PostgreSQL-Datenbank gespeichert. In der Entwicklungsumgebung wird diese in der [docker-compose-yml](./.devcontainer/docker-compose.yml) konfiguriert.

### Datenbankadministration

Über http://localhost:5050 ist PgAdmin4 erreichbar. Damit lässt sich die Datenbank komfortabel verwalten. 
Der Benutzername lautet `zli@example.com` und das Passwort `zli*123`.  

Die Verbindung zur PostgreSQL-Datenbank muss zuerst mit folgenden Daten konfiguriert werden:
 - Host name/address: `db`
 - Port: `5432`
 - Maintenance database: `postgres`
 - Username: `postgres`
 - Password: `postgres`

## Automatische Tests

Die automatischen Tests können mit `./mvnw quarkus:test` ausgeführt werden. Für die automatischen Tests wird nicht die PostgreSQL-Datenbank verwendet, sondern eine H2-Datenbank, welche sich im Arbeitsspeicher während der Ausführung befindet.
Die Testdaten befinden sich in der Klasse `ch.giuntini.coworkingspace.TestDataservice`, im Ordner `src/test/java/`.  
Die Testdaten werden automatisch geladen wenn die automatischen testsausgeführt werden.

## Änderungen der Endpunkte

Hier folgt eine Aufzählung der veränderten Endpunkte welche von der Planung der Endpunkte abweichen.

GET /user/{id}
- Erfolg:
  - 200: Benutzer gefunden
- Fehler:
  - 401: Nicht angemeldet
  - 404: Benutzer nicht gefunden

GET /booking
- Erfolg:
  - 200: Alle Buchungen gefunden
- Fehler:
  - 401: Nicht angemeldet
  - 403: Kein Zugriff für Mitglieder
  - 404: Benutzer nicht gefunden

GET /booking/my
- Erfolg:
  - 200: Alle Buchungen des Benutzers gefunden
- Fehler:
  - 401: Nicht angemeldet

POST /booking
- Erfolg:
  - 201: Buchung erstellt
- Fehler:
  - 400: Ungültige Daten
  - 401: Nicht angemeldet

PUT /booking/{id}
- Erfolg:
  - 200: Buchung überarbeitet
- Fehler:
  - 400: Ungültige Daten
  - 400: Nur Admins können ein Wunsch-Feedback hinzufügen
  - 401: Nicht angemeldet
  - 403: Nicht Besitzer der Buchung
  - 403: Eine bereits bestätigte Buchung kann kein Wunsch hinzugefügt werden
 
PUT /booking/decline/{id}
- Erfolg:
  - 200: Buchung abgelehnt
- Fehler:
  - 401: Nicht angemeldet
  - 403: Kein zugriff für Mitglieder
  - 404: Keine Buchung mit passender ID gefunden
 
DELETE /booking/cancel/{id}
- Erfolg:
  - 200: Buchung storniert
- Fehler:
  - 400: Buchung in der vergangenheit
  - 401: Nicht angemeldet
  - 403: Nicht Besitzer der Buchung
  - 404: Keine Buchung mit passender ID gefunden
