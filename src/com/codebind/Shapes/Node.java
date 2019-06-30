package com.codebind.Shapes;

import java.awt.*;

public class Node implements Movable, Drawable {
    private Point position = null;
    private Dimension size = new Dimension(20, 20);
    private Color color;
    private String name;
    public Node() {}

    public Node(Point position, Dimension size, String name) {
        this.position = position;
        this.size = size;
        this.color = Color.red;
        this.name = name;
    }

    public void moveTo(Point newPosition) {
        position = newPosition;
    }

    public Point getPosition() {
        return position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Rectangle getBoundingRect() {
        return new Rectangle(position.x - size.width / 2, position.y - size.height / 2, size.width, size.height);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(position.x - size.width / 2, position.y - size.height / 2, size.width, size.height);

        g.setColor(Color.BLACK);
        g.drawString(name, position.x - 3, position.y + 3);
    }
}
