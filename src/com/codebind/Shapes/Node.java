package com.codebind.Shapes;

import java.awt.*;
import java.util.ArrayList;

public class Node implements Movable, Drawable {
    public static double scale = 2D;
    private Point position = null;
    private Dimension size = new Dimension(20, 20);
    private ArrayList<Edge> edges;
    private Color color;

    public Node() {}

    public Node(Point _position, Dimension size) {
        this.edges = new ArrayList<>();
        this.position = _position;
        this.size = size;
        this.color = Color.red;
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

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> nbs = new ArrayList<>();

        for (Edge edge : edges) {
            nbs.add(edge.getNeighbour(this));
        }

        return nbs;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void destroy() {
        for (Edge edge : edges) {
            Node neighbour = edge.getNeighbour(this);
            neighbour.getEdges().remove(edge);
        }

        edges.clear();
    }

    @Override
    public Rectangle getBoundingRect() {
        return new Rectangle(position.x - (int) (size.width*scale) / 2, position.y - (int) (size.height*scale) / 2, (int) (size.width*scale), (int)(size.height*scale));
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(position.x - (int) (size.width*scale) / 2, position.y - (int) (size.height*scale) / 2,(int)(size.width*scale), (int)(size.height*scale));
    }
}
