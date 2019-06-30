package com.codebind.Shapes;

import java.awt.*;
import java.awt.geom.Line2D;


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

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawLine(sourceNode.getPosition().x, sourceNode.getPosition().y, destNode.getPosition().x, destNode.getPosition().y);
    }
}
