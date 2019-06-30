package com.codebind;

import com.codebind.Shapes.Edge;
import com.codebind.Shapes.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.*;

public class GraphicsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public static boolean AddVertex = false;
    public static boolean connectVertices = false;
    public boolean drag = false;
    private Node nodeDrag = null;

    public GraphicsPanel() {
        nodes = new ArrayList<>();
        setBackground(new Color(205, 210, 255));
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                System.out.println("AA");

                if (drag) {
                    nodeDrag.moveTo(mouseEvent.getPoint());
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);

                if (AddVertex) {
                    nodes.add(new Node(mouseEvent.getPoint(), new Dimension(20, 20)));
                }
                else {
                    for (Node node : nodes) {
                        if (node.getBoundingRect().contains(mouseEvent.getPoint())) {
                            drag = true;
                            nodeDrag = node;
                            break;
                        }
                    }

                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);

                drag = false;
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.red);

        for (Node node : nodes) {
            node.draw(g2);
        }
    }
}



