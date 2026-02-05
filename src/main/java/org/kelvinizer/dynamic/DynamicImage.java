package org.kelvinizer.dynamic;

import org.kelvinizer.shapes.CRect;
import org.kelvinizer.misc.objects.Motion;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The {@code DynamicImage} class represents an image that can be animated by applying motions to its position
 * over time. It extends {@code DynamicObject} and implements rendering and activation functionality for the image.
 * <p>This class is used to display images on screen, with support for motion effects and transformations based on time.
 * @author Boyan Hu
 */
public class DynamicImage extends DynamicObject {

    /** The image to be displayed */
    private BufferedImage bf;

    /** The bounds of the image */
    private final CRect bounds;

    /**
     * Constructs a new {@code DynamicImage} with specified bounds.
     *
     * @param bounds the bounds of the image as a {@code CRect}
     */
    public DynamicImage(CRect bounds){
        this.bounds = bounds;
    }

    /**
     * Sets the image to be rendered.
     *
     * @param bf the {@code BufferedImage} to be displayed
     */
    public void setImage(BufferedImage bf){
        this.bf = bf;
    }

    /**
     * Retrieves the buffered image.
     * This method returns the current instance of the {@link BufferedImage} stored in the class.
     * The returned image can be used for various purposes, such as displaying or manipulating the image data.
     *
     * @return the {@link BufferedImage} object representing the image.
     */
    public BufferedImage getImage() {
        return bf;
    }

    /**
     * Renders the image at its current position, applying motion transformations based on the time passed.
     * The image's position is updated according to the horizontal and vertical motions.
     *
     * @param g2d the {@code Graphics2D} object used for rendering the image
     * @param time the current time in nanoseconds, used for calculating the position based on motion
     */
    @Override
    public void render(Graphics2D g2d, long time) {
        double timePassed = (double) (System.nanoTime() - start) / S_TO_NS_CONVERSION_FACTOR;

        // Apply horizontal motion
        for(Motion m: horizontal){
            if(m.contains(timePassed)){
                bounds.setX(m.getPos(timePassed));
            }
        }

        // Apply vertical motion
        for(Motion m: vertical){
            if(m.contains(timePassed)){
                bounds.setY(m.getPos(timePassed));
            }
        }

        // Render the image at the calculated position
        Rectangle r = bounds.toJShape();
        g2d.drawImage(bf, r.x, r.y, r.width, r.height, null);
    }
}