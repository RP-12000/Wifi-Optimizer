package org.kelvinizer.dynamic;

import org.kelvinizer.misc.objects.Motion;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;

/**
 * The {@code DynamicString} class represents a dynamic string that can be moved over time according to specified motions.
 * It extends {@link DynamicObject} and implements {@link Drawable} to provide rendering capabilities.
 * This class encapsulates a {@link BoundedString} that contains the string data, and it allows dynamic motion along the horizontal and vertical axes.
 * @author Boyan Hu
 */
public class DynamicString extends DynamicObject implements Drawable {

    /**
     * A {@code BoundedString} that contains the string data and its bounds for rendering.
     */
    private final BoundedString bs;

    /**
     * Constructs a {@code DynamicString} instance with the given string, font size, and bounding box dimensions.
     *
     * @param d The string data to display.
     * @param stringSize The size of the font for the string.
     * @param x The x-coordinate for the starting position of the string.
     * @param y The y-coordinate for the starting position of the string.
     * @param width The width of the bounding box for the string.
     * @param height The height of the bounding box for the string.
     */
    public DynamicString(String d, int stringSize, double x, double y, double width, double height){
        bs = new BoundedString(d, stringSize, x, y, width, height);
    }

    /**
     * Constructs a {@code DynamicString} instance with the given string, font size, and starting position.
     * The bounding box will have width and height set to 0.
     *
     * @param d The string data to display.
     * @param stringSize The size of the font for the string.
     * @param x The x-coordinate for the starting position of the string.
     * @param y The y-coordinate for the starting position of the string.
     */
    public DynamicString(String d, int stringSize, double x, double y){
        this(d, stringSize, x, y, 0, 0);
    }

    /**
     * Constructs a {@code DynamicString} instance with the given string and font size.
     * The starting position will be (0, 0), and the bounding box will have width and height set to 0.
     *
     * @param d The string data to display.
     * @param stringSize The size of the font for the string.
     */
    public DynamicString(String d, int stringSize){
        this(d, stringSize, 0, 0);
    }

    /**
     * Constructs a {@code DynamicString} instance with the given string and font size set to 0.
     * The starting position will be (0, 0), and the bounding box will have width and height set to 0.
     *
     * @param d The string data to display.
     */
    public DynamicString(String d){
        this(d, 0);
    }

    /**
     * Returns the {@link BoundedString} object associated with this dynamic string, which contains the string data and its bounding box.
     *
     * @return The {@code BoundedString} object representing the string data and bounds.
     */
    public BoundedString getBoundedString(){
        return bs;
    }

    /**
     * Renders the dynamic string using the provided {@code Graphics2D} object. The position of the string is updated
     * based on the horizontal and vertical motions and the elapsed time.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     * @param time The time in nanoseconds at which the object should be rendered.
     */
    @Override
    public void render(Graphics2D g2d, long time) {
        double timePassed = (double) (time - start) / S_TO_NS_CONVERSION_FACTOR;

        // Update horizontal motion based on time
        for(Motion m: horizontal){
            if(m.contains(timePassed)){
                bs.getBounds().setX(m.getPos(timePassed));
            }
        }

        // Update vertical motion based on time
        for(Motion m: vertical){
            if(m.contains(timePassed)){
                bs.getBounds().setY(m.getPos(timePassed));
            }
        }

        // Render the bounded string
        bs.render(g2d);
    }
}