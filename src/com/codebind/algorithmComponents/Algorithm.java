package com.codebind.algorithmComponents;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import java.util.concurrent.Callable;

public interface Algorithm {
    void initialize(Node node);
    void initialize(Edge edge);
    boolean isInitialized();
    void doRun();
    void doStep();
    void doWhile(Callable<?> func);
    void setGraph(Graph graph);
    void setGraphicsPanel(GraphicsPanel panel);

    void reset();
}
