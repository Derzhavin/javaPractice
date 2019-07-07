package com.codebind.graphComonents;

import com.codebind.viewComponents.DrawDirectedEdge;
import com.codebind.viewComponents.DrawEdge;
import com.codebind.viewComponents.DrawNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Edge {
    private Node sourceNode;
    private Node destNode;
    private boolean isDirected = false;
    private DrawEdge edgeView;


    public Edge(Node sourceNode, Node destNode, DrawEdge edgeView) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.edgeView = edgeView;
    }

    public Edge(Edge other, HashMap<Node, Node> checkedNodes) {
        if (!checkedNodes.keySet().contains(other.sourceNode)) {
            this.sourceNode = new Node(other.sourceNode);
        }
        else {
            this.sourceNode = checkedNodes.get(other.sourceNode);
        }

        if (!checkedNodes.keySet().contains(other.destNode)) {
            this.destNode = new Node(other.destNode);
        }
        else {
            this.destNode = checkedNodes.get(other.destNode);
        }

        this.isDirected = other.isDirected;
        this.edgeView = this.isDirected ? new DrawDirectedEdge(other.edgeView, this.sourceNode.getView(), this.destNode.getView()) :
                new DrawEdge(other.edgeView, this.sourceNode.getView(), this.destNode.getView());
    }

    public Edge(Node sourceNode, Node destNode) {
        this(sourceNode, destNode, new DrawEdge(sourceNode.getView(), destNode.getView()));
    }

    public Edge(Node sourceNode, Node destNode, boolean isDirected) {
        this(sourceNode, destNode, isDirected ? new DrawDirectedEdge(sourceNode.getView(), destNode.getView()) :
                                                new DrawEdge(sourceNode.getView(), destNode.getView()));
        this.isDirected = isDirected;
    }

    public Node getNeighbour(Node node) {
        if (node != sourceNode && node != destNode) {
            return null;
        }
        else {
            return (node == sourceNode) ? destNode : sourceNode;
        }
    }

    public Node getSmartNeighbour(Node node) {
        if (!isDirected) {
            return getNeighbour(node);
        }

        if (node != sourceNode && node != destNode) {
            return null;
        }
        else {
            return (node == sourceNode) ? destNode : null;
        }
    }

    public void destroy() {
        sourceNode.removeEdge(this);
        destNode.removeEdge(this);
    }

    public boolean isDirected() {
        return isDirected;
    }

    public ArrayList<Node> getNodes() {
        ArrayList<Node> pair = new ArrayList<>();
        pair.add(sourceNode);
        pair.add(destNode);

        return pair;
    }

    public DrawEdge getView() {
        return edgeView;
    }

    public void transpose() {
        Node tmpNode = sourceNode;
        sourceNode = destNode;
        destNode = tmpNode;

        edgeView.transpose();
    }
}
