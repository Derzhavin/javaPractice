package com.codebind.Сancellation;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.Сancellation.MyCollections.Triplet;

import java.util.ArrayList;
import java.util.Stack;

// В триплете первый Node - source, второй - dest.
//Почему-то если добавлять edge и в source, и в dest,
//не работает (как будто, попав в один node,
//edge уже появляется во 2-ом). It's a kind of magic.

public class RemoveEdges extends Command {
    ArrayList<Triplet<Edge, Node, Node>> triplets = new ArrayList<>();
    ArrayList<Integer> countsEdgesToRecover = new ArrayList<>();

    public RemoveEdges() {super();}

    public void addEdge(ArrayList<Edge> edges) {
        for(Edge edge: edges) {
            Triplet<Edge, Node, Node> triplet = null;
            ArrayList<Node> nodes = edge.getNodes();

            if (!edge.isDirected()) {
                triplet = new Triplet<>(edge, nodes.get(0), nodes.get(1));
            } else {
                if (edge.getSmartNeighbour(nodes.get(0)) != null) {
                    triplet = new Triplet<>(edge, nodes.get(0), nodes.get(1));
                } else {
                    triplet = new Triplet<>(edge, nodes.get(1), nodes.get(0));
                }
            }

            triplets.add(triplet);
        }

        countsEdgesToRecover.add(edges.size());
    }

    @Override
    void recover(Graph graph) {
        //System.out.println("RemoveEdges_recover");

        Integer countEdgesToRecover = countsEdgesToRecover.remove(countsEdgesToRecover.size()-1);

        for(int i = countEdgesToRecover; i > 0; i--) {
            Triplet<Edge, Node, Node> triplet = triplets.remove(triplets.size()-1);

            Edge edge = triplet.first;
            Node sourseNode = triplet.second;
            Node destNode = triplet.third;

            //sourseNode.addEdge(edge);
            destNode.addEdge(edge);


            graph.add(edge);

            if (triplets.size()== 0) {
                setFinished(true);
            }
        }
    }

    @Override
    public void free() {
        Integer countEdgesToRecover = countsEdgesToRecover.remove(countsEdgesToRecover.size()-1);

        for(int i = triplets.size()-1; countEdgesToRecover > 0; i--, countEdgesToRecover--) {
            Triplet<Edge, Node, Node> triplet = triplets.remove(i);

            triplet.first.destroy();
            triplet.second.destroy();
            triplet.third.destroy();
        }
    }
}
