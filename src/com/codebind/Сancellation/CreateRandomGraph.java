package com.codebind.Сancellation;

import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.GraphEventManager;

public class CreateRandomGraph extends Command {

    public CreateRandomGraph() {super();}

    @Override
    void recover(Graph graph) {
        GraphEventManager.getInstance().removeGraph();
        setFinished(true);
    }
}
