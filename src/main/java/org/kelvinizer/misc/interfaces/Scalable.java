package org.kelvinizer.misc.interfaces;

import java.awt.*;

/**
 * This interface defines how to scale an object that implements it when the window is resized.
 * It is mainly used for calculating buttons dimensions when buttons are resized.
 * Note that it is recommended to first define a reference window.
 * @author Boyan Hu
 */
public interface Scalable {
    /**
     * Scaling the object with the new window dimension.
     * @param d The new dimension of the window
     */
    void scale(Dimension d);
}