package com.codebind.algorithmComponents;

import com.codebind.graphComonents.Graph;

import java.util.concurrent.Callable;

public interface Algorithm {
    void doRun();
    void doStep();
    void doWhile(Callable<?> func);
    void setGraph(Graph graph);

    void reset();
}
