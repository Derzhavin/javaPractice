package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.Shapes.Movable;

import java.awt.*;

public class DrawNode implements Drawable, Movable {
    private Point position;
    private Color color;
    private int radius;

    public static final Color BASIC_COLOR = Color.red;
    public static final Color SELECTED_COLOR = Color.green;
    public static final int BASIC_RADIUS = 15;

    public DrawNode(Point position, Color color, int radius) {
        this.position = position;
        this.color = color;
        this.radius = radius;
    }

    public DrawNode(Point position, Color color) {
        this(position, color, BASIC_RADIUS);
    }

    public DrawNode(Point position) {
        this(position, BASIC_COLOR, BASIC_RADIUS);
    }

    public void moveTo(Point newPosition) {
        position = newPosition;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(position.x - radius, position.y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public Rectangle getBoundingRect() {
        return new Rectangle(position.x - radius, position.y - radius, 2 * radius, 2 * radius);
    }
}
