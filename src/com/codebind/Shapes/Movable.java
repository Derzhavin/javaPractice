package com.codebind.Shapes;

import java.awt.geom.Rectangle2D;

@FunctionalInterface
public interface Movable {
    Rectangle2D getBoundingRect();
}
