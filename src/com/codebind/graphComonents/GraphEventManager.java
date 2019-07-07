package com.codebind.graphComonents;

import com.codebind.Algorithms;
import com.codebind.GraphicsPanel;
import com.codebind.Snapshots.GraphCaretaker;
import com.codebind.algorithmComponents.Algorithm;
import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.viewComponents.DrawDirectedEdge;
import com.codebind.viewComponents.DrawEdge;
import com.codebind.viewComponents.DrawNode;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphEventManager {
    private Graph graph;
    private GraphStates graphState;
    private DraggData draggData;
    private ConnectData connectData;
    private DeleteData deleteData;
    private Point oldDragPoint;

    private GraphicsPanel graphicsPanel;

    private static final GraphEventManager instance = new GraphEventManager();

    public static GraphEventManager getInstance() {
        return instance;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public GraphStates getState() {
        return graphState;
    }

    public int getNodesCount() {
        return graph.getNodes().size();
    }

    public int getEdgesCount() {
        return graph.getEdges().size();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setState(GraphStates state) {
        switch (state) {
            case CREATE_NODE:
            case MOVE_NODE:
            case NOTHING:
            case DELETE_NODE:
            case ALGORITHM:
                connectData.clear();
                break;
            case CONNECT_NODE:
                break;
        }

        graphState = state;
    }

    public void removeGraph() {
        graph.clear();
    }

    public void connectAllVertices() {
        graph.connectAllVertices();
    }

    public boolean isCutting() {
        return deleteData.cutting;
    }

    public void setNodeConnectionType(boolean isDirected) {
        ConnectData.IS_DIRECTED_CONNECTION = isDirected;
    }

    public void setGraphicsPanel(GraphicsPanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
    }

    public Node getNodeOnPos(Point position, ArrayList<Node> nodes) {
        for (Node node : nodes) {
            if (node.getView().getBoundingRect().contains(position)) {
                return node;
            }
        }

        return null;
    }

    public ArrayList<Point> getScissors() {
        ArrayList<Point> pair = new ArrayList<>();
        pair.add(deleteData.firstPoint);
        pair.add(deleteData.secondPoint);

        return pair;
    }

    public void resetConnectData() {
        connectData.clear();
    }

    private GraphEventManager() {
        graph = null;
        graphState = GraphStates.NOTHING;
        draggData = new DraggData();
        connectData = new ConnectData();
        deleteData = new DeleteData();
        graphicsPanel = null;
    }

    private int getNewNodeName() {
        int newNodeName = 0;

        ArrayList<Node> nodes = graph.getNodes();
        Integer[] mas = new Integer[nodes.size()];

        for(int i = 0; i< nodes.size(); i++ ){
            mas[i] = Integer.parseInt(nodes.get(i).getView().getName());
        }

        Arrays.sort(mas);

        for (int i : mas){
            if(newNodeName != i) {
                break;
            }

            newNodeName++;
        }

        return newNodeName;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        oldDragPoint = mouseEvent.getPoint();

        if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
            return;
        }

        switch (graphState) {
            case CREATE_NODE:
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    GraphCaretaker.push(graph.save());

                    graph.add(new Node(new DrawNode(new Point2D.Double(mouseEvent.getX(), mouseEvent.getY()),
                            String.valueOf(getNewNodeName()))));
                }

                break;
            case CONNECT_NODE:
                if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
                    connectData.clear();
                    break;
                }
                else if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    connectData.addNodeOnMousePos(mouseEvent.getPoint(), graph.getNodes());

                    if (connectData.edgeCreated) {
                        Edge newEdge = connectData.getEdgeIfNotExists();

                        if (newEdge != null) {
                            GraphCaretaker.push(graph.save());
                            graph.add(newEdge);
                        }
                    }
                }
                break;
            case MOVE_NODE:
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    draggData.grabNodeOnMousePos(mouseEvent.getPoint(), graph.getNodes());

                    if (draggData.isDragg) {
                        GraphCaretaker.push(graph.save());
                    }
                }
                break;
            case NOTHING:
                break;
            case DELETE_NODE:
                deleteData.setFirstPoint(mouseEvent.getPoint());
                deleteData.secondPoint = mouseEvent.getPoint();

                for (Node node : graph.getNodes()) {
                    if (node.getView().getBoundingRect().contains(mouseEvent.getPoint())) {
                        GraphCaretaker.push(graph.save());
                        graph.remove(node);
                        graph.removeAll(node.getEdges());
                        node.destroy();

                        break;
                    }
                }
                break;
            case ALGORITHM:
                if (!Algorithms.currentAlgorithm.isInitialized()) {
                    Node selectedNode = getNodeOnPos(mouseEvent.getPoint(), graph.getNodes());

                    if (selectedNode != null) {
                        Algorithms.currentAlgorithm.initialize(selectedNode);
                    }
                }
                break;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e, Point2D.Double center) {
        double scale = 1D;
        center.x = e.getPoint().x;
        center.y = e.getPoint().y;

        if (e.getPreciseWheelRotation() < 0) {
            scale += 0.1;
        } else {
            scale -= 0.1;
        }

        DrawNode.scale *= scale;

        if (DrawNode.scale > 4) {
            DrawNode.scale /= scale;
            scale = 1;
        }
        if (DrawNode.scale < 0.5) {
            DrawNode.scale /= scale;
            scale = 1;
        }

        if (scale != 1) {
            for (Node node : graph.getNodes()) {
                Point2D.Double nodePoint2D = node.getView().getPosition();

                if (center.y > nodePoint2D.y) {
                    nodePoint2D.y = center.y - Math.abs(center.y - nodePoint2D.y) * scale;
                } else if (center.y < nodePoint2D.y) {
                    nodePoint2D.y = center.y + Math.abs(center.y - nodePoint2D.y) * scale;
                }

                if (center.x > nodePoint2D.x) {
                    nodePoint2D.x = center.x - Math.abs(center.x - nodePoint2D.x) * scale;
                } else if (center.x < nodePoint2D.x) {
                    nodePoint2D.x = center.x + Math.abs(center.x - nodePoint2D.x) * scale;
                }
            }
        }
    }
    public void mouseReleased(MouseEvent mouseEvent) {
        if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
            return;
        }

        switch (graphState) {
            case CREATE_NODE:
                break;
            case CONNECT_NODE:
                break;
            case MOVE_NODE:
                draggData.clear();
                break;
            case NOTHING:
                break;
            case DELETE_NODE:
                deleteData.setSecondPoint(mouseEvent.getPoint());
                ArrayList<Edge> edgesToDelete = new ArrayList<>();

                for (Edge edge : graph.getEdges()) {
                    if (Line2D.linesIntersect(edge.getNodes().get(0).getView().getPosition().x, edge.getNodes().get(0).getView().getPosition().y,
                            edge.getNodes().get(1).getView().getPosition().x, edge.getNodes().get(1).getView().getPosition().y,
                            deleteData.firstPoint.x, deleteData.firstPoint.y,
                            deleteData.secondPoint.x, deleteData.secondPoint.y)) {
                        edgesToDelete.add(edge);
                    }
                }

                if (!edgesToDelete.isEmpty()) {
                    GraphCaretaker.push(graph.save());
                }

                for (Edge edge : edgesToDelete) {
                    edge.destroy();
                }

                graph.removeAll(edgesToDelete);
                break;
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (SwingUtilities.isMiddleMouseButton(mouseEvent)) {
            double xMotion = mouseEvent.getX() - oldDragPoint.getX();
            double yMotion = mouseEvent.getY() - oldDragPoint.getY();

            for(Node node : graph.getNodes()) {
                Point2D.Double oldPoint2D = node.getView().getPosition();
                node.getView().moveTo(new Point2D.Double(oldPoint2D.getX()+xMotion, oldPoint2D.getY()+yMotion));
            }

            oldDragPoint = mouseEvent.getPoint();
        }
        else {
            switch (graphState) {
                case CREATE_NODE:
                    break;
                case CONNECT_NODE:
                    break;
                case MOVE_NODE:
                    if (draggData.getIsDragg()) {
                        draggData.moveNodeIfGrabed(mouseEvent.getPoint());
                    }

                    break;
                case NOTHING:
                    break;
                case DELETE_NODE:
                    deleteData.secondPoint = mouseEvent.getPoint();
                    break;
            }
        }
    }


    private static class DraggData {
        private boolean isDragg;
        private Node node;

        public boolean getIsDragg() {return isDragg;}

        public DraggData() {
            clear();
        }

        public void grabNodeOnMousePos(Point mousePosition, ArrayList<Node> nodes) {
            for (Node node : nodes) {
                if (node.getView().getBoundingRect().contains(mousePosition)) {
                    isDragg = true;
                    this.node = node;
                    break;
                }
            }
        }

        public void moveNodeIfGrabed(Point mousePosition) {
            if (isDragg) {
                node.getView().moveTo(new Point2D.Double(mousePosition.getX(), mousePosition.getY()));
            }
        }

        public void clear() {
            isDragg = false;
            node = null;
        }
    }

    private static class DeleteData {
        private Point firstPoint;
        private Point secondPoint;
        private boolean cutting;

        public DeleteData() {
            firstPoint = null;
            secondPoint = null;
            cutting = false;
        }

        public void setFirstPoint(Point point) {
            firstPoint = point;
            cutting = true;
        }

        public void setSecondPoint(Point point) {
            secondPoint = point;
            cutting = false;
        }
    }

    public static class ConnectData {
        private Node firstNode;
        private Node secondNode;
        private boolean edgeCreated;
        public static boolean IS_DIRECTED_CONNECTION = false;

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
                node.getView().setColor(DrawNode.SELECTED_COLOR);
                firstNode = node;
            }
            else {
                if (node != firstNode) {
                    node.getView().setColor(DrawNode.SELECTED_COLOR);
                    secondNode = node;
                    edgeCreated = true;
                }
            }
        }

        private void addNodeOnMousePos(Point mousePosition, ArrayList<Node> nodes) {
            for (Node node : nodes) {
                if (node.getView().getBoundingRect().contains(mousePosition)) {
                    addNode(node);
                    break;
                }
            }
        }

        private Edge getEdgeIfNotExists() {
            if (!firstNode.getNeighbours().contains(secondNode)) {
                Edge newEdge = new Edge(firstNode, secondNode, IS_DIRECTED_CONNECTION);

                clear();

                return newEdge;
            } else {
                secondNode.getView().setColor(DrawNode.BASIC_COLOR);
                secondNode = null;
                edgeCreated = false;

                return null;
            }
        }


        public void clear() {
            if (firstNode != null) firstNode.getView().setColor(DrawNode.BASIC_COLOR);
            if (secondNode != null) secondNode.getView().setColor(DrawNode.BASIC_COLOR);

            firstNode = null;
            secondNode = null;
            edgeCreated = false;
        }
    }
}
