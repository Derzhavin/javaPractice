package com.codebind;

import com.codebind.algorithmComponents.Algorithm;
import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.algorithmComponents.KosarajuAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class Algorithms {
    private static HashMap<String, Algorithm> algorithmHashMap = new HashMap<>();
    public static Algorithm currentAlgorithm = null;

    public Algorithms() {}

    public static void init() {
        algorithmHashMap.put("DFS", new DFSAlgorithm());
        algorithmHashMap.put("Kosaraju", new KosarajuAlgorithm());
    }

    public static void selectAlgorithmByName(String name) {
        currentAlgorithm = algorithmHashMap.get(name);
    }
}
