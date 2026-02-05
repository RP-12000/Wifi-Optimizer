package org.kelvinizer.buttons;

import org.kelvinizer.misc.interfaces.*;

/**
 * The {@code KButton} class serves as an abstract base class for buttons that
 * implement the {@link Drawable}, {@link Scalable}, and {@link Focusable} interfaces.
 *
 * <p>This class provides basic functionality for managing selection and focus states.</p>
 * @author Boyan Hu
 */
public abstract class KButton implements Drawable, Scalable, Focusable {

    /** Indicates whether the button is currently selected. */
    protected boolean selected = false;

    /** Indicates whether the button is currently focused. */
    protected boolean focused = false;

    /**
     * Constructs a new {@code KButton}.
     */
    public KButton() {}

    /**
     * Sets the selection state of the button.
     *
     * @param option {@code true} to select the button, {@code false} to deselect it
     */
    public void select(boolean option) {
        selected = option;
    }

    /**
     * Checks whether the button is currently focused.
     *
     * @return {@code true} if the button is focused, {@code false} otherwise
     */
    public boolean isFocused() {
        return focused;
    }
}