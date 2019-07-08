package com.codebind;

import com.codebind.algorithmComponents.Algorithm;
import com.codebind.algorithmComponents.AlgorithmButtons;
import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.algorithmComponents.KosarajuAlgorithm;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Algorithms {
    private static HashMap<String, Algorithm> algorithmHashMap = new HashMap<>();
    public static Algorithm currentAlgorithm = null;
    public static AlgorithmButtons buttonPanel = new AlgorithmButtons();
    public static JLabel display = null;

    private static int delay = 500;

    public Algorithms() {}

    public static void init() {
        algorithmHashMap.put("DFS", new DFSAlgorithm());
        algorithmHashMap.put("Kosaraju", new KosarajuAlgorithm());

        currentAlgorithm = algorithmHashMap.get("DFS");
    }

    public static void selectAlgorithmByName(String name) {
        currentAlgorithm = algorithmHashMap.get(name);
        currentAlgorithm.setDelay(delay);
    }

    public static void setDelay(int _delay) {
        delay = _delay;
        currentAlgorithm.setDelay(delay);
    }
}
