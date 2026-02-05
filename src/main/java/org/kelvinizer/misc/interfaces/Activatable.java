package org.kelvinizer.misc.interfaces;

/**
 * This interface provides a method to activates the object that implement this.
 * This is used in Dynamic objects and its associated children.
 * @author Boyan Hu
 */
public interface Activatable {
    /**
     * The method that activates the object that implements it
     */
    void activate();
}