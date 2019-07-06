package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.Сancellation.MyCollections.Quadruple;

import java.util.ArrayList;
import java.util.Stack;

public class RemoveNodes extends Command {
    ArrayList<Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>>> quadruples= new ArrayList<>();

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
        quadruples.add(quadruple);
    }

    @Override
    void recover(Graph graph) {

        Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>> quadruple = quadruples.remove(quadruples.size()-1);

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

        if (quadruples.size() == 0) {
            setFinished(true);
        }
    }

    @Override
    public void free() {
        for(int i = quadruples.size()-1; i > -1; i--) {
            Quadruple<Node, ArrayList<Node>, ArrayList<Node>, ArrayList<Node>> quadruple = quadruples.remove(i);
            quadruple.first.destroy();
            quadruple.second.clear();
            quadruple.third.clear();
            quadruple.fourth.clear();
        }
    }
}
