package com.codebind.Сancellation;

import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import java.util.ArrayList;
import java.util.Stack;

public class AddNodes extends Command {
    private ArrayList<Node> nodes = new ArrayList<>();

    public AddNodes() {super();}
    public void addNode(Node node){
        nodes.add(node);
    }

    @Override
    public void recover(Graph graph) {
        graph.remove(nodes.remove(nodes.size()-1));

        if (nodes.size() == 0) {
            setFinished(true);
        }
    }

    @Override
    public void free() {nodes.remove(nodes.size()-1).destroy();}
}
