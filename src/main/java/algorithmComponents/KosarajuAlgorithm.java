package algorithmComponents;

import ViewCompomemts.DrawEdge;
import ViewCompomemts.DrawNode;
import graphComponents.Edge;
import graphComponents.Graph;
import graphComponents.Node;

import java.util.*;
import java.util.concurrent.Callable;
import java.awt.Color;

public class KosarajuAlgorithm extends Algorithm {
    private static final int INIT_STEPS_COUNT = 1;

    private Node startNode;
    private Node currentNode;

    private int UnVisitedCounter;
    private boolean edgesTransposed = false;
    private Color colorOfComponent;
    private String stageOfAlgorithm = "doDFSstep";
    private Random random = new Random(System.currentTimeMillis());

    private HashMap<Node, NodeWrapper> nodes = new HashMap<>();
    private ArrayList<Edge> edges = new ArrayList<>();

    private Stack<Node> stack = new Stack<>();
    private Stack<Node> timeOutList = new Stack<>();

    public KosarajuAlgorithm() {
        this.startNode = null;
        this.currentNode = null;
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
        stepsColorDataBase.add(new KosarajuStepsColorDataBase(graph, "first DFS step"));
        UnVisitedCounter--;
    }

    @Override
    public void doForwardStep() {
        if (stepsColorDataBaseIterator != (stepsColorDataBase.size() - 1)) {
            stepsColorDataBaseIterator++;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();

            if (((KosarajuStepsColorDataBase)stepsColorDataBase.get(stepsColorDataBaseIterator)).transposed) {
                transpose();
            }

            return;
        }

        if (finished) {
            return;
        }

        String currentStageOfAlgorithm = stageOfAlgorithm;

        switch (stageOfAlgorithm) {
            case "doDFSstep":
                doDFSstep();
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
        stepsColorDataBase.add(new KosarajuStepsColorDataBase(graph,
                currentStageOfAlgorithm.equals("doTransposeStep"), stepsColorDataBaseIterator + 1 + "th " + currentStageOfAlgorithm));
        stepsColorDataBaseIterator++;
    }

    @Override
    public void doBackwardStep() {
        if (stepsColorDataBaseIterator != 0) {
            if (stageOfAlgorithm.equals("doDFSstep")) {
                UnVisitedCounter++;
            }

            if (((KosarajuStepsColorDataBase)stepsColorDataBase.get(stepsColorDataBaseIterator)).transposed) {
                transpose();
            }

            System.out.println(stepsColorDataBaseIterator);
            stepsColorDataBaseIterator--;
            stepsColorDataBase.get(stepsColorDataBaseIterator).resetColors();
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
                UnVisitedCounter--;
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
        stageOfAlgorithm = "doInitializationSearchComponentsStep";
    }

    private void doSearchComponentsStep() {
        if (timeOutList.isEmpty()) {
            System.out.println("FINISH!!!");
            finished = true;
            return;
        }

        if (stack.empty()) {
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
    }

    @Override
    public void reset() {
        super.reset();

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


    public class KosarajuStepsColorDataBase extends StepsColorDataBase {
        public boolean transposed = false;

        public KosarajuStepsColorDataBase(Graph graph, boolean transposed, String stepNmae) {
            super(graph, stepNmae);
            this.transposed = transposed;
        }

        public KosarajuStepsColorDataBase(Graph graph, String stepName) {
            this(graph, false, stepName);
        }

        @Override
        public void resetColors() {
            super.resetColors();
        }
    }
}
