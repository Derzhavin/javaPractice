package com.codebind.Shapes;

import com.codebind.GraphicsPanel;

import java.awt.*;

public class Node implements Movable, Drawable {
    private Point position = null;
    private Dimension size = new Dimension(20, 20);
    private boolean beginDrag;

    public Node() {}

    public Node(Point _position, Dimension size) {
        this.position = _position;
        this.size = size;
    }

    public void moveTo(Point newPosition) {
        position = newPosition;
    }

    @Override
    public Rectangle getBoundingRect() {
        return new Rectangle(position.x - size.width / 2, position.y - size.height / 2, size.width, size.height);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval(position.x - size.width / 2, position.y - size.height / 2, size.width, size.height);
    }
}
