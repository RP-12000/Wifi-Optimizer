package org.kelvinizer.buttons;

import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.shapes.CRect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * The {@code CRectButton} class represents a custom button with rectangular bounds and support for
 * different states (normal, focused, and selected). It extends {@link KButton} and provides
 * functionality for managing graphical representation and scaling.
 * @author Boyan Hu
 */
public class CRectButton extends KButton {
    private BoundedString normal;
    private BoundedString onFocus;
    private BoundedString onSelection = null;
    private BufferedImage icon = null;
    private CRect normalSpace, focusSpace, selectionSpace;

    /**
     * Constructs a new {@code CRectButton}.
     */
    public CRectButton() {}

    /**
     * Initializes the {@code normalSpace} bounds for the normal state of the button.
     */
    private void initNormalSpace() {
        normalSpace = new CRect(
                normal.getBounds().getX(),
                normal.getBounds().getY(),
                normal.getBounds().getWidth(),
                normal.getBounds().getHeight()
        );
        normalSpace.setOrigin(normal.getBounds().getOrigin());
    }

    /**
     * Sets the {@code normal} state representation of the button.
     *
     * @param bs the {@link BoundedString} for the normal state
     */
    public void setNormal(BoundedString bs) {
        normal = bs;
        initNormalSpace();
    }

    /**
     * Initializes the {@code focusSpace} bounds for the focused state of the button.
     */
    private void initFocusSpace() {
        focusSpace = new CRect(
                onFocus.getBounds().getX(),
                onFocus.getBounds().getY(),
                onFocus.getBounds().getWidth(),
                onFocus.getBounds().getHeight()
        );
        focusSpace.setOrigin(onFocus.getBounds().getOrigin());
    }

    /**
     * Sets the {@code onFocus} state representation of the button.
     *
     * @param bs the {@link BoundedString} for the focused state
     */
    public void setOnFocus(BoundedString bs) {
        onFocus = bs;
        initFocusSpace();
    }

    /**
     * Initializes the {@code selectionSpace} bounds for the selected state of the button.
     */
    private void initSelectionSpace() {
        selectionSpace = new CRect(
                onSelection.getBounds().getX(),
                onSelection.getBounds().getY(),
                onSelection.getBounds().getWidth(),
                onSelection.getBounds().getHeight()
        );
        selectionSpace.setOrigin(onSelection.getBounds().getOrigin());
    }

    /**
     * Sets the {@code onSelection} state representation of the button.
     *
     * @param bs the {@link BoundedString} for the selected state
     */
    public void setOnSelection(BoundedString bs) {
        onSelection = bs;
        initSelectionSpace();
    }

    /**
     * Gets the {@code normal} state representation of the button.
     *
     * @return the {@link BoundedString} for the normal state
     */
    public BoundedString getNormal() {
        return normal;
    }

    /**
     * Gets the {@code onFocus} state representation of the button.
     *
     * @return the {@link BoundedString} for the focused state
     */
    public BoundedString getOnFocus() {
        return onFocus;
    }

    /**
     * Gets the {@code onSelection} state representation of the button.
     *
     * @return the {@link BoundedString} for the selected state
     */
    public BoundedString getOnSelection() {
        return onSelection;
    }

    /**
     * Updates the focused state based on the mouse event.
     *
     * @param e the {@link MouseEvent} triggering the update
     */
    @Override
    public void setFocused(MouseEvent e) {
        if (selected) {
            focused = selectionSpace.contains(e.getPoint());
        } else if (focused) {
            focused = focusSpace.contains(e.getPoint());
        } else {
            focused = normalSpace.contains(e.getPoint());
        }
    }

    /**
     * Scales the button's bounds and spaces based on the given dimensions.
     *
     * @param d the new {@link Dimension} to scale to
     */
    @Override
    public void scale(Dimension d) {
        initNormalSpace();
        normalSpace.scale(d);
        if (focusSpace != null) {
            initFocusSpace();
            focusSpace.scale(d);
        }
        if (selectionSpace != null) {
            initSelectionSpace();
            selectionSpace.scale(d);
        }
    }

    /**
     * Draws the icon associated with the button, if one exists.
     *
     * @param g2d the {@link Graphics2D} object to draw with
     */
    private void drawIcon(Graphics2D g2d) {
        if (icon != null) {
            Rectangle r;
            if (selected) {
                r = onSelection.getBounds().toJShape();
            } else if (focused) {
                r = onFocus.getBounds().toJShape();
            } else {
                r = normal.getBounds().toJShape();
            }
            g2d.drawImage(icon, r.x, r.y, r.width, r.height, null);
        }
    }

    /**
     * Renders the button based on its current state.
     *
     * @param g2d the {@link Graphics2D} object to render with
     */
    @Override
    public void render(Graphics2D g2d) {
        if (selected) {
            onSelection.render(g2d);
            g2d.draw(onSelection.getBounds().toJShape());
        } else if (focused) {
            onFocus.render(g2d);
        } else {
            normal.render(g2d);
        }
        drawIcon(g2d);
    }

    /**
     * Sets the icon for the button from the given file path.
     *
     * @param iconPath the path to the icon file
     * @return {@code true} if the icon was successfully loaded, {@code false} otherwise
     */
    public boolean setIcon(String iconPath) {
        try {
            icon = ImageIO.read(Objects.requireNonNull(CRectButton.class.getResourceAsStream("/" + iconPath)));
            return true;
        } catch (RuntimeException | IOException e) {
            icon = null;
            return false;
        }
    }
}