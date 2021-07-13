# Schach

Dokumentation Überblick

[[_TOC_]]

## Vorgehensplan

Der [Vorgehensplan](documentation/4_Vorgehensplan.pdf) beinhaltet aktuelle To-dos und einen Überblick der geplanten Features.

## Anleitung

Die [Anleitung](documentation/2_Bedienungsanleitung.pdf) beinhaltet einen einfachen Einstieg für die Verwendung unseres Programmes.

## Architektur

Das [Architektur-Dokument](documentation/3_Architekturdokumentation.pdf) stellt die Architektur des Systems aus Nutzersicht sowie aus statischer und dynamischer Sicht dar.

## Anforderungsdokumentation

Die [Anforderungsdokumentation](documentation/1_Anforderungsdokumentation.pdf) zeigen die Anwendungsfälle bzw. Funktionen des Projekts auf.

## Statische Variablen

Identifikation der Figuren:

| Figuren | ID | symbol white | symbol black |
| :---:       |  :------:  |  :------:  |  :------:  |
| Bauer     | 1        | P        | p        |
| Turm      | 2        | R        | r        |
| Springer  | 3        | N        | n        |
| Läufer    | 4        | B        | b        |
| Dame      | 5        | Q        | q        |
| König     | 6        | K        | k        |

Diese festen Zuordnungen werden von einigen Teilen des Programms verwendet, um Figuren einordnen zu können.

# Maven

Kurzübersicht nützlicher Maven-Befehle. Weitere Informationen finden sich im Tutorial:

* `mvn clean` löscht alle generierten Dateien
* `mvn compile` übersetzt den Code
* `mvn javafx:jlink` packt den gebauten Code als modulare Laufzeit-Image. Das Projekt kann danach gestartet werden. Mit `target/chess/bin/chess`
* `mvn test` führt die Tests aus
* `mvn compile site` baut den Code, die Dokumentation und die Tests und führt alle Tests, sowie JaCoCo und PMD inklusive CPD aus. Die Datei `target/site/index.html` bietet eine Übersicht über alle Reports.
* `mvn javafx:run` führt das Projekt aus
* `mvn javafx:run -Dargs="--no-gui"` führt das Projekt mit Command-Line-Parameter `--no-gui` aus.

![Bildtext](images/ReadMe_banner.jpg "Banner")