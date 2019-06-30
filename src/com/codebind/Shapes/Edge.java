package com.codebind.Shapes;

import java.awt.*;
import java.awt.geom.Line2D;

public class Edge implements Drawable {
    private Node[] nodes = new Node[2];

    Edge(Node n1, Node n2) {
        nodes[0] = n1;
        nodes[1] = n2;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawLine(nodes[0].getPosition().x, nodes[0].getPosition().y, nodes[1].getPosition().x, nodes[1].getPosition().y);
    }
}
