package algorithmComponents;

import Managers.AlgorithmEventManager;

import java.util.HashMap;

public class Algorithms {
    private static HashMap<String, Algorithm> algorithmHashMap = new HashMap<>();
    private static int delay = 500;

    public Algorithms() {}

    public static void init() {
        algorithmHashMap.put("DFS", new DFSAlgorithm());
        algorithmHashMap.put("Kosaraju", new KosarajuAlgorithm());

        AlgorithmEventManager.getInstance().setAlgorithm(algorithmHashMap.get("DFS"));
    }

    public static void selectAlgorithmByName(String name) {
        AlgorithmEventManager.getInstance().setAlgorithm(algorithmHashMap.get(name));
        AlgorithmEventManager.getInstance().setDelay(delay);
    }

    public static void setDelay(int _delay) {
        delay = _delay;
        AlgorithmEventManager.getInstance().setDelay(delay);
    }
}
