# Schach

Dokumentation Überblick

[[_TOC_]]

## Vorgehensplan

Der [Vorgehensplan](documentation/Vorgehensplan.pdf) beinhaltet aktuelle To-dos und einen Überblick der geplanten Features.

## Anleitung

Die [Anleitung](documentation/Bedienungsanleitung.pdf) beinhaltet einen einfachen Einstieg für die Verwendung unseres Programmes.

## Architektur

Das [Architektur-Dokument](documentation/Architekturdokumentation.pdf) stellt die Architektur des Systems aus Nutzersicht sowie aus statischer und dynamischer Sicht dar.

## StoryCards

Die [Storycards](documentation/Anforderungsdokumentation/Story%20Cards) zeigen die Anwendungsfälle bzw. Funktionen des Projekts auf.

## Diagramme

Das [UML-Klassen-Diagramm](documentation/Diagramme/Klassendiagramm.png) bietet eine Übersicht über das Zusammenspiel der einzelnen Klassen.

Das [Use-Case-Diagramm](documentation/Anforderungsdokumentation/Use-Case_Diagramm.png) stellt die Leistungen des Systems aus Benutzersicht dar.

Der [Programmablaufplan](documentation/Diagramme/Programmablaufplan.png) stellt den grundlegenden Ablauf des Programms dar.

## Statische Variablen

Identifikation der Figuren:

| Figuren | ID |
| ---       |  ------  |
| Bauer     | 1        |
| Turm      | 2        |
| Springer  | 3        |
| Läufer    | 4        |
| Dame      | 5        |
| König     | 6        |

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

![Bildtext](documentation/images/ReadMe_banner.jpg "Banner")