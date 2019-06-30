package com.codebind;

import com.codebind.Shapes.Drawable;
import com.codebind.Shapes.Edge;
import com.codebind.Shapes.Node;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

enum GraphState {
    CREATE_NODE,
    CONNECT_NODE,
    MOVE_NODE,
    NOTHING
}

public class Graph implements Drawable {
    private GraphState state;
    private ArrayList<Edge> edges;
    private ArrayList<Node> nodes;
    private DraggData draggData;
    private ConnectData connectData;

    public MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            switch (state) {
                case CREATE_NODE:
                    nodes.add(new Node(e.getPoint(), new Dimension(20, 20), "a"));
                    break;
                case CONNECT_NODE:
                    for (Node node : nodes) {
                        if (node.getBoundingRect().contains(e.getPoint())) {
                            connectData.addNode(node);
                            break;
                        }
                    }

                    if (connectData.edgeCreated) {
                        edges.add(new Edge(connectData.firstNode, connectData.secondNode));
                        connectData.clear();
                    }

                    break;
                case MOVE_NODE:
                    for (Node node : nodes) {
                        if (node.getBoundingRect().contains(e.getPoint())) {
                            draggData.isDragg = true;
                            draggData.node = node;
                            break;
                        }
                    }

                    break;
                case NOTHING:
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            switch (state) {
                case CREATE_NODE:
                    break;
                case CONNECT_NODE:
                    break;
                case MOVE_NODE:
                    draggData.isDragg = false;
                    draggData.node = null;
                    break;
                case NOTHING:
                    break;
            }
        }
    };

    public MouseMotionListener mouseMotionListener = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);

            switch (state) {
                case CREATE_NODE:
                    break;
                case CONNECT_NODE:
                    break;
                case MOVE_NODE:
                    if (draggData.isDragg) {
                        draggData.node.moveTo(e.getPoint());
                    }

                    break;
                case NOTHING:
                    break;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
        }
    };

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        state = GraphState.NOTHING;
        draggData = new DraggData();
        connectData = new ConnectData();
    }

    public void setState(GraphState state) {
        this.state = state;
    }

    @Override
    public void draw(Graphics2D g) {
        for (Edge edge : edges) {
            edge.draw(g);
        }

        for (Node node : nodes) {
            node.draw(g);
        }
    }

    private static class DraggData {
        private boolean isDragg;
        private Node node;

        public DraggData() {
            isDragg = false;
            node = null;
        }
    }

    private static class ConnectData {
        private Node firstNode;
        private Node secondNode;
        private boolean edgeCreated;

        public ConnectData() {
            firstNode = null;
            secondNode = null;
            edgeCreated = false;
        }

        private void addNode(Node node) {
            if (secondNode != null) {
                return;
            }
            else if (firstNode == null) {
                node.setColor(Color.green);
                firstNode = node;
            }
            else {
                node.setColor(Color.green);
                secondNode = node;
                edgeCreated = true;
            }
        }

        public void clear() {
            firstNode.setColor(Color.red);
            secondNode.setColor(Color.red);
            firstNode = null;
            secondNode = null;
            edgeCreated = false;
        }
    }

    void removeGraph() {
        nodes.clear();
        edges.clear();
    }
}
