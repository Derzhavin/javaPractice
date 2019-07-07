package com.codebind.algorithmComponents;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.viewComponents.DrawNode;
import com.codebind.viewComponents.DrawEdge;

import java.util.*;
import java.util.concurrent.Callable;
import java.awt.Color;

public class KosarajuAlgorithm extends Algorithm {
    private static final int INIT_STEPS_COUNT = 1;

    private Node startNode;
    private Node currentNode;

    private int UnVisitedCounter;
    boolean edgesTransposed = false;
    private Color colorOfComponent;
    private int componentCounter;
    private String stageOfAlgorithm = "doDFSstep";
    private Random random = new Random(System.currentTimeMillis());

    private HashMap<Node, NodeWrapper> nodes = new HashMap<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private Stack<Node> stack = new Stack<>();
    private Stack<Node> timeOutList = new Stack<>();

    public KosarajuAlgorithm() {
        this.startNode = null;
        this.currentNode = null;
        this.componentCounter = 1;
        this.colorOfComponent = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private void wrappNodes() {
        for (Node node : graph.getNodes()) {
            nodes.put(node, new NodeWrapper());
        }
    }

    @Override
    public void initialize(Node node) {
        switch (currentStep) {
            case 0:
                this.startNode = node;
                break;
        }

        currentStep++;
        checkInitialization();
    }

    private void checkInitialization() {
        if (currentStep == INIT_STEPS_COUNT) {
            initialized = true;
            wrappNodes();
            UnVisitedCounter = nodes.size();
            doRun();
        }
    }

    @Override
    public void initialize(Edge edge) {

    }

    @Override
    public void doRun() {
        currentNode = startNode;
        nodes.get(currentNode).setVisited(true);
        stack.push(startNode);
        updatePictures();
        graphicsPanel.repaint();
        stepsColorDataBase.add(new KosarajuStepsColorDataBase(graph));
        UnVisitedCounter--;
        //timer.start();
    }

    @Override
    public void doStep() {
        if (stepsColorDataBaseIterator != (stepsColorDataBase.size() - 1)) {
            stepsColorDataBaseIterator++;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();

            if (((KosarajuStepsColorDataBase)stepsColorDataBase.get(stepsColorDataBaseIterator)).transposed) {
                transpose();
            }

            graphicsPanel.repaint();
            return;
        }

        String currentStageOfAlgorithm = stageOfAlgorithm;

        switch (stageOfAlgorithm) {
            case "doDFSstep":
                doDFSstep();
                UnVisitedCounter++;
                break;
            case "doTransposeStep":
                doTransposeStep();
                doInitializationSearchComponentsStep();
                break;
            case "doInitializationSearchComponentsStep":
                break;
            case "doSearchComponentsStep":
                doSearchComponentsStep();
                break;
        }

        System.out.println(stageOfAlgorithm);
        updatePictures();
        graphicsPanel.repaint();

        stepsColorDataBase.add(new KosarajuStepsColorDataBase(graph, currentStageOfAlgorithm.equals("doTransposeStep")));
        stepsColorDataBaseIterator++;
    }

    @Override
    public void doBackwardsStep() {
        if (stepsColorDataBaseIterator != 0) {
            if (stageOfAlgorithm.equals("doDFSstep")) {
                UnVisitedCounter--;
            }

            System.out.println(stepsColorDataBaseIterator);
            stepsColorDataBaseIterator--;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();

            if (((KosarajuStepsColorDataBase)stepsColorDataBase.get(stepsColorDataBaseIterator)).transposed) {
                transpose();
            }

            graphicsPanel.repaint();
        }
    }

    private void doInitializationSearchComponentsStep() {
        wrappNodes();

        currentNode = timeOutList.pop();
        nodes.get(currentNode).setVisited(true);
        stack.push(currentNode);

        nodes.get(currentNode).color = colorOfComponent;
        stageOfAlgorithm = "doSearchComponentsStep";

        updatePictures();
        graphicsPanel.repaint();
    }

    private void doDFSstep() {
        if (stack.isEmpty()) {
            if (UnVisitedCounter > 0) {
                for (Node node : graph.getNodes()) {
                    if (!nodes.get(node).isVisited) {
                        currentNode = node;
                        stack.push(currentNode);
                        return;
                    }
                }
            }

            stageOfAlgorithm = "doTransposeStep";
            return;
        }

        currentNode = stack.peek();
        nodes.get(currentNode).setVisited(true);

        for (Node neighbour: currentNode.getSmartNeighbours()) {
            if (!nodes.get(neighbour).isVisited) {
                edges.add(currentNode.getEdge(neighbour));
                stack.push(neighbour);
                return;
            }
        }

        timeOutList.push(currentNode);
        stack.pop();
    }

    private void updatePictures() {
        if (stageOfAlgorithm.equals("doSearchComponentsStep")) {
            for (Node node : nodes.keySet()) {
                NodeWrapper nodeWrapper = nodes.get(node);
                node.getView().setColor(nodeWrapper.color);
            }
        }
        else {
            for (Node node : nodes.keySet()) {
                if (nodes.get(node).isVisited) {
                    node.getView().setColor(DrawNode.VISITED_COLOR);
                }
            }

            for (Edge edge : edges) {
                edge.getView().setColor(DrawEdge.VISITED_COLOR);
            }
        }

        if (!stack.isEmpty()) {
            stack.peek().getView().setColor(DrawNode.SELECTED_COLOR);
        }
    }

    private void transpose() {
        for(Edge edge: graph.getEdges()) {
            edge.transpose();
        }

        edgesTransposed = !edgesTransposed;
    }

    private void doTransposeStep() {
        transpose();

        updatePictures();
        graphicsPanel.repaint();
        stageOfAlgorithm = "doInitializationSearchComponentsStep";
    }

    private void doSearchComponentsStep() {
        if (timeOutList.isEmpty()) {
            System.out.println("FINISH!!!");
            timer.stop();
            return;
        }

        if (stack.empty()) {
            componentCounter++;
            colorOfComponent = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

            currentNode = timeOutList.peek();

            while(nodes.get(currentNode).isVisited) {
                timeOutList.pop();

                if (timeOutList.isEmpty()) {
                    return;
                }

                currentNode = timeOutList.peek();
            }

            nodes.get(currentNode).setVisited(true);
            nodes.get(currentNode).color = colorOfComponent;
            stack.push(currentNode);
            return;
        }

        currentNode = stack.peek();

        for (Node neighbour: currentNode.getSmartNeighbours()) {
            if (!nodes.get(neighbour).isVisited) {
                currentNode = neighbour;
                nodes.get(currentNode).setVisited(true);
                nodes.get(currentNode).color = colorOfComponent;
                stack.push(currentNode);
                return;
            }
        }

        stack.pop();
        updatePictures();
        graphicsPanel.repaint();
    }

    @Override
    public void doWhile(Callable<?> func) {

    }

    @Override
    public void sayHello() {

    }

    @Override
    public void reset() {
        super.reset();

        if (graph != null) {
            for (Node node : graph.getNodes()) {
                node.getView().setColor(DrawNode.BASIC_COLOR);
            }

            for (Edge edge : edges) {
                edge.getView().setColor(DrawEdge.BASIC_COLOR);
            }

            if (graphicsPanel != null) {
                graphicsPanel.repaint();
            }
        }

        if (edgesTransposed) {
            transpose();
        }

        stack.clear();
        edges.clear();
        nodes.clear();
        timeOutList.clear();
        stageOfAlgorithm = "doDFSstep";
    }

    private class NodeWrapper {
        private boolean isVisited;
        public Color color = Color.MAGENTA;

        public NodeWrapper() {
            this.isVisited = false;
        }

        public void setVisited(boolean isVisited) {
            this.isVisited = isVisited;
        }
    }


    private class KosarajuStepsColorDataBase extends StepsColorDataBase {
        public boolean transposed = false;

        public KosarajuStepsColorDataBase(Graph graph, boolean transposed) {
            super(graph);
            this.transposed = transposed;
        }

        public KosarajuStepsColorDataBase(Graph graph) {
            this(graph, false);
        }

        @Override
        public void resetColors() {
            super.resetColors();
        }
    }
}
