package client;

import java.io.Serializable;

/**
 * Created by Pascal on 08.07.2014.
 */
public class Graph implements Serializable{

    private boolean[][] nodeConnections;
    private String[] nodes;

    public Graph() {
        nodes = null;
        nodeConnections = null;
    }

    public String[] getNodes() {
        return nodes;
    }

    public Graph clone() {
        Graph graphClone = new Graph();
        return graphClone.addGraph(this);
    }

    public void addConnection(String s1, String s2) {
        changeConnection(s1, s2, true);
    }

    public void deleteConnection(String s1, String s2) {
        changeConnection(s1, s2, false);
    }

    private void changeConnection(String s1, String s2, boolean arg) {
        if (nodes != null) {
            int s1Index = -1, s2Index = -1;
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].equals(s1))
                    s1Index = i;
                if (nodes[i].equals(s2))
                    s2Index = i;
            }
            if (s1Index != -1 && s2Index != -1) {
                nodeConnections[s1Index][s2Index] = arg;
                nodeConnections[s2Index][s1Index] = arg;
            }
        }
    }

    public boolean isNodeConnected(String s1, String s2) {
        if (nodes == null)
            return false;

        int column = -1, row = -1;
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i].equals(s1))
                column = i;
            if (nodes[i].equals(s2))
                row = i;
        }
        if (column == -1 || row == -1)
            return false;

        return (nodeConnections[column][row] && nodeConnections[row][column]);
    }

    public boolean isNodeInGraph(String s1) {
        if (nodes == null) {
            return false;
        }
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i].equals(s1)) {
                return true;
            }
        }
        return false;
    }

    public void addNode(String s1) {
        if (nodes == null) {
            nodeConnections = new boolean[1][1];
            nodes = new String[]{s1};
            nodeConnections[0][0] = true;
        } else if (!isNodeInGraph(s1)) {
            boolean[][] newMatrix = new boolean[nodeConnections.length + 1][nodeConnections.length + 1];
            for (int row = 0; row < newMatrix.length; row++) {
                for (int column = 0; column < newMatrix.length; column++) {
                    newMatrix[row][column] = false;
                }
            }
            for (int row = 0; row < nodeConnections.length; row++) {
                for (int column = 0; column < nodeConnections.length; column++) {
                    newMatrix[row][column] = nodeConnections[row][column];
                }
            }
            String[] newKnotenName = new String[nodeConnections.length + 1];
            for (int i = 0; i < nodeConnections.length; ++i) {
                newKnotenName[i] = nodes[i];
            }
            newKnotenName[newKnotenName.length - 1] = s1;
            newMatrix[newMatrix.length - 1][newMatrix.length - 1] = true;

            nodes = newKnotenName;
            nodeConnections = newMatrix;
        }
    }

    public synchronized Graph addGraph(Graph graph2) {
        Graph newGraph = new Graph();

        if (graph2 != null && graph2.nodes != null) {
            String[] toConnect = graph2.getNodes();
            for (int c = 0; c < toConnect.length; c++) {
                if (!newGraph.isNodeInGraph(toConnect[c])) {
                    newGraph.addNode(toConnect[c]);
                }
            }
            for (int c = 0; c < toConnect.length; c++) {
                for (int i = 0; i < toConnect.length; i++) {
                    if (graph2.isNodeConnected(toConnect[c], toConnect[i])) {
                        newGraph.addConnection(toConnect[c], toConnect[i]);
                    }
                }
            }

            toConnect = getNodes();
            if (toConnect != null) {
                for (int c = 0; c < toConnect.length; c++) {
                    if (!newGraph.isNodeInGraph(toConnect[c])) {
                        newGraph.addNode(toConnect[c]);
                    }
                }
                for (int c = 0; c < toConnect.length; c++) {
                    for (int i = 0; i < toConnect.length; i++) {
                        if (isNodeConnected(toConnect[c], toConnect[i])) {
                            newGraph.addConnection(toConnect[c], toConnect[i]);
                        }
                    }
                }
            }
            nodeConnections = newGraph.nodeConnections;
            nodes = newGraph.nodes;
        }
        return this;
    }

    public String toString() {
        if (nodes == null) {
            return " - - - - - - Kein Graph - - - - - - ";
        } else {
            String out="", div="", connect="";

            int maxLength = 0;
            for (int c = 0; c < nodes.length; c++) {
                maxLength = Math.max(maxLength, nodes[c].length());
            }

            String[] nodeNames = new String[nodes.length];
            for (int c = 0; c < nodes.length; c++) {
                nodeNames[c] = "";
                int lenName = nodes[c].length();
                for (int c2 = 0; c2 < (maxLength - lenName) / 2; c2++) {
                    nodeNames[c] += " ";
                }
                nodeNames[c] += nodes[c];
            }

            // columnn
            out += String.format("%-" + maxLength + "s|", "");
            for (int nr = 0; nr < nodeConnections.length; ++nr) {
                out += String.format("%-" + maxLength + "s|", nodeNames[nr]);
            }
            out += "\n";
            for (int c = 0; c < maxLength; c++) {
                div += "-";
            }
            out += String.format("%-" + maxLength + "s|", div);
            for (int nr = 0; nr < nodeConnections.length; ++nr)
                out += String.format("%-" + maxLength + "s|", div);
            out += "\n";

            // Node Connections
            for (int c = 0; c < maxLength / 2; c++) {
                connect += " ";
            }
            connect += "X";

            // rown
            for (int row = 0; row < nodeConnections.length; row++) {
                out += String.format("%-" + maxLength + "s|", nodeNames[row]);
                for (int column = 0; column < nodeConnections.length; column++) {
                    out += (nodeConnections[row][column] ? String.format("%-" + maxLength + "s|", connect) :
                                                           String.format("%-" + maxLength + "s|", " "));
                }
                out += "\n";
            }
            return out;
        }
    }
}
