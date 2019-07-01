package com.codebind;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import com.codebind.Shapes.Node;

public class GraphicsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Graph graph;
    private JPanel panelNodesEdges;

    private double scale = 1D;

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

                if (graph.getState() == GraphState.MOVE_NODE) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }

                updatePanelNodesEdges();

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                graph.mouseListener.mouseReleased(mouseEvent);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                updatePanelNodesEdges();

                repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getPreciseWheelRotation() < 0) {
                    scale -= 0.1;
                } else {
                    scale += 0.1;
                }

                if (scale < 0.01) {
                    scale = 0.01;
                }
                System.out.println(scale);
                Point2D.Double center = new Point2D.Double(getWidth()/2.0, getHeight()/2.0);

                Node.scale *= scale;

                for(Node node: graph.getNodes()) {
                    Point2D.Double nodePoint2D = node.getPosition();
                    if (center.y > nodePoint2D.y)
                        nodePoint2D.y = center.y - Math.abs(center.y - nodePoint2D.y) * scale;
                    else if (center.y < nodePoint2D.y)
                        nodePoint2D.y = center.y + Math.abs(center.y - nodePoint2D.y) * scale;

                    if (center.x > nodePoint2D.x)
                        nodePoint2D.x = center.x - Math.abs(center.x - nodePoint2D.x) * scale;
                    else if (center.x < nodePoint2D.x)
                        nodePoint2D.x = center.x + Math.abs(center.x - nodePoint2D.x) * scale;
                }

                repaint();

                scale = 1D;
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

    public void updatePanelNodesEdges() {
        JLabel labelNodes = (JLabel)panelNodesEdges.getComponents()[0];
        JLabel labelEdges = (JLabel)panelNodesEdges.getComponents()[1];

        labelNodes.setText("Nodes: " + graph.getNodes().size());
        labelEdges.setText("Edges: " + graph.getEdges().size());
    }

    public void setPanelNodesEdges(JPanel panelNodesEdges) {
        this.panelNodesEdges = panelNodesEdges;
    }
}



