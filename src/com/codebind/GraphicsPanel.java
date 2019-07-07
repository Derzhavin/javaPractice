package com.codebind;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.GraphStates;
import com.codebind.viewComponents.DrawGraph;
import com.codebind.Сancellation.Buffer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.geom.*;

public class GraphicsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private DrawGraph graph;
    private JPanel panelNodesEdges;
    private Buffer buffer = new Buffer();

    public GraphicsPanel() {
        graph = new DrawGraph();
        setBackground(new Color(205, 210, 255));

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                super.mouseDragged(mouseEvent);
                GraphEventManager.getInstance().mouseDragged(mouseEvent);

                if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                GraphEventManager.getInstance().mousePressed(mouseEvent);

                if (GraphEventManager.getInstance().getState() == GraphStates.MOVE_NODE) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }

                updatePanelNodesEdges();

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);
                GraphEventManager.getInstance().mouseReleased(mouseEvent);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                updatePanelNodesEdges();

                repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Point2D.Double center = new Point2D.Double(getWidth()/2.0, getHeight()/2.0);

                GraphEventManager.getInstance().mouseWheelMoved(e, center);

                repaint();
            }
        });
    }

    void setGraphState(GraphStates state) {
        GraphEventManager.getInstance().setState(state);
    }

    public void setGraph(DrawGraph graph){
        this.graph = graph;
        this.updatePanelNodesEdges();
        repaint();
    }

    public void recoverGraph() {
        DrawGraph graph = buffer.recover();

        if (graph != null) {
            this.graph = graph;
            GraphEventManager.getInstance().setGraph(graph.getGraph());
            System.out.println("not null");
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Times New Roman", Font.BOLD + Font.ITALIC, 15));

        graph.draw(g2);
        graph.print(g2);

        if (GraphEventManager.getInstance().isCutting()) {
            ArrayList<Point> pair = GraphEventManager.getInstance().getScissors();

            g.setColor(Color.red);
            g.drawLine(pair.get(0).x, pair.get(0).y, pair.get(1).x, pair.get(1).y);
        }

        if (GraphStates.MOVE_NODE != GraphEventManager.getInstance().getState() &&
                GraphStates.ALGORITHM != GraphEventManager.getInstance().getState() &&
                GraphStates.NOTHING != GraphEventManager.getInstance().getState()) {
            try {
                if (graph != null) {
                    buffer.push(graph.clone());
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePanelNodesEdges() {
        JLabel labelNodes = (JLabel)panelNodesEdges.getComponents()[0];
        JLabel labelEdges = (JLabel)panelNodesEdges.getComponents()[1];

        labelNodes.setText("Nodes: " + GraphEventManager.getInstance().getNodesCount());
        labelEdges.setText("Edges: " + GraphEventManager.getInstance().getEdgesCount());
    }

    public void setPanelNodesEdges(JPanel panelNodesEdges) {
        this.panelNodesEdges = panelNodesEdges;
    }
}



