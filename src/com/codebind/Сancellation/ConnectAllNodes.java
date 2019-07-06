package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;

import java.util.ArrayList;

public class ConnectAllNodes extends Command {
    private ArrayList<Edge> edges;

    public ConnectAllNodes() {super();}
    public void addEdges(ArrayList<Edge> edges) {this.edges = edges;}

    @Override
    void recover(Graph graph) {
        for(int i = edges.size() - 1; i > -1; i--) {
            graph.remove(edges.get(i));
            edges.get(i).destroy();
        }

        setFinished(true);
    }
}
