package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.Shapes.Movable;
import java.awt.*;
import java.awt.geom.*;

public class DrawNode implements Drawable, Movable {
    private Point2D.Double position;
    private Color color;
    private int radius;
    private String name;
    public static double scale = 1D;
    public static final Color BASIC_COLOR = Color.white;
    public static final Color SELECTED_COLOR = Color.green;
    public static final Color VISITED_COLOR = Color.magenta;
    public static final int BASIC_RADIUS = 15;
    public static final String DEFAULT_NAME = "";

    public DrawNode(Point2D.Double position, Color color, int radius, String name) {
        this.position = position;
        this.color = color;
        this.radius = radius;
        this.name = name;
    }

    public DrawNode(Point2D.Double position, Color color) {
        this(position, color, BASIC_RADIUS, DEFAULT_NAME);
    }

    public DrawNode(Point2D.Double position) {
        this(position, BASIC_COLOR, BASIC_RADIUS, DEFAULT_NAME);
    }

    public DrawNode(Point2D.Double position, String name) {
        this(position, BASIC_COLOR, BASIC_RADIUS, name);
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

    public String getName(){ return name; }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(73, 73, 73));
        g.fillOval((int)(position.x - radius*scale + 2*scale), (int)(position.y - radius*scale + 2*scale), (int)(2 * radius*scale ), (int)(2 * radius*scale));

        Point2D center = new Point2D.Float((int)(position.x -4*scale ) , (int)(position.y -4*scale ));
        float[] dist = {0.3f, 0.9f};
        Color[] colors = {color.brighter(), color.darker()};
        RadialGradientPaint p =
                new RadialGradientPaint(center, (int)(radius*scale),
                        dist, colors,
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g.setPaint(p);

        g.fillOval((int)(position.x - radius*scale), (int)(position.y - radius*scale), (int)(2 * radius*scale), (int)(2 * radius*scale));

        g.setColor(Color.darkGray);
        g.drawOval((int)(position.x - radius*scale), (int)(position.y - radius*scale), (int)(2 * radius*scale), (int)(2 * radius*scale));
    }

    public void print(Graphics2D g) {
        g.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        g.setColor(Color.black);
        g.drawString(name,(int)(position.x - radius * scale),(int)(position.y - radius * scale));
    }

    @Override
    public Rectangle2D.Double getBoundingRect() {
        return new Rectangle2D.Double(position.x - radius*scale, position.y - radius*scale, 2 * radius*scale, 2 * radius*scale);
    }
}
