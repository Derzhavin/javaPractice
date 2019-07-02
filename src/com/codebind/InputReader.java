package com.codebind;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;


import com.codebind.graphComonents.*;
import com.codebind.viewComponents.*;

enum DataMode {
    DEFAULT,
    RANDOM,
    COORDS
}

public class InputReader {
    private ArrayList<String> lines;
    private DataMode mode;

    public InputReader(){
        lines = new ArrayList<String>(50);
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
                String line;
                while((line = reader.readLine()) != null){
                    lines.add(line);
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
        int radius = 200;
        switch (lines.get(0).replaceAll(" ","")){
            case "Location:default": mode = DataMode.DEFAULT; break;
            case "Location:random" : mode = DataMode.RANDOM; break;
            case "Location:bycoordinates": mode = DataMode.COORDS; break;
        }
        for(int i =1; i < lines.size(); i++){
            String line = lines.get(i);
            name = new StringBuilder(line.substring(0,line.indexOf(' ')));
            switch (mode){
                case DEFAULT:
                    Point2D.Double point = new Point2D.Double(radius*Math.cos((i*360/(lines.size()-1))*3.14/180),
                            radius*Math.sin((i*360/(lines.size()-1))*3.14/180));
                    graph.add(new Node(new DrawNode(point,name.toString()))); break;
            }
        }
        return graph;
    }
}
