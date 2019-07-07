package com.codebind.Сancellation;

import com.codebind.graphComonents.*;
import com.codebind.viewComponents.DrawGraph;

import java.util.ArrayList;

public class Buffer {
    private ArrayList<DrawGraph> graphs = new ArrayList<>();
    private final int LIMIT_OF_GRAPHS = 100;

    public DrawGraph recover() {
        if (graphs.size() != 0) {
            return  graphs.remove(graphs.size()-1);
        }
        return  null;
    }

    public void push(DrawGraph graph) {
        if (graphs.size() > LIMIT_OF_GRAPHS) {
            graphs.remove(0);
        }
        graphs.add(graph);
    }
  }
