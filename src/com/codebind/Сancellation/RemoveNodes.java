package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.Сancellation.MyCollections.Quadruple;

import java.util.ArrayList;
import java.util.Stack;

public class RemoveNodes extends Command {
    Stack<Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>>> quadruples= new Stack<>();

    public RemoveNodes() {super();}

    public void addNode(Node node) {
        ArrayList<Node> smartNeigbours = new ArrayList<>();

        for(Node smartNeighbour: node.getSmartNeighbours()) {
            smartNeigbours.add(smartNeighbour);
        }

        ArrayList<Node> peerNeighbours = new ArrayList<>();
        ArrayList<Node> ownerNeighbours = new ArrayList<>();

        for(Node neighbour: node.getNeighbours()) {
            if (!node.getSmartNeighbours().contains(neighbour) && !neighbour.getSmartNeighbours().contains(node)) {
                peerNeighbours.add(neighbour);
            }

            if (neighbour.getSmartNeighbours().contains(node)) {
                ownerNeighbours.add(neighbour);
            }
        }

        Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>> quadruple = new Quadruple<>(node, smartNeigbours, peerNeighbours, ownerNeighbours);
        quadruples.push(quadruple);
    }

    @Override
    void recover(Graph graph) {

        Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>> quadruple = quadruples.pop();

        Node node = quadruple.first;
        ArrayList<Node> smartNeighbours = quadruple.second;
        ArrayList<Node> peerNeighbours= quadruple.third;
        ArrayList<Node> ownerNeighbours= quadruple.fourth;

        for(Node smartNeigbour: smartNeighbours) {
            Edge edge = new Edge(node, smartNeigbour, true);
            graph.add(edge);
        }

        for(Node peerNeigbour: peerNeighbours) {
            Edge edge = new Edge(node, peerNeigbour, false);
            graph.add(edge);
        }

        for(Node ownerNeigbour: ownerNeighbours) {
            Edge edge = new Edge(ownerNeigbour, node, true);
            graph.add(edge);
        }

        graph.add(node);

        if (quadruples.empty()) {
            setFinished(true);
        }
    }
}
