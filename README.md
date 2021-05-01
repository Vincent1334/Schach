# Schach

Dokumentation überblick

[[_TOC_]]

## Vorgehensplan

Der Vorgehensplan beinhaltet aktuelle ToDos und einen überblick der geplanten Features.

[Vorgehensplan](documentation/Vorgehensplan.pdf)

## Anleitung

Die Anleitung beinhaltet einen einfachen Einstieg für die Verwendung unseres Programmes.

[Spielanleitung](documentation/Anleitung.pdf)

## StoryCards

[Storycards](documentation/Story%20Cards)

## Diagramme

[Klassen-Diagramm](documentation/uml-class-diagramm.png)
[UML-Diagramm](documentation/schach-git.png)

## Statische Werte

Interne verteilung der Teams:

| Team | ID |
| ---      |  ------  |
| Weiß     | 0        |
| Schwarz  | 1        |
| None     | 2        |

Interne Verteilung der Figuren

| Figuren | ID |
| ---       |  ------  |
| Bauer     | 1        |
| Turm      | 2        |
| Springer  | 3        |
| Läufer    | 4        |
| Dame      | 5        |
| König     | 6        |


# Maven

Kurzübersicht nützlicher Maven-Befehle. Weitere Informationen finden sich im Tutorial:

* `mvn clean` löscht alle generierten Dateien
* `mvn compile` übersetzt den Code
* `mvn javafx:jlink` packt den gebauten Code als modulare Laufzeit-Image. Das Projekt kann danach gestartet werden mit `target/chess/bin/chess`
* `mvn test` führt die Tests aus
* `mvn compile site` baut den Code, die Dokumentation und die Tests und führt alle Tests, sowie JaCoCo und PMD inklusive CPD aus. Die Datei `target/site/index.html` bietet eine Übersicht über alle Reports.
* `mvn javafx:run` führt das Projekt aus
* `mvn javafx:run -Dargs="--no-gui"` führt das Projekt mit Command-Line-Parameter `--no-gui` aus.

![Bildtext](documentation/images/ReadMe_banner.jpg "Banner")