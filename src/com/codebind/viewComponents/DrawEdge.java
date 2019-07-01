package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;

import java.awt.*;

public class DrawEdge implements Drawable {
    private Color color;
    private DrawNode sourceNode;
    private DrawNode destNode;

    public static final Color BASIC_COLOR = Color.blue;

    public DrawEdge(DrawNode sourceNode, DrawNode destNode, Color color) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.color = color;
    }

    public DrawEdge(DrawNode sourceNode, DrawNode destNode) {
        this(sourceNode, destNode, BASIC_COLOR);
    }


    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawLine(sourceNode.getPosition().x, sourceNode.getPosition().y, destNode.getPosition().x, destNode.getPosition().y);
    }
}
