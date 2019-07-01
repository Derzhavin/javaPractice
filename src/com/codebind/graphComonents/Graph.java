package com.codebind.graphComonents;

import java.util.ArrayList;


public class Graph {
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();

    public Graph() {}

    public void add(Node node) {
        nodes.add(node);
    }

    public void add(Edge edge) {
        edges.add(edge);
    }

    public void remove(Node node) {
        nodes.remove(node);
    }

    public void remove(Edge edge) {
        edges.remove(edge);
    }

    public void removeAll(ArrayList<Edge> edges) {
        this.edges.removeAll(edges);
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }

    public void connectAllVertices() {
        for (int i = 0; i < nodes.size() - 1; i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (!nodes.get(i).getNeighbours().contains(nodes.get(j))) {
                    Edge edge = new Edge(nodes.get(i), nodes.get(j));

                    nodes.get(i).addEdge(edge);
                    nodes.get(j).addEdge(edge);
                    edges.add(edge);
                }
            }
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
