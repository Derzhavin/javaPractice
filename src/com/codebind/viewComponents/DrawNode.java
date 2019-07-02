package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.Shapes.Movable;
import java.awt.*;
import java.awt.geom.*;

public class DrawNode implements Drawable, Movable {
    private Point2D.Double position;
    private Color color;
    private int radius;
    public static double scale = 1D;
    public static final Color BASIC_COLOR = Color.red;
    public static final Color SELECTED_COLOR = Color.green;
    public static final int BASIC_RADIUS = 15;

    public DrawNode(Point2D.Double position, Color color, int radius) {
        this.position = position;
        this.color = color;
        this.radius = radius;
    }

    public DrawNode(Point2D.Double position, Color color) {
        this(position, color, BASIC_RADIUS);
    }

    public DrawNode(Point2D.Double position) {
        this(position, BASIC_COLOR, BASIC_RADIUS);
    }

    public void moveTo(Point2D.Double newPosition) {
        position = newPosition;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval((int)(position.x - radius*scale), (int)(position.y - radius*scale), (int)(2 * radius*scale), (int)(2 * radius*scale));
    }

    @Override
    public Rectangle2D getBoundingRect() {
        return new Rectangle2D.Double(position.x - radius*scale, position.y - radius*scale, 2 * radius*scale, 2 * radius*scale);
    }
}
