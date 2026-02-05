package org.kelvinizer.dynamic;

import org.kelvinizer.misc.interfaces.Activatable;
import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code DynamicMotionManager} class manages a collection of {@code DynamicObject}s and is responsible for activating
 * and rendering them with respect to time. It handles the motions and rendering of dynamic objects in the system.
 * <p>
 * This class is used to control the activation and visualization of dynamic elements, including handling time-based
 * motion and rendering of associated graphical components.
 * @author Boyan Hu
 */
public class DynamicMotionManager implements Drawable, Activatable {

    /** The time when the motion manager was activated, in nanoseconds */
    private long startTime = -1;

    /** The list of dynamic objects to be managed and rendered */
    private final ArrayList<DynamicObject> dm = new ArrayList<>();

    /**
     * Constructs a new {@code DynamicMotionManager}.
     */
    public DynamicMotionManager(){}

    /**
     * Adds a {@code DynamicObject} to the list of objects managed by this motion manager.
     *
     * @param dynamicObject the {@code DynamicObject} to add to the manager
     */
    public void addDynamicObject(DynamicObject dynamicObject){
        dm.add(dynamicObject);
    }

    /**
     * Activates the motion manager by setting the start time and activating all dynamic objects.
     * The start time is recorded in nanoseconds, and the objects are activated accordingly.
     */
    @Override
    public void activate() {
        if(startTime == -1){
            startTime = System.nanoTime();
            for(DynamicObject dO: dm){
                dO.activate(startTime);
            }
        }
    }

    /**
     * Renders all the dynamic objects managed by this motion manager.
     * Each dynamic object is rendered based on the current time.
     *
     * @param g2d the {@code Graphics2D} object used for rendering the dynamic objects
     */
    @Override
    public void render(Graphics2D g2d) {
        long t = System.nanoTime();
        for(DynamicObject dO: dm){
            dO.render(g2d, t);
        }
    }
}