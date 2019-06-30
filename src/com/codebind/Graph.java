package com.codebind;

import com.codebind.Shapes.Drawable;
import com.codebind.Shapes.Edge;
import com.codebind.Shapes.Node;

import java.awt.*;
import java.util.ArrayList;

public class Graph implements Drawable {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private Main.GraphStates state = Main.GraphStates.NOTHING;

    @Override
    public void draw(Graphics2D g) {

    }

}
