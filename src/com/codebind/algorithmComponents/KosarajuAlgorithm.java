package com.codebind.algorithmComponents;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.viewComponents.DrawNode;
import com.codebind.viewComponents.DrawEdge;

import java.util.*;
import java.util.concurrent.Callable;
import java.awt.Color;
import javax.swing.Timer;

public class KosarajuAlgorithm implements Algorithm {
    private static final int INIT_STEPS_COUNT = 1;
    private static final int TIMER_BASIC_DELAY = 500;
    private static final int DELTA_COLOR = 80;
    private boolean initialized = false;
    private int currentStep = 0;
    private Graph graph;
    private Node startNode;
    private Node currentNode;
    private Color colorOfComponent;
    private GraphicsPanel graphicsPanel;
    private int componentCounter;
    private String stageOfAlgorithm = "doDFSstep";
    private Random random;

    private HashMap<Node, NodeWrapper> nodes = new HashMap<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    //private HashSet<Node> components[] = null;

    private Stack<Node> stack = new Stack<>();
    private Stack<Node> timeOutList = new Stack<>();
    private Timer timer;

    public KosarajuAlgorithm() {
        this.graphicsPanel = null;
        this.graph = null;
        this.startNode = null;
        this.currentNode = null;
        this.timer = new Timer(TIMER_BASIC_DELAY, e->doStep());
        this.componentCounter = 1;
        this.colorOfComponent = new Color(componentCounter*DELTA_COLOR,componentCounter*DELTA_COLOR,componentCounter*DELTA_COLOR);
        random = new Random(System.currentTimeMillis());
    }

    private void wrappNodes() {
        for (Node node : graph.getNodes()) {
            nodes.put(node, new NodeWrapper());
        }
    }

    @Override
    public boolean isInitialized() {
        return false;
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
        timer.start();
    }

    @Override
    public void doStep() {
        switch (stageOfAlgorithm) {
            case "doDFSstep":
                doDFSstep();
                break;
            case "doTransposeStep":
                for(Node node:timeOutList) {
                    System.out.print(node.getView().getName() + " ");
                    System.out.println();
                }
                doTransposeStep();
                break;
            case "doInitializationSearchComponentsStep":
                doInitializationSearchComponentsStep();
                break;
            case "doSearchComponentsStep":
                doSearchComponentsStep();
                break;
        }

        System.out.println(stageOfAlgorithm);
    }

    private void doInitializationSearchComponentsStep() {
        wrappNodes();

        currentNode = timeOutList.pop();
        nodes.get(currentNode).setVisited(true);
        stack.push(currentNode);

        updatePictures();
        currentNode.getView().setColor(colorOfComponent);
        graphicsPanel.repaint();

        stageOfAlgorithm = "doSearchComponentsStep";
    }

    private void doDFSstep() {
        if (stack.isEmpty()) {
            for(Node node: graph.getNodes()) {
                if (!nodes.get(node).isVisited) {
                    currentNode = node;
                    stack.push(currentNode);
                    updatePictures();
                    graphicsPanel.repaint();
                    return;
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
                updatePictures();
                graphicsPanel.repaint();
                return;
            }
        }

        timeOutList.push(currentNode);
        stack.pop();

        updatePictures();
        graphicsPanel.repaint();
    }

    private void updatePictures() {
        for (Node node : nodes.keySet()) {
            if (nodes.get(node).isVisited) {
                node.getView().setColor(Color.MAGENTA);
            }
        }

        for (Edge edge : edges) {
            edge.getView().setColor(Color.red);
        }

        if (!stack.isEmpty()) {
            stack.peek().getView().setColor(Color.GREEN);
        }
    }

    private void doTransposeStep() {
        for(Edge edge: graph.getEdges()) {
            edge.transpose();
        }

        graphicsPanel.repaint();
        stageOfAlgorithm = "doInitializationSearchComponentsStep";
    }

    private void doSearchComponentsStep() {
        if (timeOutList.isEmpty()) {
            doTransposeStep();
            System.out.println("FINISH!!!");
            reset();
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

            currentNode.getView().setColor(colorOfComponent);
            nodes.get(currentNode).setVisited(true);
            stack.push(currentNode);

            graphicsPanel.repaint();
            return;
        }

        currentNode = stack.peek();

        for (Node neighbour: currentNode.getSmartNeighbours()) {
            if (!nodes.get(neighbour).isVisited) {
                currentNode = neighbour;
                currentNode.getView().setColor(colorOfComponent);
                nodes.get(currentNode).setVisited(true);
                stack.push(currentNode);

                graphicsPanel.repaint();
                return;
            }
        }

        stack.pop();
    }

    @Override
    public void doWhile(Callable<?> func) {

    }

    @Override
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void setGraphicsPanel(GraphicsPanel panel) {
        this.graphicsPanel = panel;
    }

    @Override
    public void setDelay(int delay) {
        this.timer.setDelay(delay);
    }


    @Override
    public void reset() {
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

        stack.clear();
        edges.clear();
        nodes.clear();
        timeOutList.clear();
        timer.stop();
        currentStep = 0;
        initialized = false;
        stageOfAlgorithm = "doDFSstep";
    }

    private class NodeWrapper {
        private boolean isVisited;

        public NodeWrapper() {
            this.isVisited = false;
        }

        public void setVisited(boolean isVisited) {
            this.isVisited = isVisited;
        }
    }
}
