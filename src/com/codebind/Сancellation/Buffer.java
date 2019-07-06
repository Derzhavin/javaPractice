package com.codebind.Сancellation;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.*;
import com.codebind.viewComponents.DrawGraph;

import java.util.ArrayList;
import java.util.Stack;

public class Buffer {
    private ArrayList<Command> commands = new ArrayList<>();
    private GraphicsPanel graphicsPanel;
    private Graph graph;
    private boolean gettingComand = false;
    private final int LIMIT_OF_ACTIONS = 5;

    public boolean isStillGettingCommand() {return gettingComand;}

    public void setGettingComand(boolean gettingComand) {
        this.gettingComand = gettingComand;
    }

    public void setGraphicsPanel(GraphicsPanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
    }

    public void setGraph(Graph graph) {this.graph = graph;}

    private void free() {commands.remove(0).free();}

    public void recover() {
        if (commands.size() != 0) {
            Command command = commands.get(commands.size()-1);

            command.recover(graph);

            if (command instanceof ClearScene) {
                graphicsPanel.setGraph(new DrawGraph(((ClearScene) command).recover()));
                ((ClearScene)command).setFinished(true);
            }

            if (command.isWholeCommandFinished()) {
                commands.remove(commands.size()-1);
            }

            graphicsPanel.updatePanelNodesEdges();
            graphicsPanel.repaint();
        }
    }

    public void updateBufferTopAdding(Node node) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }

        if (commands.size() != 0) {

            Command command = commands.get(commands.size()-1);

            if (command instanceof AddNodes) {
                AddNodes addNodes = (AddNodes) commands.remove(commands.size()-1);
                addNodes.addNode(node);
                commands.add(addNodes);
                return;
            }
        }

        AddNodes addNodes = new AddNodes();
        addNodes.addNode(node);

        commands.add(addNodes);

        gettingComand = false;
    }

    public void updateBufferTopAdding(Edge edge) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }

        if (commands.size() != 0) {

            Command command = commands.get(commands.size()-1);

            if (command instanceof AddEdges) {
                AddEdges addEdges = (AddEdges) commands.remove(commands.size()-1);
                addEdges.addEdge(edge);
                commands.add(addEdges);
                return;
            }
        }

        AddEdges addEdges = new AddEdges();
        addEdges.addEdge(edge);

        commands.add(addEdges);

        gettingComand = false;
    }

    public void updateBufferTopRemoving(Node node) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }

        if (commands.size() != 0) {

            Command command = commands.get(commands.size()-1);

            if (command instanceof RemoveNodes) {
                RemoveNodes removeNodes = (RemoveNodes) commands.remove(commands.size()-1);
                removeNodes.addNode(node);
                commands.add(removeNodes);
                return;
            }
        }

        RemoveNodes removeNodes = new RemoveNodes();
        removeNodes.addNode(node);

        commands.add(removeNodes);

        gettingComand = false;
    }

    public void updateBufferTopRemoving(ArrayList<Edge> edges) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }

        if (commands.size() != 0) {

            Command command = commands.get(commands.size()-1);

            if (command instanceof RemoveEdges) {
                RemoveEdges removeEdges = (RemoveEdges) commands.remove(commands.size()-1);
                removeEdges.addEdge(edges);
                commands.add(removeEdges);
                return;
            }
        }

        RemoveEdges removeEdges = new RemoveEdges();
        removeEdges.addEdge(edges);

        commands.add(removeEdges);

        gettingComand = false;
    }

    public void updateBufferTopAddingAllNodes(ArrayList<Edge> edges) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }

        if (commands.size() != 0) {

            Command command = commands.get(commands.size()-1);

            if (command instanceof ConnectAllNodes) {
                ConnectAllNodes connectAllNodes = (ConnectAllNodes) commands.remove(commands.size()-1);
                connectAllNodes.addEdges(edges);
                commands.add(connectAllNodes);
                return;
            }
        }

        ConnectAllNodes connectAllNodes = new ConnectAllNodes();
        connectAllNodes.addEdges(edges);

        commands.add(connectAllNodes);

        gettingComand = false;
    }

    public void updateBufferTopClearScene(Graph graph) {
        if (commands.size() > LIMIT_OF_ACTIONS) {
            free();
        }
        commands.add(new ClearScene(new Graph(graph)));
        gettingComand = false;
    }
  }
