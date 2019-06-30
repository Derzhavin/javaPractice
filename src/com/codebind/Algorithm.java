package com.codebind;

import java.util.concurrent.Callable;

public interface Algorithm {
    void doRun(Integer stepsCount);
    void doStep();
    void doWhile(Callable<?> func);
    void setGraph(Graph graph);

    void reset();
}
