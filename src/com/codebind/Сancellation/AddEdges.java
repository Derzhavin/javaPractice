package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;

import java.util.ArrayList;

public class AddEdges extends Command {
    private ArrayList<Edge> edges = new ArrayList<>();

    public AddEdges() {super();}

    void addEdge(Edge edge) {edges.add(edge);}

    @Override
    public void recover(Graph graph) {
        Edge edge = edges.remove(edges.size()-1);

        graph.remove(edge);

        edge.destroy();

        if (edges.size() == 0) {
            setFinished(true);
        }
    }

    @Override
    public void free() {edges.remove(edges.size()-1).destroy();}
}
