package com.codebind.graphComonents;

import com.codebind.viewComponents.DrawDirectedEdge;
import com.codebind.viewComponents.DrawEdge;
import com.codebind.viewComponents.DrawNode;

import java.util.ArrayList;

public class Edge implements  Cloneable{
    private Node sourceNode;
    private Node destNode;
    private boolean isDirected = false;
    private DrawEdge edgeView;

    @Override
    public Edge clone() throws CloneNotSupportedException {
        Edge other = (Edge)super.clone();

        other.sourceNode = (Node)this.sourceNode.clone();
        other.destNode = (Node)this.destNode.clone();
        other.isDirected = this.isDirected;
        other.edgeView = (DrawEdge)this.edgeView.clone();

        return other;
    }

    public Edge(Node sourceNode, Node destNode, DrawEdge edgeView) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.edgeView = edgeView;
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
