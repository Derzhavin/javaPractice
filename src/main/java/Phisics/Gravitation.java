package Phisics;

import Managers.GraphEventManager;
import Widgets.GraphicsPanel;
import graphComponents.Graph;
import graphComponents.Node;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Gravitation {
    public static boolean GRAVITATION_ACTIVE = true;
    private static final int BASIC_UPDATE_PERIOD = 25;
    private static final Point2D.Double ACCELERATION = new Point2D.Double(0, 0.4D);
    private Timer updateTimer = new Timer(BASIC_UPDATE_PERIOD, e->update());
    private GraphicsPanel graphicsPanel;

    public Gravitation(GraphicsPanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
        updateTimer.start();
    }

    private void update() {
        if (!GRAVITATION_ACTIVE) {
            return;
        }

        Graph graph = GraphEventManager.getInstance().getGraph();
        ArrayList<Node> removeList = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            if (node.getNeighbours().size() == 0) {
                Point2D.Double nodeAcceleration = node.gravitationUpdate(ACCELERATION);
                node.getView().moveTo(new Point2D.Double(node.getView().getPosition().x +
                        nodeAcceleration.x, node.getView().getPosition().y + nodeAcceleration.y));
                removeList.add(node);
            }
        }

        graphicsPanel.repaint();
    }


}
