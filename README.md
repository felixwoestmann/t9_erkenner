# asp_crawler

Der Crawler kann einen Baum mit Texten füttern um so die Wahrscheinlichkeit von Buchstbenfolgen zu berechnen.
Dieser kann dann von T9 Bot verwendet werden.

Die aktuelle Konfiguration lädt ein Verzeichniss welches mittels [wikiextractor](https://github.com/attardi/wikiextractor) aus einem Wikipedia XML Data Dump erzeugt wurde.
*Ein mit dem Wikiextractor bearbeiteter Dumb liegt als tar.bz2 [in meinem Drive](https://drive.google.com/open?id=0B7TjX_lUHxQgVnhxRmJOYXhQd1U) Zum entpacken bietet sich pbzip2 an, das ist Multi Threaded. Der Pfad zum Directory des Dumbs muss geändert werden.* 

Es lernt damit den Baum in 5er Chunks an und schreibt ihn in eine JSON Datei. Der Pfad zu dieser Datei muss angepasst werden.


## ToDo
### Höhere Priorität
[ ]	Titel: Optimiere Schreib Prozess des JSON
	Beschreibung: Aktuell wird der Baum in 5er Chunks gefüttert. Sollen die Chunks größer sein hat Java (Aufgrund der erhöhten Anzahl von Knoten im Baum) nicht genügend HEAP.
	Das ist Problematisch, wenn wir die Größe der Chunks optimieren wollen.
	Die Methode sollte daher den Baum in Teilen verarbeiten können.   
