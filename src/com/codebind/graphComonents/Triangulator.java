package com.codebind.graphComonents;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class Triangulator {
    public static void triangulate(Graph graph) {
        if (graph.getNodes().size() < 3) {
            return;
        }

        for (Edge edge : graph.getEdges()) {
            ArrayList<Node> pair = edge.getNodes();
            pair.get(0).removeEdge(edge);
            pair.get(1).removeEdge(edge);
        }

        graph.removeAllEdges();
        ArrayList<Node> nodes = graph.getNodes();
        Stack<Edge> stack = new Stack<>();
        int count = 2;

        nodes.sort((o1, o2) -> {
            double firstLength = Math.sqrt(Math.pow(o1.getView().getPosition().x, 2) +
                    Math.pow(o1.getView().getPosition().y, 2));
            double secondLength = Math.sqrt(Math.pow(o2.getView().getPosition().x, 2) +
                    Math.pow(o2.getView().getPosition().y, 2));

            if (firstLength < secondLength) {
                return -1;
            }
            else if (firstLength > secondLength ) {
                return 1;
            }

            return 0;
        });

        stack.push(new Edge(nodes.get(0), nodes.get(1), true));
        graph.add(stack.peek());

        while (!stack.isEmpty()) {
            Edge currentEdge = stack.pop();
            Node firstNode = currentEdge.getNodes().get(0);
            Node secondNode = currentEdge.getNodes().get(1);
            Point2D.Double currentEdgecenter = new Point2D.Double((firstNode.getView().getPosition().x + secondNode.getView().getPosition().x) / 2F,
                    (firstNode.getView().getPosition().y + secondNode.getView().getPosition().y) / 2F );

            nodes.sort((o1, o2) -> {
                double firstLength = Math.sqrt(Math.pow(o1.getView().getPosition().x - currentEdgecenter.x, 2) +
                        Math.pow(o1.getView().getPosition().y - currentEdgecenter.y, 2));
                double secondLength = Math.sqrt(Math.pow(o2.getView().getPosition().x - currentEdgecenter.x, 2) +
                        Math.pow(o2.getView().getPosition().y - currentEdgecenter.y, 2));

                if (firstLength < secondLength) {
                    return 1;
                }
                else if (firstLength > secondLength) {
                    return -1;
                }

                return 0;
            });

            for (Node bidder : nodes) {
                if (bidder == firstNode || bidder == secondNode) {
                    continue;
                }

                if (firstNode.getNeighbours().contains(bidder) && secondNode.getNeighbours().contains(bidder)) {
                    continue;
                }

                Point2D.Double A = firstNode.getView().getPosition();
                Point2D.Double B = secondNode.getView().getPosition();
                Point2D.Double C = bidder.getView().getPosition();

                double D = 2 * (A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y));
                double X = ((Math.pow(A.x, 2) + Math.pow(A.y, 2)) * (B.y - C.y) +
                        (Math.pow(B.x, 2) + Math.pow(B.y, 2)) * (C.y - A.y) +
                        (Math.pow(C.x, 2) + Math.pow(C.y, 2)) * (A.y - B.y)) / D;
                double Y = ((Math.pow(A.x, 2) + Math.pow(A.y, 2)) * (C.x - B.x) +
                        (Math.pow(B.x, 2) + Math.pow(B.y, 2)) * (A.x - C.x) +
                        (Math.pow(C.x, 2) + Math.pow(C.y, 2)) * (B.x - A.x)) / D;

                Point2D.Double triangleCircleCenter = new Point2D.Double(X, Y);
                double triangleCircleRadius = Math.sqrt(Math.pow(triangleCircleCenter.x - bidder.getView().getPosition().x, 2) +
                        Math.pow(triangleCircleCenter.y - bidder.getView().getPosition().y, 2));
                boolean spyDetected = false;

                for (Node spy : nodes) {
                    if (spy == firstNode || spy == secondNode || spy == bidder) {
                        continue;
                    }

                    double distance = Math.sqrt(Math.pow(triangleCircleCenter.x - spy.getView().getPosition().x, 2) +
                            Math.pow(triangleCircleCenter.y - spy.getView().getPosition().y, 2));

                    if (distance <= triangleCircleRadius) {
                        spyDetected = true;
                        break;
                    }
                }

                if (!spyDetected) {
                    Edge firstEdge = new Edge(firstNode, bidder, true);
                    Edge secondEdge = new Edge(secondNode, bidder, true);
                    graph.add(firstEdge);
                    graph.add(secondEdge);
                    stack.push(firstEdge);
                    stack.push(secondEdge);
                    break;
                }
            }
        }
    }
}
