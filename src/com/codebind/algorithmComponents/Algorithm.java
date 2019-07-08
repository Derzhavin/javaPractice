package com.codebind.algorithmComponents;

import com.codebind.Algorithms;
import com.codebind.ButtonState;
import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public abstract class Algorithm {
    protected static final int TIMER_BASIC_DELAY = 400;
    protected boolean initialized = false;
    protected boolean finished = false;
    protected int currentStep = 0;
    protected Timer timer = new Timer(TIMER_BASIC_DELAY, e->doStep());

    protected Graph graph = null;
    protected GraphicsPanel graphicsPanel = null;
    protected ArrayList<StepsColorDataBase> stepsColorDataBase = new ArrayList<>();
    protected int stepsColorDataBaseIterator = 0;

    public abstract void initialize(Node node);
    public abstract void initialize(Edge edge);

    public boolean isInitialized() {
        return initialized;
    }

    public abstract void doRun();
    public abstract void doStep();
    public abstract void doBackwardsStep();
    public abstract void doWhile(Callable<?> func);

    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setGraphicsPanel(GraphicsPanel panel) {
        this.graphicsPanel = panel;
    }

    public abstract void sayHello();

    public abstract void goToEnd();

    public void stop() {
        timer.stop();
    }

    public void continueIfStoped() {
        if (initialized) {
            timer.start();
        }
    }

    public void reset() {
        stepsColorDataBaseIterator = 0;
        stepsColorDataBase.clear();

        timer.stop();
        initialized = false;
        finished = false;
        currentStep = 0;
    }

    protected void updateButtons() {
        if (finished && (stepsColorDataBaseIterator == stepsColorDataBase.size() - 1)) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
            Algorithms.buttonPanel.buttons.get(0).setState(ButtonState.INACTIVE);
            Algorithms.buttonPanel.buttons.get(0).changeState();
            Algorithms.buttonPanel.buttons.get(0).setEnabled(false);
            Algorithms.buttonPanel.buttons.get(2).setEnabled(false);
            Algorithms.buttonPanel.buttons.get(4).setEnabled(false);
        }
        else {
            Algorithms.buttonPanel.buttons.get(0).setEnabled(true);
            Algorithms.buttonPanel.buttons.get(2).setEnabled(true);
            Algorithms.buttonPanel.buttons.get(4).setEnabled(true);
        }

        if (stepsColorDataBaseIterator == 0) {
            Algorithms.buttonPanel.buttons.get(3).setEnabled(false);
        }
        else {
            Algorithms.buttonPanel.buttons.get(3).setEnabled(true);
        }

        if (!finished || stepsColorDataBaseIterator != (stepsColorDataBase.size() - 1)) {

        }
    }

    protected class StepsColorDataBase {
        protected HashMap<Node, Color> nodeColorHashMap = new HashMap<>();
        protected HashMap<Edge, Color> edgeColorHashMap = new HashMap<>();

        public StepsColorDataBase(Graph graph) {
            for (Node node : graph.getNodes()) {
                nodeColorHashMap.put(node, node.getView().getColor());
            }

            for (Edge edge : graph.getEdges()) {
                edgeColorHashMap.put(edge, edge.getView().getColor());
            }
        }

        public void resetColors() {
            for (Node node : nodeColorHashMap.keySet()) {
                node.getView().setColor(nodeColorHashMap.get(node));
            }

            for (Edge edge : edgeColorHashMap.keySet()) {
                edge.getView().setColor(edgeColorHashMap.get(edge));
            }
        }
    }
}