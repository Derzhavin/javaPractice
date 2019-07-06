package com.codebind.Сancellation;

import com.codebind.GraphicsPanel;
import com.codebind.graphComonents.*;
import com.codebind.viewComponents.DrawGraph;

import java.util.ArrayList;
import java.util.Stack;

public class Buffer {
    private Stack<Command> commands = new Stack<>();
    private GraphicsPanel graphicsPanel;
    private Graph graph;
    private boolean gettingComand = false;

    public boolean isStillGettingCommand() {return gettingComand;}

    public void setGettingComand(boolean gettingComand) {
        this.gettingComand = gettingComand;
    }

    public void setGraphicsPanel(GraphicsPanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
    }

    public void setGraph(Graph graph) {this.graph = graph;}

    public void recover() {
        if (!commands.empty()) {
            Command command = commands.peek();

            command.recover(graph);

            if (command instanceof ClearScene) {
                graphicsPanel.setGraph(new DrawGraph(((ClearScene) command).recover()));
                ((ClearScene)command).setFinished(true);
            }

            if (command.isWholeCommandFinished()) {
                commands.pop();
            }

            graphicsPanel.updatePanelNodesEdges();
            graphicsPanel.repaint();
        }
    }

    public void updateBufferTopAdding(Node node) {
        if (!commands.empty()) {

            Command command = commands.peek();

            if (command instanceof AddNodes) {
                AddNodes addNodes = (AddNodes) commands.pop();
                addNodes.addNode(node);
                commands.push(addNodes);
                return;
            }
        }

        AddNodes addNodes = new AddNodes();
        addNodes.addNode(node);

        commands.push(addNodes);

        gettingComand = false;
    }

    public void updateBufferTopAdding(Edge edge) {
        if (!commands.empty()) {

            Command command = commands.peek();

            if (command instanceof AddEdges) {
                AddEdges addEdges = (AddEdges) commands.pop();
                addEdges.addEdge(edge);
                commands.push(addEdges);
                return;
            }
        }

        AddEdges addEdges = new AddEdges();
        addEdges.addEdge(edge);

        commands.push(addEdges);

        gettingComand = false;
    }

    public void updateBufferTopRemoving(Node node) {
        if (!commands.empty()) {

            Command command = commands.peek();

            if (command instanceof RemoveNodes) {
                RemoveNodes removeNodes = (RemoveNodes) commands.pop();
                removeNodes.addNode(node);
                commands.push(removeNodes);
                return;
            }
        }

        RemoveNodes removeNodes = new RemoveNodes();
        removeNodes.addNode(node);

        commands.push(removeNodes);

        gettingComand = false;
    }

    public void updateBufferTopRemoving(ArrayList<Edge> edges) {
        if (!commands.empty()) {

            Command command = commands.peek();

            if (command instanceof RemoveEdges) {
                RemoveEdges removeEdges = (RemoveEdges) commands.pop();
                removeEdges.addEdge(edges);
                commands.push(removeEdges);
                return;
            }
        }

        RemoveEdges removeEdges = new RemoveEdges();
        removeEdges.addEdge(edges);

        commands.push(removeEdges);

        gettingComand = false;
    }

    public void updateBufferTopAddingAllNodes(ArrayList<Edge> edges) {
        if (!commands.empty()) {

            Command command = commands.peek();

            if (command instanceof ConnectAllNodes) {
                ConnectAllNodes connectAllNodes = (ConnectAllNodes) commands.pop();
                connectAllNodes.addEdges(edges);
                commands.push(connectAllNodes);
                return;
            }
        }

        ConnectAllNodes connectAllNodes = new ConnectAllNodes();
        connectAllNodes.addEdges(edges);

        commands.push(connectAllNodes);

        gettingComand = false;
    }

    public  void updateBufferTopCreateRandomGraph() {commands.push(new CreateRandomGraph());gettingComand = false;}

    public void updateBufferTopClearScene(Graph graph) {commands.push(new ClearScene(new Graph(graph)));gettingComand = false;}
  }
