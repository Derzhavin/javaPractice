package com.codebind.graphComonents;

import com.codebind.viewComponents.DrawNode;

import java.util.ArrayList;

public class Node {
    private ArrayList<Edge> edges = new ArrayList<>();
    private DrawNode nodeView;

    public Node(DrawNode nodeView) {
        this.nodeView = nodeView;
    }

    public DrawNode getView() {
        return nodeView;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Edge getEdge(Node neighbour) {
        for (Edge edge : edges) {
            if (edge.getNeighbour(this) == neighbour) {
                return edge;
            }
        }

        return null;
    }

    public void destroy() {
        for (Edge edge : edges) {
            Node neighbour = edge.getNeighbour(this);
            neighbour.getEdges().remove(edge);
        }

        edges.clear();
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> nbs = new ArrayList<>();

        for (Edge edge : edges) {
            nbs.add(edge.getNeighbour(this));
        }

        return nbs;
    }
}
