package org.kelvinizer.buttons;

import org.kelvinizer.misc.interfaces.*;

/**
 * The {@code KButton} class serves as an abstract base class for buttons that
 * implement the {@link Drawable}, {@link Scalable}, and {@link Focusable} interfaces.
 *
 * <p>This class provides basic functionality for managing selection and focus states.</p>
 * @author Boyan Hu
 */
public abstract class CButton implements Drawable, Scalable, Focusable, Selectable {

    /** Indicates whether the button is currently selected. */
    protected boolean selected = false;

    /** Indicates whether the button is currently focused. */
    protected boolean focused = false;

    /**
     * Constructs a new {@code KButton}.
     */
    public CButton() {}

    /**
     * Sets the selection state of the button.
     *
     * @param option {@code true} to select the button, {@code false} to deselect it
     */
    public void select(boolean option) {
        selected = option;
    }

    public void select() {
        selected = !selected;
    }

    /**
     * Checks whether the button is currently focused.
     *
     * @return {@code true} if the button is focused, {@code false} otherwise
     */
    public boolean isFocused() {
        return focused;
    }

    /**
     * Checks whether the button is currently selected.
     *
     * @return {@code true} if the button is selected, {@code false} otherwise
     */
    public boolean isSelected() {
        return selected;
    }
}