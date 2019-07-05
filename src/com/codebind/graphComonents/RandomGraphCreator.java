package com.codebind.graphComonents;

import com.codebind.viewComponents.DrawGraph;
import com.codebind.viewComponents.DrawNode;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class RandomGraphCreator {
    public static DrawGraph create(int nodesCount, double edgeFrequency, int width, int height, boolean isCircular) {
        Graph graph = new Graph();
        Random random = new Random(System.currentTimeMillis());

        double angle = (2 * Math.PI) / nodesCount;
        double radius = ((3 * nodesCount) * DrawNode.BASIC_RADIUS) / Math.PI;
        Point2D.Double center = new Point2D.Double(width / 2F, height / 2F);

        for (int i = 0; i < nodesCount; i++) {
            Point2D.Double position = isCircular ? new Point2D.Double(center.x + radius * Math.cos(angle * i), center.y + radius * Math.sin(angle * i)) :
                    new Point2D.Double(Math.random() * width, Math.random() * height);

            graph.add(new Node(new DrawNode(position, String.valueOf(i))));
        }

        ArrayList<Node> graphNodes = graph.getNodes();

        for (int i = 0; i < nodesCount - 1; i++) {
            for (int j = i + 1; j < nodesCount; j++) {
                if (Math.random() <= edgeFrequency) {
                    Edge newEdge = new Edge(graphNodes.get(i), graphNodes.get(j), random.nextBoolean());
                    graph.add(newEdge);
                }
            }
        }

        return new DrawGraph(graph);
    }
}
