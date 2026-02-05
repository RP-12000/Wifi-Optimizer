package org.kelvinizer.buttons;

import org.kelvinizer.shapes.CTriangle;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The {@code CTriangleButton} class represents a button with a triangular shape and two states: normal and focused.
 * It extends {@link KButton} and provides functionality for rendering, scaling, and focus handling.
 * @author Boyan Hu
 */
public class CTriangleButton extends KButton {
    private CTriangle normal, onFocus;
    private CTriangle normalSpace, focusSpace;

    /**
     * Constructs a new {@code CTriangleButton}.
     */
    public CTriangleButton() {}

    /**
     * Initializes the {@code normalSpace} for the normal state of the triangle button.
     */
    private void initNormal() {
        normalSpace = new CTriangle(
                normal.getX1(),
                normal.getY1(),
                normal.getX2(),
                normal.getY2(),
                normal.getX3(),
                normal.getY3()
        );
    }

    /**
     * Sets the {@code normal} state of the triangle button.
     *
     * @param c the {@link CTriangle} representing the normal state
     */
    public void setNormal(CTriangle c) {
        normal = c;
        initNormal();
    }

    /**
     * Initializes the {@code focusSpace} for the focused state of the triangle button.
     */
    private void initOnFocus() {
        focusSpace = new CTriangle(
                onFocus.getX1(),
                onFocus.getY1(),
                onFocus.getX2(),
                onFocus.getY2(),
                onFocus.getX3(),
                onFocus.getY3()
        );
    }

    /**
     * Sets the {@code onFocus} state of the triangle button.
     *
     * @param c the {@link CTriangle} representing the focused state
     */
    public void setOnFocus(CTriangle c) {
        onFocus = c;
        initOnFocus();
    }

    /**
     * Updates the focused state of the button based on the mouse event.
     *
     * @param e the {@link MouseEvent} triggering the focus update
     */
    @Override
    public void setFocused(MouseEvent e) {
        if (focused) {
            focused = focusSpace.contains(e.getPoint());
        } else {
            focused = normalSpace.contains(e.getPoint());
        }
    }

    /**
     * Scales the button's triangular bounds based on the given dimensions.
     *
     * @param d the {@link Dimension} to scale to
     */
    @Override
    public void scale(Dimension d) {
        initNormal();
        initOnFocus();
        normalSpace.scale(d);
        focusSpace.scale(d);
    }

    /**
     * Renders the triangle button based on its current state.
     *
     * @param g2d the {@link Graphics2D} object to render with
     */
    @Override
    public void render(Graphics2D g2d) {
        if (focused) {
            onFocus.render(g2d);
        } else {
            normal.render(g2d);
        }
    }
}