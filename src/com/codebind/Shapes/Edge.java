package com.codebind.Shapes;

import java.awt.*;
import java.util.ArrayList;


public class Edge implements Drawable {
    private Node sourceNode;
    private Node destNode;

    public Edge(Node n1, Node n2) {
        this.sourceNode = n1;
        this.destNode = n2;
    }

    public Node getNeighbour(Node node) {
        if (node != sourceNode && node != destNode) {
            return null;
        }
        else {
            return (node == sourceNode) ? destNode : sourceNode;
        }
    }

    public Node[] getNodes() {
        Node[] pair = { sourceNode, destNode };

        return pair;
    }

    public void destroy() {
        sourceNode.removeEdge(this);
        destNode.removeEdge(this);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawLine((int)(sourceNode.getPosition().x), (int)(sourceNode.getPosition().y), (int)(destNode.getPosition().x), (int)(destNode.getPosition().y));
    }
}
