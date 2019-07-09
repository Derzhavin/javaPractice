package Phisics;

import java.awt.geom.Point2D;

public abstract class Droppable {
    private Point2D.Double acceleration = new Point2D.Double(0, 0);
    private boolean isFirstUpdate = true;

    public Point2D.Double gravitationUpdate(Point2D.Double gravityVector) {
        if (isFirstUpdate) {
            acceleration.y = -1;
            isFirstUpdate = false;
        }

        acceleration.x += gravityVector.x;
        acceleration.y += gravityVector.y;

        return acceleration;
    }
}
