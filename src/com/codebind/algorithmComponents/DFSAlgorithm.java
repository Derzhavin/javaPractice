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
        stepsColorDataBase.add(new StepsColorDataBase(graph, "first DFS step"));
    }

    @Override
    public void doForwardStep() {
        if (stepsColorDataBaseIterator != (stepsColorDataBase.size() - 1)) {
            stepsColorDataBaseIterator++;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();
            return;
        }

        if (finished) {
            return;
        }

        doDFSStep();
        updatePictures();
        stepsColorDataBase.add(new StepsColorDataBase(graph,  stepsColorDataBaseIterator + 1 + "th DFS step"));
        stepsColorDataBaseIterator++;
    }

    private void doDFSStep() {
        currentNode = stack.peek();
        nodes.get(currentNode).setVisited(true);

        ArrayList<Node> neighbours = currentNode.getSmartNeighbours();

        for (Node neighbour : neighbours) {
            if (!nodes.get(neighbour).isVisited) {
                edges.add(currentNode.getEdge(neighbour));
                stack.push(neighbour);
                return;
            }
        }

        stack.pop();

        if (stack.isEmpty()) {
            finished = true;
        }
    }


    @Override
    public void doBackwardStep() {
        if (stepsColorDataBaseIterator != 0) {
            stepsColorDataBaseIterator--;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();
        }
    }

    private void updatePictures() {
        for (Node node : nodes.keySet()) {
            if (nodes.get(node).isVisited) {
                node.getView().setColor(DrawNode.VISITED_COLOR);
            }
        }

        for (Edge edge : edges) {
            edge.getView().setColor(DrawEdge.VISITED_COLOR);
        }

        if (!stack.isEmpty()) {
            stack.peek().getView().setColor(DrawNode.SELECTED_COLOR);
        }
    }


    @Override
    public void reset() {
        super.reset();

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
