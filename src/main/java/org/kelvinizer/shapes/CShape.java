package org.kelvinizer.shapes;

import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;

import static org.kelvinizer.params.GeneralParams.REF_WIN_H;
import static org.kelvinizer.params.GeneralParams.REF_WIN_W;

/**
 * An abstract class representing a customizable shape with properties for fill color, outline color,
 * and outline thickness. It provides methods for rendering, checking point containment, and scaling.
 * Subclasses must implement methods for converting to a Java AWT {@link Shape} and scaling the shape.
 * Implements the {@link Drawable} interface.
 *
 * @see Drawable
 * @author Boyan Hu
 */
public abstract class CShape implements Drawable {

    /**
     * The color used to fill the shape. If {@code null}, no fill will be applied.
     */
    private Color fillColor;

    /**
     * The color used for the outline of the shape. If {@code null}, no outline will be drawn.
     */
    private Color outlineColor;

    /**
     * The thickness of the shape's outline. A value of {@code 0.0} means no outline will be drawn.
     */
    private double outlineThickness;

    /**
     * Constructs a {@code CShape} with specified fill color, outline color, and outline thickness.
     *
     * @param fillColor        the fill color of the shape, or {@code null} for no fill
     * @param outlineColor     the outline color of the shape, or {@code null} for no outline
     * @param outlineThickness the thickness of the outline, in pixels
     */
    public CShape(Color fillColor, Color outlineColor, double outlineThickness) {
        this.fillColor = fillColor;
        this.outlineColor = outlineColor;
        this.outlineThickness = outlineThickness;
    }

    /**
     * Constructs a {@code CShape} with no fill, no outline, and an outline thickness of 0.0.
     */
    public CShape() {
        this(null, null, 0.0);
    }

    /**
     * Returns the fill color of the shape.
     *
     * @return the fill color, or {@code null} if no fill is applied
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color of the shape.
     *
     * @param fillColor the new fill color, or {@code null} for no fill
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Returns the outline color of the shape.
     *
     * @return the outline color, or {@code null} if no outline is applied
     */
    public Color getOutlineColor() {
        return outlineColor;
    }

    /**
     * Sets the outline color of the shape.
     *
     * @param outlineColor the new outline color, or {@code null} for no outline
     */
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    /**
     * Returns the thickness of the shape's outline.
     *
     * @return the outline thickness in pixels
     */
    public double getOutlineThickness() {
        return outlineThickness;
    }

    /**
     * Sets the thickness of the shape's outline.
     *
     * @param outlineThickness the new outline thickness in pixels
     */
    public void setOutlineThickness(double outlineThickness) {
        this.outlineThickness = outlineThickness;
    }

    /**
     * Renders the shape onto the provided {@link Graphics2D} object. The method draws the fill
     * and outline of the shape based on the current properties.
     *
     * @param g2d the graphics context used for rendering
     */
    @Override
    public void render(Graphics2D g2d) {
        Shape s = toJShape();
        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fill(s);
        }
        if (outlineColor != null) {
            g2d.setStroke(new BasicStroke((float) outlineThickness));
            g2d.setColor(outlineColor);
            g2d.draw(s);
        }
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.0f));
    }

    /**
     * Determines if the specified point is contained within the shape.
     *
     * @param p the point to test
     * @return {@code true} if the point is inside the shape; {@code false} otherwise
     */
    public boolean contains(Point p) {
        return toJShape().contains(p);
    }

    public boolean contains(Point p, Dimension d) {
        return toJShape().contains(p.getX() / d.width * REF_WIN_W, p.getY() / d.height * REF_WIN_H);
    }

    /**
     * Converts the shape to a Java AWT {@link Shape} object.
     * This method must be implemented by subclasses to define the shape.
     *
     * @return the AWT {@link Shape} representation of the shape
     */
    public abstract Shape toJShape();

    /**
     * Scales the shape according to the given dimensions.
     * This method must be implemented by subclasses to define scaling behavior.
     *
     * @param d the dimensions used for scaling
     */
    public abstract void scale(Dimension d);

    public abstract CShape clone();
}