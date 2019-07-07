package com.codebind.viewComponents;

import java.awt.*;
import java.awt.geom.Point2D;

public class DrawDirectedEdge extends DrawEdge implements Cloneable{
    private static final double POLYGON_ANGLE = (Math.PI / 12);
    private static final double POLYGON_SIZE = 20D;

    public DrawDirectedEdge() {super();}

    @Override
    public DrawDirectedEdge clone() throws CloneNotSupportedException {
        DrawDirectedEdge other =(DrawDirectedEdge)super.clone();
        return other;
    }

    public DrawDirectedEdge(DrawNode sourceNode, DrawNode destNode) {
        super(sourceNode, destNode);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        Point2D.Double[] polygonPoints = new Point2D.Double[3];
        Point2D.Double vector = new Point2D.Double(sourcePosition.x - destPosition.x, sourcePosition.y - destPosition.y);
        double vectorLength = Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
        Point2D.Double unitVector = new Point2D.Double(vector.x / vectorLength, vector.y / vectorLength);
        unitVector.x *= POLYGON_SIZE * DrawNode.scale;
        unitVector.y *= POLYGON_SIZE * DrawNode.scale;

        polygonPoints[0] = (Point2D.Double) destPosition.clone();
        polygonPoints[1] = new Point2D.Double(unitVector.x * Math.cos(POLYGON_ANGLE) - unitVector.y * Math.sin(POLYGON_ANGLE) + destPosition.x,
                unitVector.x * Math.sin(POLYGON_ANGLE) + unitVector.y * Math.cos(POLYGON_ANGLE) + destPosition.y);
        polygonPoints[2] = new Point2D.Double(unitVector.x * Math.cos(-POLYGON_ANGLE) - unitVector.y * Math.sin(-POLYGON_ANGLE) + destPosition.x,
                unitVector.x * Math.sin(-POLYGON_ANGLE) + unitVector.y * Math.cos(-POLYGON_ANGLE) + destPosition.y);

        int xs[] = { (int)polygonPoints[0].x, (int)polygonPoints[1].x, (int)polygonPoints[2].x };
        int ys[] = { (int)polygonPoints[0].y, (int)polygonPoints[1].y, (int)polygonPoints[2].y };

        g.fillPolygon(xs, ys, 3);
    }
}
