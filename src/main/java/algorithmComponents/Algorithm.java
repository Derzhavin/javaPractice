package algorithmComponents;

import graphComponents.Edge;
import graphComponents.Graph;
import graphComponents.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

public abstract class Algorithm {
    protected boolean initialized = false;
    protected boolean finished = false;
    protected int currentStep = 0;

    protected Graph graph = null;
    protected ArrayList<StepsColorDataBase> stepsColorDataBase = new ArrayList<>();
    protected int stepsColorDataBaseIterator = 0;

    public abstract void initialize(Node node);
    public abstract void initialize(Edge edge);

    public boolean isInitialized() {
        return initialized;
    }

    public abstract void doRun();
    public abstract void doForwardStep();
    public abstract void doBackwardStep();

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public AlgorithmState getState() {
        int step = 0;

        if (stepsColorDataBaseIterator == 0) {
            step = 0;
        }
        else if (finished && (stepsColorDataBaseIterator == stepsColorDataBase.size() - 1)) {
            step = 2;
        }
        else {
            step = 1;
        }

        return new AlgorithmState(initialized, step, stepsColorDataBase.get(stepsColorDataBaseIterator));
    }

    public void reset() {
        stepsColorDataBaseIterator = 0;
        stepsColorDataBase.clear();

        initialized = false;
        finished = false;
        currentStep = 0;
    }

    public class AlgorithmState {
        public boolean initialized;
        public int step; //0 - first; 1 - middle; 2 - last
        public StepsColorDataBase stepsColorDataBase;

        public AlgorithmState(boolean initialized, int step, StepsColorDataBase stepsColorDataBase) {
            this.initialized = initialized;
            this.step = step;
            this.stepsColorDataBase = stepsColorDataBase;
        }
    }

    public class StepsColorDataBase {
        protected HashMap<Node, Color> nodeColorHashMap = new HashMap<>();
        protected HashMap<Edge, Color> edgeColorHashMap = new HashMap<>();
        public String stepName = "";

        public StepsColorDataBase(Graph graph, String stepName) {
            for (Node node : graph.getNodes()) {
                nodeColorHashMap.put(node, node.getView().getColor());
            }

            for (Edge edge : graph.getEdges()) {
                edgeColorHashMap.put(edge, edge.getView().getColor());
            }

            this.stepName = stepName;
        }

        public void resetColors() {
            for (Node node : nodeColorHashMap.keySet()) {
                node.getView().setColor(nodeColorHashMap.get(node));
            }

            for (Edge edge : edgeColorHashMap.keySet()) {
                edge.getView().setColor(edgeColorHashMap.get(edge));
            }
        }
    }
}