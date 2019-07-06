package com.codebind.Сancellation;

import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;

import java.util.Stack;

public class AddNodes extends Command {
    private Stack<Node> nodes = new Stack<>();

    public AddNodes() {super();}
    public void addNode(Node node){
        nodes.push(node);
    }

    @Override
    public void recover(Graph graph) {
        graph.remove(nodes.pop());

        if (nodes.empty()) {
            setFinished(true);
        }
    }
}
