package org.kelvinizer.misc.interfaces;

public interface Selectable {
    /**
     * Toggles the selection state of the implementing object.
     * Implementing classes should define how the selection state is managed and what actions to take when selected.
     */
    void select();
}
