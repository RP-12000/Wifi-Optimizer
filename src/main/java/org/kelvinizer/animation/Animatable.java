package org.kelvinizer.animation;

import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;

/**
 * The {@code Animatable} interface extends the {@code Drawable} interface to include
 * methods for handling appearance and disappearance animations of an object.
 * Implementing classes define the specific behavior for these animations.
 * @author Boyan Hu
 */
public interface Animatable extends Drawable {

    /**
     * Called when the object needs to handle its appearance animation.
     *
     * @param g2d the {@code Graphics2D} context used for drawing the appearance animation
     */
    void onAppearance(Graphics2D g2d);

    /**
     * Called when the object needs to handle its disappearance animation.
     *
     * @param g2d the {@code Graphics2D} context used for drawing the disappearance animation
     */
    void onDisappearance(Graphics2D g2d);
}
