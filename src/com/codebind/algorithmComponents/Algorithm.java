package com.codebind.algorithmComponents;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import javax.swing.*;
import java.util.concurrent.Callable;

public abstract class Algorithm {
    protected static final int TIMER_BASIC_DELAY = 400;
    protected boolean initialized = false;
    protected int currentStep = 0;
    protected Timer timer = new Timer(TIMER_BASIC_DELAY, e->doStep());

    protected Graph graph = null;
    protected GraphicsPanel graphicsPanel = null;


    public abstract void initialize(Node node);
    public abstract void initialize(Edge edge);

    public  boolean isInitialized() {
        return initialized;
    }

    public abstract void doRun();
    public abstract void doStep();
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

    public void stop() {
        timer.stop();
    }

    public void continueIfStoped() {
        if (initialized) {
            timer.restart();
        }
    }

    public void reset() {
        timer.stop();
        initialized = false;
        currentStep = 0;
    }
}
