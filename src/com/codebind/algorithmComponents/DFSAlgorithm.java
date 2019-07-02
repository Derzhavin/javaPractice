package com.codebind.algorithmComponents;

import com.codebind.Application;
import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;


public class DFSAlgorithm implements Algorithm, Initialized {
    private static final int INIT_STEPS_COUNT = 1;
    private static final int TIMER_BASIC_DELAY = 500;
    private boolean initialized = false;
    private int currentStep = 0;
    private Graph graph;
    private Node startNode;
    private Node currentNode;

    private Stack<Node> stack = new Stack<>();
    private HashMap<Node, AlgorithmNodeStructure> nodes = new HashMap<>();
    private Timer timer;

    public DFSAlgorithm(Graph graph) {
        this.graph = graph;
        this.startNode = null;
        this.currentNode = null;
        this.timer = new Timer(TIMER_BASIC_DELAY, e->doStep());
    }

    void setDealy(int dealy) {
        this.timer.setDelay(dealy);
    }

    void checkInitialization() {
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
        Application.getPanel().repaint();
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

        ArrayList<Node> neighbours = currentNode.getNeighbours();

        for (Node neighbour : neighbours) {
            if (!nodes.get(neighbour).isVisited) {
                currentNode.getEdge(neighbour).getView().setColor(Color.MAGENTA);
                stack.push(neighbour);
                updatePictures();
                Application.getPanel().repaint();
                return;
            }
        }

        stack.pop();
        updatePictures();
        Application.getPanel().repaint();
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

    }

    @Override
    public void reset() {

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
