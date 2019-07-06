package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.graphComonents.Node;

import java.awt.*;
import java.awt.geom.Point2D;

public class DrawEdge implements Drawable {
    protected Color color;
    protected DrawNode sourceNode;
    protected DrawNode destNode;
    protected Point2D.Double sourcePosition;
    protected Point2D.Double destPosition;

    public DrawEdge() {}

    public DrawEdge(DrawEdge other) {
        this.color = other.color;
        this.sourceNode = new DrawNode(other.sourceNode);
        this.destNode = new DrawNode(other.destNode);
        this.sourcePosition = other.sourcePosition;
        this.destPosition = other.destPosition;
    }

    public static final Color BASIC_COLOR = Color.darkGray;
    public static final Color VISITED_COLOR = Color.magenta;

    public DrawEdge(DrawNode sourceNode, DrawNode destNode, Color color) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.color = color;
        updateOffsetVector();
    }

    public Color getColor() {
        return color;
    }

    public DrawEdge(DrawNode sourceNode, DrawNode destNode) {
        this(sourceNode, destNode, BASIC_COLOR);
    }

    private Point2D.Double createOffsetVector(DrawNode sourceNode, DrawNode destNode) {
        Point2D.Double vector = new Point2D.Double(destNode.getPosition().x - sourceNode.getPosition().x,
                destNode.getPosition().y - sourceNode.getPosition().y);
        double vectorLength = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
        vector.x /= vectorLength;
        vector.y /= vectorLength;
        vector.x *= DrawNode.BASIC_RADIUS * DrawNode.scale;
        vector.y *= DrawNode.BASIC_RADIUS * DrawNode.scale;

        return vector;
    }

    private void updateOffsetVector() {
        Point2D.Double offsetVector = createOffsetVector(sourceNode, destNode);

        this.sourcePosition = new Point2D.Double(sourceNode.getPosition().x + offsetVector.x,
                sourceNode.getPosition().y + offsetVector.y);
        this.destPosition = new Point2D.Double(destNode.getPosition().x - offsetVector.x,
                destNode.getPosition().y - offsetVector.y);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void transpose() {
        DrawNode tmpNode = sourceNode;
        sourceNode = destNode;
        destNode = tmpNode;
    }

    @Override
    public void draw(Graphics2D g) {
        updateOffsetVector();
        g.setColor(color);
        g.drawLine((int)sourcePosition.x, (int)sourcePosition.y, (int)destPosition.x, (int)destPosition.y);
    }
}
