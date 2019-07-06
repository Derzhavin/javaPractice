package com.codebind.Сancellation;

import com.codebind.graphComonents.Graph;

public abstract class Command {
    private boolean isFinished = false;

    abstract void recover(Graph graph);
    abstract void free();
    boolean isWholeCommandFinished() {return isFinished;}
    void setFinished(boolean isFinished) {this.isFinished = isFinished;}
}
