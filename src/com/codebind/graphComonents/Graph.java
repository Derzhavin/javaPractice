package com.codebind.graphComonents;

import com.codebind.Snapshots.Snapshot;

import java.util.ArrayList;


public class Graph {
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();

    public Graph() {}

    public void add(Node node) {
        nodes.add(node);
    }

    public void add(Edge edge) {
        ArrayList<Node> pair = edge.getNodes();

        Edge e1 = pair.get(0).getEdge(pair.get(1));

        if (e1 != null && edge.isDirected() && e1.isDirected()) {
            if (pair.get(0) == e1.getNodes().get(1)) {
                edges.remove(e1);
                e1.destroy();

                Edge newEdge = new Edge(pair.get(0), pair.get(1), false);
                pair.get(0).addEdge(newEdge);
                pair.get(1).addEdge(newEdge);
                edges.add(newEdge);
            }
        }

        if (e1 == null) {
            pair.get(0).addEdge(edge);
            pair.get(1).addEdge(edge);
            edges.add(edge);
        }
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

    public void removeAllEdges() {
        edges.clear();
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

    public Snapshot save() {
        return new Snapshot(nodes, edges);
    }

    public void restore(Snapshot snapshot) {
        if (snapshot == null) {
            return;
        }

        nodes.clear();
        edges.clear();

        nodes.addAll(snapshot.getSingleNodes());
        edges.addAll(snapshot.getEdges());

        for (Edge edge : edges) {
            ArrayList<Node> pair = edge.getNodes();

            if (!nodes.contains(pair.get(0))) {
                nodes.add(pair.get(0));
            }

            if (!nodes.contains(pair.get(1))) {
                nodes.add(pair.get(1));
            }

            pair.get(0).addEdge(edge);
            pair.get(1).addEdge(edge);
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
