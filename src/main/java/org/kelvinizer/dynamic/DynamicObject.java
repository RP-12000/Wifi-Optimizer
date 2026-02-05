package org.kelvinizer.dynamic;

import org.kelvinizer.misc.objects.Motion;
import org.kelvinizer.misc.interfaces.Activatable;
import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code DynamicObject} class represents an object with dynamic motion properties. It manages horizontal
 * and vertical motion through {@link Motion} objects and supports activation and rendering in a graphical context.
 * The object is drawn with respect to time and can have its motion activated.
 * It implements {@link Drawable} for rendering and {@link Activatable} for activation functionality.
 * @author Boyan Hu
 */
public abstract class DynamicObject implements Drawable, Activatable {

    /**
     * The start time of the dynamic object, initialized to -1 to indicate it's not activated yet.
     */
    long start = -1;

    /**
     * A constant factor for converting seconds to nanoseconds (1 second = 1e9 nanoseconds).
     */
    protected static final double S_TO_NS_CONVERSION_FACTOR = 1e9;

    /**
     * A list of horizontal {@link Motion} objects that define the horizontal movement of the dynamic object.
     */
    ArrayList<Motion> horizontal = new ArrayList<>();

    /**
     * A list of vertical {@link Motion} objects that define the vertical movement of the dynamic object.
     */
    ArrayList<Motion> vertical = new ArrayList<>();

    /**
     * Constructs a new {@code DynamicObject} instance with no motion data initially.
     */
    public DynamicObject(){}

    /**
     * Adds a horizontal motion to this dynamic object.
     *
     * @param m The {@code Motion} object representing the horizontal motion to be added.
     */
    public void addHorizontalMotion(Motion m){
        horizontal.add(m);
    }

    /**
     * Adds a vertical motion to this dynamic object.
     *
     * @param m The {@code Motion} object representing the vertical motion to be added.
     */
    public void addVerticalMotion(Motion m){
        vertical.add(m);
    }

    /**
     * Activates the dynamic object by recording the current system time in nanoseconds.
     * The start time is set only if it hasn't been set previously.
     */
    @Override
    public void activate(){
        activate(System.nanoTime());
    }

    /**
     * Activates the dynamic object by recording the provided time.
     * The start time is set only if it hasn't been set previously.
     *
     * @param time The time to use for activation in nanoseconds.
     */
    public void activate(long time){
        if(start==-1){
            start = time;
        }
    }

    /**
     * Renders the dynamic object using the provided {@code Graphics2D} object, using the current time
     * from the system in nanoseconds.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     */
    @Override
    public void render(Graphics2D g2d){
        render(g2d, System.nanoTime());
    }

    /**
     * Renders the dynamic object using the provided {@code Graphics2D} object and the specified time.
     * This method must be implemented by subclasses to define how the dynamic object is rendered.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     * @param time The time in nanoseconds at which the object should be rendered.
     */
    public abstract void render(Graphics2D g2d, long time);
}