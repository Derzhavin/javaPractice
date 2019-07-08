package com.codebind.viewComponents;

import com.codebind.Shapes.Drawable;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.Managers.GraphEventManager;
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

    public void resetColors() {
        for (Node node : graph.getNodes()) {
            node.getView().setColor(DrawNode.BASIC_COLOR);
        }

        for (Edge edge : graph.getEdges()) {
            edge.getView().setColor(DrawEdge.BASIC_COLOR);
        }
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

    public void print(Graphics2D g) {
        for (Node node : graph.getNodes()) {
            node.getView().print(g);
        }
    }
}
