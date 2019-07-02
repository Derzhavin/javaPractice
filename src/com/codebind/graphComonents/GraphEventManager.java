package com.codebind.graphComonents;

import com.codebind.viewComponents.DrawDirectedEdge;
import com.codebind.viewComponents.DrawEdge;
import com.codebind.viewComponents.DrawNode;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GraphEventManager {
    private Graph graph;
    private GraphStates graphState;
    private DraggData draggData;
    private ConnectData connectData;
    private DeleteData deleteData;
    private Point oldDragPoint;

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

    public void setState(GraphStates state) {
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

    public ArrayList<Point> getScissors() {
        ArrayList<Point> pair = new ArrayList<>();
        pair.add(deleteData.firstPoint);
        pair.add(deleteData.secondPoint);

        return pair;
    }

    private GraphEventManager() {
        graph = null;
        graphState = GraphStates.NOTHING;
        draggData = new DraggData();
        connectData = new ConnectData();
        deleteData = new DeleteData();
    }

    public void mousePressed(MouseEvent mouseEvent) {
        oldDragPoint = mouseEvent.getPoint();

        switch (graphState) {
            case CREATE_NODE:
                DrawNode nodeView = new DrawNode(new Point2D.Double(mouseEvent.getX(), mouseEvent.getY()));
                graph.add(new Node(nodeView));
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
                            graph.add(newEdge);
                        }
                    }
                }
                break;
            case MOVE_NODE:
                draggData.grabNodeOnMousePos(mouseEvent.getPoint(), graph.getNodes());
                break;
            case NOTHING:
                break;
            case DELETE_NODE:
                deleteData.setFirstPoint(mouseEvent.getPoint());
                deleteData.secondPoint = mouseEvent.getPoint();

                for (Node node : graph.getNodes()) {
                    if (node.getView().getBoundingRect().contains(mouseEvent.getPoint())) {
                        graph.remove(node);
                        graph.removeAll(node.getEdges());
                        node.destroy();

                        break;
                    }
                }
                break;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e, Point2D.Double center) {
        double scale = 1D;

        if (e.getPreciseWheelRotation() < 0) {
            scale -= 0.1;
        } else {
            scale += 0.1;
        }

        DrawNode.scale *= scale;

        for(Node node: graph.getNodes()) {
            Point2D.Double nodePoint2D = node.getView().getPosition();

            if (center.y > nodePoint2D.y)
                nodePoint2D.y = center.y - Math.abs(center.y - nodePoint2D.y) * scale;
            else if (center.y < nodePoint2D.y)
                nodePoint2D.y = center.y + Math.abs(center.y - nodePoint2D.y) * scale;

            if (center.x > nodePoint2D.x)
                nodePoint2D.x = center.x - Math.abs(center.x - nodePoint2D.x) * scale;
            else if (center.x < nodePoint2D.x)
                nodePoint2D.x = center.x + Math.abs(center.x - nodePoint2D.x) * scale;
        }
    }
    public void mouseReleased(MouseEvent mouseEvent) {
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

                for (Edge edge : edgesToDelete) {
                    edge.destroy();
                }

                graph.removeAll(edgesToDelete);
                break;
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        switch (graphState) {
            case CREATE_NODE:
                break;
            case CONNECT_NODE:
                break;
            case MOVE_NODE:
                if (draggData.getIsDragg()) {
                    draggData.moveNodeIfGrabed(mouseEvent.getPoint());
                } else {
                    double xMotion = mouseEvent.getX() - oldDragPoint.getX();
                    double yMotion = mouseEvent.getY() - oldDragPoint.getY();

                    for(Node node:graph.getNodes()) {
                        Point2D.Double oldPoint2D = node.getView().getPosition();
                        node.getView().moveTo(new Point2D.Double(oldPoint2D.getX()+xMotion, oldPoint2D.getY()+yMotion));
                    }
                    oldDragPoint = mouseEvent.getPoint();
                }
                break;
            case NOTHING:
                break;
            case DELETE_NODE:
                deleteData.secondPoint = mouseEvent.getPoint();
                break;
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

                firstNode.addEdge(newEdge);
                secondNode.addEdge(newEdge);

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
