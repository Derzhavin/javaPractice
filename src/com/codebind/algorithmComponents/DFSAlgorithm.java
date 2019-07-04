package com.codebind.algorithmComponents;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Node;
import com.codebind.viewComponents.DrawEdge;
import com.codebind.viewComponents.DrawNode;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;


public class DFSAlgorithm extends Algorithm {
    private static final int INIT_STEPS_COUNT = 1;

    private Node startNode;
    private Node currentNode;

    private Stack<Node> stack = new Stack<>();
    private HashMap<Node, AlgorithmNodeStructure> nodes = new HashMap<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    public DFSAlgorithm() {
        this.startNode = null;
        this.currentNode = null;
    }

    private void checkInitialization() {
        if (currentStep == INIT_STEPS_COUNT) {
            initialized = true;
            createStructures();
            doRun();
        }
    }

    private void createStructures() {
        for (Node node : graph.getNodes()) {
            nodes.put(node, new AlgorithmNodeStructure());
        }
    }

    @Override
    public void initialize(Node node) {
        switch (currentStep) {
            case 0:
                this.startNode = node;
                break;
        }

        currentStep++;
        checkInitialization();
    }

    @Override
    public void initialize(Edge edge) {
        currentStep++;
        checkInitialization();
    }

    @Override
    public void doRun() {
        currentNode = startNode;
        nodes.get(currentNode).setVisited(true);
        stack.push(startNode);
        updatePictures();
        graphicsPanel.repaint();

        timer.start();
    }

    @Override
    public void doStep() {
        if (stack.isEmpty()) {
            timer.stop();
            return;
        }

        currentNode = stack.peek();
        nodes.get(currentNode).setVisited(true);

        ArrayList<Node> neighbours = currentNode.getSmartNeighbours();

        for (Node neighbour : neighbours) {
            if (!nodes.get(neighbour).isVisited) {
                edges.add(currentNode.getEdge(neighbour));
                stack.push(neighbour);
                updatePictures();
                graphicsPanel.repaint();
                return;
            }
        }

        stack.pop();
        updatePictures();
        graphicsPanel.repaint();
    }

    private void updatePictures() {
        for (Node node : nodes.keySet()) {
            if (nodes.get(node).isVisited) {
                node.getView().setColor(Color.MAGENTA);
            }
        }

        for (Edge edge : edges) {
            edge.getView().setColor(Color.red);
        }

        if (!stack.isEmpty()) {
            stack.peek().getView().setColor(Color.GREEN);
        }
    }

    @Override
    public void doWhile(Callable<?> func) {

    }

    @Override
    public void sayHello() {

    }

    @Override
    public void reset() {
        super.reset();

        if (graph != null) {
            for (Node node : graph.getNodes()) {
                node.getView().setColor(DrawNode.BASIC_COLOR);
            }

            for (Edge edge : edges) {
                edge.getView().setColor(DrawEdge.BASIC_COLOR);
            }

            if (graphicsPanel != null) {
                graphicsPanel.repaint();
            }
        }

        stack.clear();
        nodes.clear();
        edges.clear();
    }

    private class AlgorithmNodeStructure {
        private boolean isVisited;

        public AlgorithmNodeStructure() {
            this.isVisited = false;
        }

        public void setVisited(boolean isVisited) {
            this.isVisited = isVisited;
        }
    }

    private class AlgorithmEdgeStructure {
        private boolean isVisited;

        public AlgorithmEdgeStructure() {
            this.isVisited = false;
        }

        public void setVisited(boolean isVisited) {
            this.isVisited = isVisited;
        }
    }
}
