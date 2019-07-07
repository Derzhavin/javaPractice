package com.codebind.Snapshots;

import java.util.ArrayDeque;

public class GraphCaretaker {
    private static ArrayDeque<Snapshot> deque = new ArrayDeque<>();
    public static final int STACK_MAX_DEPTH = 50;

    public static void push(Snapshot snapshot) {
        deque.add(snapshot);

        if (deque.size() == STACK_MAX_DEPTH) {
            deque.removeFirst();
        }
    }

    public static Snapshot pop() {
        return deque.isEmpty() ? null : deque.removeLast();
    }
}
