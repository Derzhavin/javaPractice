package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;

import java.util.Stack;

public class AddEdges extends Command {
    private Stack<Edge> edges = new Stack<>();

    public AddEdges() {super();}

    void addEdge(Edge edge) {edges.push(edge);}

    @Override
    public void recover(Graph graph) {
        Edge edge = edges.pop();

        graph.remove(edge);

        edge.destroy();

        if (edges.empty()) {
            setFinished(true);
        }
    }
}
