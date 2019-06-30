package com.codebind;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphicsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Graph graph;

    public GraphicsPanel() {
        graph = new Graph();
        setBackground(new Color(205, 210, 255));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                graph.mouseMotionListener.mouseDragged(mouseEvent);

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                graph.mouseListener.mousePressed(mouseEvent);

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                graph.mouseListener.mouseReleased(mouseEvent);

                repaint();
            }
        });
    }

    Graph getGraph() {return graph;}

    void setGraphState(GraphState state) {
        graph.setState(state);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Times New Roman", Font.BOLD + Font.ITALIC, 15));
        graph.draw(g2);
    }
}



