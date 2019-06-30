package com.codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.*;

public class GraphicsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public ArrayList<Point> points;
    public boolean AddVertex = false;

    public GraphicsPanel() {
        points = new ArrayList<Point>();
        setBackground(new Color(205, 210, 255));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);

                if (AddVertex) {
                    points.add(new Point(mouseEvent.getX(), mouseEvent.getY()));
                    repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.red);
        for (Point point : points) {
            g2.fillOval(point.x - 10, point.y - 10, 20, 20);
        }
    }

}



