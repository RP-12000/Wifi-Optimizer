package org.kelvinizer.misc.interfaces;

import java.awt.event.MouseEvent;

/**
 * This interface lets an object detects mouse movement and define if the object is focused.
 * It is mainly used in buttons and the collection class of buttons.
 * @author Boyan Hu
 */
public interface Focusable {
    /**
     * Set an object's focus state based on a {@code MouseEvent} object
     * @param e The event to be processed
     */
    void setFocused(MouseEvent e);
}