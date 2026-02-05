package org.kelvinizer.dynamic;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.objects.Motion;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * The {@code DynamicCRectButton} class represents a button with dynamic motion that can be scaled and focused.
 * It extends {@link DynamicObject} and implements {@link Scalable} and {@link Focusable} interfaces.
 * This class encapsulates a {@link CRectButton} object and allows it to move over time according to specified motions.
 * @author Boyan Hu
 */
public class DynamicCRectButton extends DynamicObject implements Scalable, Focusable {

    /**
     * The {@code CRectButton} that this dynamic button encapsulates.
     */
    private CRectButton b;

    /**
     * Constructs a default {@code DynamicCRectButton} instance with no associated {@code CRectButton}.
     */
    public DynamicCRectButton(){}

    /**
     * Sets the {@link CRectButton} for this dynamic button.
     *
     * @param b The {@code CRectButton} to be associated with this dynamic button.
     */
    public void setCRectButton(CRectButton b){
        this.b = b;
    }

    /**
     * Renders the dynamic button using the provided {@code Graphics2D} object. The position of the button is updated
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
                b.getNormal().getBounds().setX(m.getPos(timePassed));
                try{
                    Objects.requireNonNull(b.getOnFocus()).getBounds().setX(m.getPos(timePassed));
                    Objects.requireNonNull(b.getOnSelection()).getBounds().setX(m.getPos(timePassed));
                } catch (RuntimeException ignored) {}
            }
        }

        // Update vertical motion based on time
        for(Motion m: vertical){
            if(m.contains(timePassed)){
                b.getNormal().getBounds().setY(m.getPos(timePassed));
                try{
                    Objects.requireNonNull(b.getOnFocus()).getBounds().setY(m.getPos(timePassed));
                    Objects.requireNonNull(b.getOnSelection()).getBounds().setY(m.getPos(timePassed));
                } catch (RuntimeException ignored) {}
            }
        }

        // Render the button
        b.render(g2d);
    }

    /**
     * Returns the {@link CRectButton} object associated with this dynamic button.
     *
     * @return The {@code CRectButton} object representing the button.
     */
    public CRectButton getCRectButton(){
        return b;
    }

    /**
     * Sets the focus for this button based on the mouse event.
     * This method delegates the call to the {@link CRectButton#setFocused(MouseEvent)} method.
     *
     * @param e The {@code MouseEvent} indicating the mouse action.
     */
    @Override
    public void setFocused(MouseEvent e) {
        b.setFocused(e);
    }

    /**
     * Scales the button based on the provided {@code Dimension}.
     * This method delegates the call to the {@link CRectButton#scale(Dimension)} method.
     *
     * @param d The {@code Dimension} to scale the button by.
     */
    @Override
    public void scale(Dimension d) {
        b.scale(d);
    }
}