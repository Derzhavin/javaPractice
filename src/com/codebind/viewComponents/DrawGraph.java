package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.Node;

import java.awt.*;

public class DrawGraph implements Drawable {
    private Graph graph;

    public DrawGraph() {
        this.graph = new Graph();
        GraphEventManager.getInstance().setGraph(graph);
    }
    public DrawGraph(Graph graph){
        this.graph = graph;
        GraphEventManager.getInstance().setGraph(graph);
    }

    @Override
    public void draw(Graphics2D g) {
        for (Node node : graph.getNodes()) {
            node.getView().draw(g);
        }

        for (Edge edge : graph.getEdges()) {
            edge.getView().draw(g);
        }
    }
}
