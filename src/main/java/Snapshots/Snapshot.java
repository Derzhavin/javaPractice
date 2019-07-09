package Snapshots;

import graphComponents.Edge;
import graphComponents.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Snapshot {
    private ArrayList<Node> singleNodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    public Snapshot(ArrayList<Node> graphNodes, ArrayList<Edge> graphEdges) {
        HashMap<Node, Node> checkedNodes = new HashMap<>();

        for (Node node : graphNodes) {
            if (node.getEdges().isEmpty()) {
                singleNodes.add(new Node(node));
            }
        }

        for (Edge edge : graphEdges) {
            Edge newEdge = new Edge(edge, checkedNodes);
            checkedNodes.put(edge.getNodes().get(0), newEdge.getNodes().get(0));
            checkedNodes.put(edge.getNodes().get(1), newEdge.getNodes().get(1));

            edges.add(newEdge);
        }
    }

    public ArrayList<Node> getSingleNodes() {
        return singleNodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
