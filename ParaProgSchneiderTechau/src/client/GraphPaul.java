package client;

import java.io.Serializable;

public class GraphPaul implements Serializable {

    // ----------------------------------------------
    // Konstruktoren
    // ----------------------------------------------

    GraphPaul() {
        m_KnotenNamen = null;
        m_Matrix = null;
    }

    GraphPaul(String name) {
        this(new String[] { name });
    }

    GraphPaul(String[] knotenNamen) {
        m_KnotenNamen = knotenNamen;
        m_Matrix = new boolean[knotenNamen.length][knotenNamen.length];
    }

    // ----------------------------------------------
    // Member
    // ----------------------------------------------

    private boolean[][] m_Matrix;
    private String[] m_KnotenNamen;

    // ----------------------------------------------
    // Methoden
    // ----------------------------------------------

    // Gib alle Knotennamen als Feld zurück
    public String[] getKnotenNamen() {
        return m_KnotenNamen;
    }

    // Clone
    public GraphPaul clone() {
        GraphPaul graphKopie = new GraphPaul();
        graphKopie.addGraph(this);
        return graphKopie;
    }

    // Setze Verbindung zwischen Knoten mit Namen s1 und s2
    // (synchronized, weil Arbeiter-Thread zugreift)
    public synchronized boolean verbindeKnoten(String s1, String s2) {
        // Graph noch leer
        if (m_KnotenNamen == null)
            return false;
        int s1Index = -1, s2Index = -1;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s1))
                s1Index = i;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s2))
                s2Index = i;
        if (s1Index == -1 || s2Index == -1)
            return false;
        m_Matrix[s1Index][s2Index] = true;
        m_Matrix[s2Index][s1Index] = true;
        return true;
    }

    // Teste Verbindung
    // (synchronized, weil Arbeiter-Thread zugreift)
    public synchronized boolean sindKnotenVerbunden(String s1, String s2) {
        // Graph noch leer
        if (m_KnotenNamen == null)
            return false;
        int s1Index = -1, s2Index = -1;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s1))
                s1Index = i;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s2))
                s2Index = i;
        if (s1Index == -1 || s2Index == -1)
            return false;
        if (m_Matrix[s1Index][s2Index] && m_Matrix[s2Index][s1Index])
            return true;
        else
            return false;
    }

    // Prüfe ob ein Knoten mit Namen s1 in Graph existiert
    // (synchronized, weil Arbeiter-Thread zugreift)
    public synchronized boolean istKnotenInGraph(String s1) {
        // Graph noch leer
        if (m_KnotenNamen == null)
            return false;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s1))
                return true;
        return false;
    }

    // Füge einen neuen Knoten mit Namen s1 in Graph ein
    // (synchronized, weil Arbeiter-Thread zugreift)
    public synchronized boolean addKnoten(String s1) {
        // Graph noch leer
        if (m_KnotenNamen == null) {
            m_Matrix = new boolean[1][1];
            m_KnotenNamen = new String[] { s1 };
            return true;
        }
        // Knoten schon drin? → Ende
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(s1))
                return false;
        boolean[][] matrixNeu = new boolean[m_Matrix.length + 1][m_Matrix.length + 1];
        String[] knotenNamenNeu = new String[m_Matrix.length + 1];
        // Kopiere Matrix
        for (int zeile = 0; zeile < m_Matrix.length; ++zeile)
            for (int spalte = 0; spalte < m_Matrix.length; ++spalte)
                matrixNeu[zeile][spalte] = m_Matrix[zeile][spalte];
        // Kopiere Namen
        for (int i = 0; i < m_Matrix.length; ++i)
            knotenNamenNeu[i] = m_KnotenNamen[i];
        knotenNamenNeu[knotenNamenNeu.length - 1] = s1;
        m_KnotenNamen = knotenNamenNeu;
        m_Matrix = matrixNeu;
        return true;
    }

    // Lösche einen Knoten mit Namen s1 aus Graph
    // (synchronized, weil Arbeiter-Thread zugreift)
    public synchronized boolean delKnoten(String sdel) {
        // Graph noch leer → Ende
        if (m_KnotenNamen == null)
            return false;
        // Suche Knoten in Graph
        boolean treffer = false;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            if (m_KnotenNamen[i].equals(sdel)) {
                treffer = true;
                break;
            }
        // Knoten nicht drin? → Ende
        if (treffer == false)
            return false;
        // Ist nur noch der zu löschende Knoten in Graph → Graph löschen
        if (m_KnotenNamen.length == 1) {
            m_KnotenNamen = null;
            m_Matrix = null;
            return true;
        }
        // Los gehts
        boolean[][] matrixNeu = new boolean[m_Matrix.length - 1][m_Matrix.length - 1];
        String[] knotenNamenNeu = new String[m_Matrix.length - 1];
        // Kopiere Namen
        int tweak = 0;
        for (int i = 0; i < m_Matrix.length; ++i)
            if (m_KnotenNamen[i].equals(sdel))
                tweak = 1;
            else
                knotenNamenNeu[i - tweak] = m_KnotenNamen[i];
        // Kopiere Matrix
        int spalteKopie;
        int zeileKopie;
        zeileKopie = 0;
        // Durchlaufe Zeilen des Originals
        for (int zeileOrig = 0; zeileOrig < m_Matrix.length; ++zeileOrig) {
            // Gehört die Zeile nicht zum zu löschenden Knoten → weiter
            if (!m_KnotenNamen[zeileOrig].equals(sdel)) {
                spalteKopie = 0;
                // Durchlaufe Spalten des Originals
                for (int spalteOrig = 0; spalteOrig < m_Matrix.length; ++spalteOrig) {
                    // Gehört die Spalte nicht zum zu löschenden Knoten → weiter
                    if (!m_KnotenNamen[spalteOrig].equals(sdel)) {
                        // Kopiere Inhalte
                        matrixNeu[zeileKopie][spalteKopie] = m_Matrix[zeileOrig][spalteOrig];
                        ++spalteKopie; // in Kopie eine Spalte weiter
                    }
                }
                ++zeileKopie; // in Kopie eine Spalte weiter
            }
        }
        m_KnotenNamen = knotenNamenNeu;
        m_Matrix = matrixNeu;
        return true;
    }

    // Füge einen anderen gesamten Graphen hinzu
    public synchronized GraphPaul addGraph(GraphPaul graph2) {
        // Hat Graph2 überhaupt Knoten?
        if (graph2 != null && graph2.m_KnotenNamen != null) {
            String[] namenGraph2 = graph2.getKnotenNamen();
            // Füge alle Knoten und Verbindungen aus Graph2 ein
            for (String name1 : namenGraph2) {
                addKnoten(name1);
                for (String name2 : namenGraph2)
                    if (graph2.sindKnotenVerbunden(name1, name2))
                        verbindeKnoten(name1, name2);
            }
        }
        return this;
    }

    // To String
    public String toString() {
        return druckeGraphAlsMatrix();
    }

    // Graphen ausgeben (als Adjazenzmatrix)
    public String druckeGraphAlsMatrix() {
        // Ist der Graph leer ?
        if (m_KnotenNamen == null) {
            return "|leerer Graph|";
        }
        // Ermittle Länge des längsten Namen
        int maxStrLen = 0;
        for (int i = 0; i < m_KnotenNamen.length; ++i)
            maxStrLen = Math.max(maxStrLen, m_KnotenNamen[i].length());
        String out = "";
        // Alle Namen zentrieren
        String[] namenZentriert = new String[m_KnotenNamen.length];
        for (int i = 0; i < m_KnotenNamen.length; ++i) {
            namenZentriert[i] = "";
            int lenName = m_KnotenNamen[i].length();
            for (int j = 0; j < (maxStrLen - lenName) / 2; ++j) {
                namenZentriert[i] += " ";
            }
            namenZentriert[i] += m_KnotenNamen[i];
        }
        // Spaltenköpfe ausgeben
        out += String.format("%-" + maxStrLen + "s|", "");
        for (int nr = 0; nr < m_Matrix.length; ++nr)
            out += String.format("%-" + maxStrLen + "s|", namenZentriert[nr]);
        out += "\n";
        // Querstrich unter ersten Zeile ausgeben
        String nameStrich = "";
        for (int i = 0; i < maxStrLen; ++i)
            nameStrich += "-"; // Richtige länge für den Querstrich pro Namen
        out += String.format("%-" + maxStrLen + "s|", nameStrich);
        for (int nr = 0; nr < m_Matrix.length; ++nr)
            out += String.format("%-" + maxStrLen + "s|", nameStrich);
        out += "\n";
        // Zentriertes Zeichen für Verbunden erstellen
        String OK = "";
        for (int i = 0; i < maxStrLen / 2; ++i)
            OK += " "; // Halbe Maximale Wortlänge davor als Space einfügen
        OK += "x";
        // Zeilenweise die Tabelle ausgeben
        for (int zeile = 0; zeile < m_Matrix.length; ++zeile) {
            out += String
                    .format("%-" + maxStrLen + "s|", namenZentriert[zeile]);
            for (int spalte = 0; spalte < m_Matrix.length; ++spalte)
                out += (m_Matrix[zeile][spalte] ? String.format("%-"
                        + maxStrLen + "s|", OK) : String.format("%-"
                        + maxStrLen + "s|", " "));
            out += "\n";
        }
        return out;
    }

}
