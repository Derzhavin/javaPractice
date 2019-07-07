package com.codebind.Snapshots;

import java.util.Stack;

public class GraphCaretaker {
    private static Stack<Snapshot> stack = new Stack<>();
    public static final int STACK_MAX_DEPTH = 50;

    public static void push(Snapshot snapshot) {
        stack.push(snapshot);
    }

    public static Snapshot pop() {
        return stack.pop();
    }
}
