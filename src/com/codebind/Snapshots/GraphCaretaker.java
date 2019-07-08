package com.codebind.Snapshots;

import com.codebind.Button;
import com.codebind.Managers.GraphEventManager;

import java.util.ArrayDeque;

public class GraphCaretaker {
    private static ArrayDeque<Snapshot> backDeque = new ArrayDeque<>();
    private static ArrayDeque<Snapshot> frontDeque = new ArrayDeque<>();
    public static final int STACK_MAX_DEPTH = 50;
    public static Button button1;
    public static Button button2;

    public static void push(Snapshot snapshot) {
        backDeque.add(snapshot);
        frontDeque.clear();

        if (backDeque.size() == STACK_MAX_DEPTH) {
            backDeque.removeFirst();
        }

        button1.setEnabled(true);
        button2.setEnabled(false);
    }

    public static boolean isEmpty() {
        return backDeque.isEmpty();
    }

    public static boolean isFull() {
        return frontDeque.isEmpty();
    }

    public static Snapshot pop() {
        if (backDeque.size() == 1) {
            button1.setEnabled(false);
        }

        button2.setEnabled(true);

        if (!backDeque.isEmpty()) {
            Snapshot snapshot = backDeque.removeLast();
            frontDeque.add(GraphEventManager.getInstance().getGraph().save());
            return snapshot;
        }
        else {
            return null;
        }
    }

    public static Snapshot poll() {
        if (frontDeque.size() == 1) {
            button2.setEnabled(false);
        }

        button1.setEnabled(true);

        if (!frontDeque.isEmpty()) {
            Snapshot snapshot = frontDeque.removeLast();
            backDeque.add(GraphEventManager.getInstance().getGraph().save());
            return snapshot;
        }
        else {
            return null;
        }
    }
}
