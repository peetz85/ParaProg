package client;

public class GraphPaul {

    // ----------------------------------------------
    // Konstruktoren
    // ----------------------------------------------

    GraphPaul() {
	m_knotenNamen = null;
	m_matrix = null;
    }

    GraphPaul(String[] knotenNamen) {
	m_knotenNamen = knotenNamen;
	m_matrix = new boolean[knotenNamen.length][knotenNamen.length];
    }

    // ----------------------------------------------
    // Member
    // ----------------------------------------------

    private boolean[][] m_matrix;
    private String[] m_knotenNamen;

    // ----------------------------------------------
    // Methoden
    // ----------------------------------------------

    // Setze Verbindung zwischen Knoten mit Namen s1 und s2
    boolean verbindeKnoten(String s1, String s2) {
	if (m_knotenNamen == null)
	    return false;
	int s1Index = -1, s2Index = -1;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s1))
		s1Index = i;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s2))
		s2Index = i;
	if (s1Index == -1 || s2Index == -1)
	    return false;
	m_matrix[s1Index][s2Index] = true;
	m_matrix[s2Index][s1Index] = true;
	return true;
    }

    boolean sindKnotenVerbunden(String s1, String s2) {
	if (m_knotenNamen == null)
	    return false;
	int s1Index = -1, s2Index = -1;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s1))
		s1Index = i;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s2))
		s2Index = i;
	if (s1Index == -1 || s2Index == -1)
	    return false;
	if (m_matrix[s1Index][s2Index] && m_matrix[s2Index][s1Index])
	    return true;
	else
	    return false;
    }

    // Prüfe ob ein Knoten mit Namen s1 in Graph existiert
    boolean istKnotenInGraph(String s1) {
	if (m_knotenNamen == null)
	    return false;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s1))
		return true;
	return false;
    }

    // Füge einen neuen Knoten mit Namen s1 in Graph ein
    boolean addKnoten(String s1) {
	// Graph noch leer
	if (m_knotenNamen == null) {
	    m_matrix = new boolean[1][1];
	    m_knotenNamen = new String[] { s1 };
	    return true;
	}
	// Knoten schon drin? → Ende
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    if (m_knotenNamen[i].equals(s1))
		return false;
	boolean[][] matrixNeu = new boolean[m_matrix.length + 1][m_matrix.length + 1];
	String[] knotenNamenNeu = new String[m_matrix.length + 1];
	// Kopiere Matrix
	for (int zeile = 0; zeile < m_matrix.length; ++zeile)
	    for (int spalte = 0; spalte < m_matrix.length; ++spalte)
		matrixNeu[zeile][spalte] = m_matrix[zeile][spalte];
	// Kopiere Namen
	for (int i = 0; i < m_matrix.length; ++i)
	    knotenNamenNeu[i] = m_knotenNamen[i];
	knotenNamenNeu[knotenNamenNeu.length - 1] = s1;
	m_knotenNamen = knotenNamenNeu;
	m_matrix = matrixNeu;
	return true;
    }

    // To String
    public String toString() {
	return druckeGraphAlsMatrix();
    }

    // Graphen ausgeben (als Adjazenzmatrix)
    public String druckeGraphAlsMatrix() {
	// Ermittle Länge des längsten Namen
	int maxStrLen = 0;
	for (int i = 0; i < m_knotenNamen.length; ++i)
	    maxStrLen = Math.max(maxStrLen, m_knotenNamen[i].length());
	String out = "";
	// Alle Namen zentrieren
	String[] namenZentriert = new String[m_knotenNamen.length];
	for (int i = 0; i < m_knotenNamen.length; ++i) {
	    namenZentriert[i] = "";
	    int lenName = m_knotenNamen[i].length();
	    for (int j = 0; j < (maxStrLen - lenName) / 2; ++j) {
		namenZentriert[i] += " ";
	    }
	    namenZentriert[i] += m_knotenNamen[i];
	}
	// Spaltenköpfe ausgeben
	out += String.format("%-" + maxStrLen + "s|", "");
	for (int nr = 0; nr < m_matrix.length; ++nr)
	    out += String.format("%-" + maxStrLen + "s|", namenZentriert[nr]);
	out += "\n";
	// Querstrich unter ersten Zeile ausgeben
	String nameStrich = "";
	for (int i = 0; i < maxStrLen; ++i)
	    nameStrich += "–"; // Richtige länge für den Querstrich pro Namen
	out += String.format("%-" + maxStrLen + "s|", nameStrich);
	for (int nr = 0; nr < m_matrix.length; ++nr)
	    out += String.format("%-" + maxStrLen + "s|", nameStrich);
	out += "\n";
	// Zentriertes Zeichen für Verbunden erstellen
	String OK = "";
	for (int i = 0; i < maxStrLen / 2; ++i)
	    OK += " "; // Halbe Maximale Wortlänge davor als Space einfügen
	OK += "x";
	// Zeilenweise die Tabelle ausgeben
	for (int zeile = 0; zeile < m_matrix.length; ++zeile) {
	    out += String
		    .format("%-" + maxStrLen + "s|", namenZentriert[zeile]);
	    for (int spalte = 0; spalte < m_matrix.length; ++spalte)
		out += (m_matrix[zeile][spalte] ? String.format("%-"
			+ maxStrLen + "s|", OK) : String.format("%-"
			+ maxStrLen + "s|", " "));
	    out += "\n";
	}
	return out;
    }

}
