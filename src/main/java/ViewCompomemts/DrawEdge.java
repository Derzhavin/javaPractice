package ViewCompomemts;

import Shapes.Drawable;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.Random;

public class DrawEdge implements Drawable {
    protected Color color;
    private DrawNode sourceNode;
    private DrawNode destNode;
    protected Point2D.Double sourcePosition;
    protected Point2D.Double destPosition;
    protected Point2D.Double circleCenter;

    public static final Color BASIC_COLOR = Color.darkGray;
    public static final Color VISITED_COLOR = Color.magenta;

    public DrawEdge(DrawNode sourceNode, DrawNode destNode, Color color) {
        this.sourceNode = sourceNode;
        this.destNode = destNode;
        this.color = color;
        updateOffsetVector();
    }

    public DrawEdge(DrawEdge other, DrawNode sourceNode, DrawNode destNode) {
        this.color = other.color;
        this.sourceNode = sourceNode;
        this.destNode = destNode;
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

        double h = 30 * DrawNode.scale;
        Point2D.Double hordaCenter = new Point2D.Double((sourcePosition.x + destPosition.x) / 2F,
                (sourcePosition.y + destPosition.y) / 2F );
        double hordaLength = Math.sqrt(Math.pow(sourcePosition.x - destPosition.x, 2) +
                Math.pow(sourcePosition.y - destPosition.y, 2));
        h = Math.min(h, hordaLength / 2);
        Point2D.Double unitVector = new Point2D.Double(destPosition.x - hordaCenter.x,
                destPosition.y - hordaCenter.y);
        unitVector.x /= (hordaLength / 2);
        unitVector.y /= (hordaLength / 2);
        Point2D.Double radiusVector = new Point2D.Double(0, 0);
        radiusVector.x = unitVector.x * Math.cos(Math.PI / 2) - unitVector.y * Math.sin(Math.PI / 2);
        radiusVector.y = unitVector.x * Math.sin(Math.PI / 2) + unitVector.y * Math.cos(Math.PI / 2);
        double radius = (Math.pow(hordaLength, 2) + 4 * Math.pow(h, 2)) / (8 * h);
        radiusVector.x *= radius - h;
        radiusVector.y *= radius - h;
        radiusVector.x += hordaCenter.x;
        radiusVector.y += hordaCenter.y;

        circleCenter = radiusVector;

        double width = 2 * radius;
        double height = 2 * radius;

        Point2D.Double firstVector = new Point2D.Double(sourcePosition.x - radiusVector.x,
                sourcePosition.y - radiusVector.y);
        Point2D.Double secondVector = new Point2D.Double(destPosition.x - radiusVector.x,
                destPosition.y - radiusVector.y);
        double firstAngle = Math.atan2(firstVector.x, firstVector.y);
        double secondAngle = Math.atan2(secondVector.x, secondVector.y);

        if (firstAngle < 0) {
            firstAngle = 2 * Math.PI + firstAngle;
        }

        if (secondAngle < 0) {
            secondAngle = 2 * Math.PI + secondAngle;
        }

        Arc2D.Double arc = new Arc2D.Double();
        arc.x = radiusVector.x - radius;
        arc.y = radiusVector.y - radius;
        arc.width = width;
        arc.height = height;
        arc.start = Math.toDegrees(secondAngle) - 90;
        arc.extent = Math.toDegrees((Math.abs(firstAngle - secondAngle) < Math.PI) ? Math.abs(firstAngle - secondAngle) : 2 * Math.PI - Math.abs(firstAngle - secondAngle));

        g.draw(arc);
       // g.drawLine((int)sourcePosition.x, (int)sourcePosition.y, (int)destPosition.x, (int)destPosition.y);
        //g.setColor(Color.red);
        //g.drawLine((int)radiusVector.x, (int)radiusVector.y, (int)destPosition.x, (int)destPosition.y);
    }
}
