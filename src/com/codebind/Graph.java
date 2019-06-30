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

public class Graph implements Drawable {
    private ArrayList<Edge> edges;
    private ArrayList<Node> nodes;
    private Main.GraphStates state;
    private DraggData draggData;

    public MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            switch (state) {
                case CREATE_NODE:
                    nodes.add(new Node(e.getPoint(), new Dimension(20, 20)));
                    break;
                case CONNECT_NODE:
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
        state = Main.GraphStates.NOTHING;
        draggData = new DraggData();
    }

    public void setState(Main.GraphStates state) {
        this.state = state;
    }

    @Override
    public void draw(Graphics2D g) {
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

}
