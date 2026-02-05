package org.kelvinizer.misc.interfaces;

import java.awt.*;

/**
 * This interface has a method that defines how to draw the object that implement this.
 * Note that this is not the same as {@code paintComponent(Graphics g)} in JPanel.
 * This passes in a Graphics2D object for more customized rendering.
 * @author Boyan Hu
 */
public interface Drawable {
    /**
     * Render the object that implements it.
     * @param g2d the Graphics2D object responsible for drawing
     */
    void render(Graphics2D g2d);
}