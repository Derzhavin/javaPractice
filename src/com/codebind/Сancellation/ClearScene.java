package com.codebind.Сancellation;

import com.codebind.graphComonents.Graph;

public class ClearScene extends Command {
    private Graph graph;

    public ClearScene(Graph graph) {
        super();
        this.graph = graph;
    }

    @Override
    public void recover(Graph graph) {}

    @Override
    public void free() {graph = null;}

    public Graph recover() {return this.graph;}
}
