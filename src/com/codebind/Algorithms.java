package com.codebind;

import com.codebind.algorithmComponents.Algorithm;
import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.algorithmComponents.KosarajuAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class Algorithms {
    private static HashMap<String, Algorithm> algorithmHashMap = new HashMap<>();

    public Algorithms() {}

    public static void init() {
        algorithmHashMap.put("DFS", new DFSAlgorithm());
        algorithmHashMap.put("Kosaraju", new KosarajuAlgorithm());
    }

    public static Algorithm getAlgorithmByName(String name) {
        return algorithmHashMap.get(name);
    }
}
