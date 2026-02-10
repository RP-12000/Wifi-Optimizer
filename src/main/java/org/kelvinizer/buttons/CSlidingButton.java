package org.kelvinizer.buttons;

import org.kelvinizer.params.GeneralParams;
import org.kelvinizer.shapes.CRect;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.misc.interfaces.*;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The {@code SlidingButton} class represents a UI component consisting of a base, a slider, and an optional verdict text.
 * The slider allows users to select a value within a defined range.
 * @author Boyan Hu
 */
public class CSlidingButton implements Focusable, Drawable, Scalable {
    private CRect base = new CRect();
    private CRectButton slider = new CRectButton();
    private BoundedString verdict = new BoundedString();
    private final double minVal, maxVal;
    private double start, end, k, b, currentVal;
    private double mousePosScaleFactor = 1;

    /**
     * Constructs a {@code SlidingButton} with specified minimum, maximum, and initial values.
     *
     * @param minVal   the minimum value of the slider
     * @param maxVal   the maximum value of the slider
     * @param startVal the initial value of the slider
     */
    public CSlidingButton(double minVal, double maxVal, double startVal) {
        this.maxVal = maxVal;
        this.minVal = minVal;
        this.currentVal = startVal;
    }

    /**
     * Sets the base rectangle and slider button for the sliding button.
     *
     * @param base   the {@link CRect} representing the base
     * @param slider the {@link CRectButton} representing the slider
     */
    public void setBaseAndSlider(CRect base, CRectButton slider) {
        this.base = base;
        this.slider = slider;
        start = base.getX() - base.getWidth() / 2.0 + slider.getNormal().getBounds().getWidth() / 2.0;
        end = base.getX() + base.getWidth() / 2.0 - slider.getNormal().getBounds().getWidth() / 2.0;
        k = (maxVal - minVal) / (end - start);
        b = minVal - k * start;
        this.slider.getNormal().getBounds().setPosition((currentVal - b) / k, this.base.getY());
        this.slider.getOnFocus().getBounds().setPosition((currentVal - b) / k, this.base.getY());
    }

    /**
     * Sets the verdict text to be displayed.
     *
     * @param verdict the {@link BoundedString} representing the verdict text
     */
    public void setVerdict(BoundedString verdict) {
        this.verdict = verdict;
    }

    /**
     * Sets the verdict text string to be displayed.
     *
     * @param s the string representing the verdict text
     */
    public void setVerdictString(String s) {
        this.verdict.setString(s);
    }

    /**
     * Moves the slider based on the provided mouse event.
     *
     * @param e the {@link MouseEvent} triggering the slider movement
     */
    public void moveSlider(MouseEvent e) {
        if (slider.isFocused()) {
            slider.getNormal().getBounds().setX(Math.clamp(e.getX() / mousePosScaleFactor, start, end));
            slider.getOnFocus().getBounds().setX(Math.clamp(e.getX() / mousePosScaleFactor, start, end));
            currentVal = k * slider.getOnFocus().getBounds().getX() + b;
        }
    }

    /**
     * Moves the slider by a specified delta value.
     *
     * @param delta the change in value to move the slider
     */
    public void moveSlider(double delta) {
        currentVal = Math.clamp(currentVal + delta, minVal, maxVal);
        slider.getNormal().getBounds().setX((currentVal - b) / k);
        slider.getOnFocus().getBounds().setX((currentVal - b) / k);
    }

    /**
     * Gets the current value of the slider.
     *
     * @return the current value of the slider
     */
    public double getCurrentVal() {
        return currentVal;
    }

    /**
     * Renders the sliding button, including its base, slider, and verdict text.
     *
     * @param g2d the {@link Graphics2D} object to render with
     */
    @Override
    public void render(Graphics2D g2d) {
        base.render(g2d);
        slider.render(g2d);
        verdict.render(g2d);
    }

    /**
     * Updates the focused state of the slider based on the mouse event.
     *
     * @param e the {@link MouseEvent} triggering the focus update
     */
    @Override
    public void setFocused(MouseEvent e) {
        slider.setFocused(e);
    }

    /**
     * Scales the sliding button's components based on the given dimensions.
     *
     * @param d the {@link Dimension} to scale to
     */
    @Override
    public void scale(Dimension d) {
        slider.scale(d);
        mousePosScaleFactor = (double) d.width / GeneralParams.REF_WIN_W;
    }
}