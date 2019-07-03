package com.codebind;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;


import com.codebind.graphComonents.*;
import com.codebind.graphComonents.Graph;
import com.codebind.viewComponents.*;

enum DataMode {
    DEFAULT,
    RANDOM,
    COORDS
}

class InputReader {
    private ArrayList<String> lines;
    private DataMode mode;
    boolean FileOpen = false;

    public InputReader(){
        lines = new ArrayList<String>(50);
        JFileChooser fileopen = new JFileChooser(new File("./GraphExamples"));
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) FileOpen = true;
        if (FileOpen) {
            File file = fileopen.getSelectedFile();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                String line;
                while((line = reader.readLine()) != null){
                    if (!line.equals(""))lines.add(line);
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Graph initFromData(){
        StringBuilder name;
        Graph graph = new Graph();
        int radius = 150;
        int indent = 0;
        switch (lines.get(0).replaceAll(" ","")){
            case "Location:default": mode = DataMode.DEFAULT; break;
            case "Location:random" : mode = DataMode.RANDOM; break;
            case "Location:bycoordinates": mode = DataMode.COORDS; break;
            default: mode = DataMode.DEFAULT; indent = 1;break;
        }
        for(int i = 1 - indent; i < lines.size(); i++){
            String line = lines.get(i);
            int k = 0;
            for (; k < line.length(); k++){
                if (line.charAt(k) == ' ' || line.charAt(k) == '(') break;
            }
            name = new StringBuilder(line.substring(0,k));
            Point2D.Double point = new Point2D.Double();
            line = line.replaceAll(" ","");
            switch (mode){
                case DEFAULT:
                     point = new Point2D.Double(350+ radius*Math.cos(Math.toRadians(i * 360 / (lines.size() - (1-indent)))),
                            250 + radius*Math.sin(Math.toRadians(i*360/(lines.size()-(1-indent))))); break;
                case RANDOM:
                    Random rand = new Random();
                    point = new Point2D.Double( 50 + rand.nextInt(700), 50 + rand.nextInt(400)); break;
                case COORDS:

                    int x = Integer.parseInt(line.substring(line.indexOf('(')+1,line.indexOf(';')));
                    int y = Integer.parseInt(line.substring(line.indexOf(';')+1,line.indexOf(')')));
                    point = new Point2D.Double(x,y);
            }
            graph.add(new Node(new DrawNode(point,name.toString())));
        }
        for (int i = 1- indent; i< lines.size(); i++){
            String line = lines.get(i);
            int ind = 0;
            if(mode == DataMode.COORDS){
                line = line.substring(line.indexOf(')') + 1);
                ind = 1;
            }
            line = line.trim();
            String[] neighbours = line.split("\\s+");
            Node SourceNode = graph.getNodes().get(i-(1-indent));
            Node DestNode = SourceNode;
            for(int j = 1 - ind; j < neighbours.length; j++){
                for (Node node :graph.getNodes()){
                    if(node.getView().getName().equals(neighbours[j])){
                        DestNode = node; break;
                    }
                }

                graph.add(new Edge(SourceNode, DestNode,true));
            }
        }

        return graph;
    }
}
