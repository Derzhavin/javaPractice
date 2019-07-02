package com.codebind.algorithmComponents;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.Node;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;


public class DFSAlgorithm implements Algorithm {
    private static final int INIT_STEPS_COUNT = 1;
    private static final int TIMER_BASIC_DELAY = 400;
    private boolean initialized = false;
    private int currentStep = 0;
    private Graph graph;
    private Node startNode;
    private Node currentNode;

    private GraphicsPanel graphicsPanel;

    private Stack<Node> stack = new Stack<>();
    private HashMap<Node, AlgorithmNodeStructure> nodes = new HashMap<>();
    private Timer timer;

    public DFSAlgorithm() {
        this.graphicsPanel = null;
        this.graph = null;
        this.startNode = null;
        this.currentNode = null;
        this.timer = new Timer(TIMER_BASIC_DELAY, e->doStep());
    }

    public void setDealy(int dealy) {
        this.timer.setDelay(dealy);
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

    public boolean isInitialized() {
        return initialized;
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
                currentNode.getEdge(neighbour).getView().setColor(Color.MAGENTA);
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

        currentNode.getView().setColor(Color.GREEN);
    }

    @Override
    public void doWhile(Callable<?> func) {

    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void setGraphicsPanel(GraphicsPanel panel) {
        this.graphicsPanel = panel;
    }

    @Override
    public void reset() {
        currentStep = 0;
        initialized = false;
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
}
