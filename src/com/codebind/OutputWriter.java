package com.codebind;

import com.codebind.graphComonents.Edge;
import com.codebind.graphComonents.Graph;
import com.codebind.graphComonents.Node;
import com.codebind.viewComponents.DrawNode;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputWriter {
    
    public void saveGraph(Graph graph){
        ArrayList<Node> nodes = graph.getNodes();
        StringBuilder[] res = new StringBuilder[nodes.size()+1];
        for (int i = 0; i < res.length; i++) {
            res[i] = new StringBuilder("");
        }
        res[0] = new StringBuilder("Location: by coordinates");
        for(int i = 0; i<nodes.size();i++){
            Node node = nodes.get(i);
            res[i+1].append(node.getView().getName() + "(" + (int)node.getView().getPosition().getX() + ";"
                    + (int)node.getView().getPosition().getY() + ") ");
            ArrayList<Edge> edges = node.getEdges();
            for(Edge edge : edges){
                Node destNode = edge.getSmartNeighbour(node);
                if (destNode != null){
                    res[i+1].append(destNode.getView().getName()+ " ");
                }
            }
        }

        JFileChooser savefile = new JFileChooser(new File("./GraphExamples"));
        int ret = savefile.showSaveDialog(null);
        if(ret == JFileChooser.APPROVE_OPTION){
            File file = savefile.getSelectedFile();
            try(FileWriter writer = new FileWriter(file, false)) {
                for (StringBuilder string: res){
                    writer.append(string.toString() + System.lineSeparator());

                }
            }
            catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
