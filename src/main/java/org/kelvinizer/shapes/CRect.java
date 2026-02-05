package org.kelvinizer.shapes;

import org.kelvinizer.misc.objects.Pair;
import org.kelvinizer.animation.Params;

import java.awt.*;

/**
 * Represents a rectangle shape with customizable position, size, and origin.
 * Extends the {@link CShape} class and provides specific implementation for
 * converting to a {@link Rectangle} and scaling the rectangle.
 *
 * @see CShape
 * @author Boyan Hu
 */
public class CRect extends CShape {

    /** The x-coordinate of the rectangle's position. */
    private double x;

    /** The y-coordinate of the rectangle's position. */
    private double y;

    /** The width of the rectangle. */
    private double width;

    /** The height of the rectangle. */
    private double height;

    /** The origin point of the rectangle, represented as a pair of doubles (x, y). */
    private Pair<Double, Double> origin;

    /**
     * Constructs a rectangle with the specified position and size.
     *
     * @param x      the x-coordinate of the rectangle's position
     * @param y      the y-coordinate of the rectangle's position
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public CRect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.origin = new Pair<>(width / 2, height / 2);
    }

    /**
     * Constructs a rectangle with the specified size, positioned at (0, 0).
     *
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    public CRect(double width, double height) {
        this(0, 0, width, height);
    }

    /**
     * Constructs a rectangle with default position (0, 0) and size (0, 0).
     */
    public CRect() {
        this(0, 0);
    }

    /**
     * Returns the origin point of the rectangle.
     *
     * @return the origin point as a {@link Pair} of doubles
     */
    public Pair<Double, Double> getOrigin() {
        return origin;
    }

    /**
     * Sets the origin point of the rectangle.
     *
     * @param origin the new origin point as a {@link Pair} of doubles
     */
    public void setOrigin(Pair<Double, Double> origin) {
        this.origin = origin;
    }

    /**
     * Sets the origin point of the rectangle using x and y coordinates.
     *
     * @param x the x-coordinate of the new origin
     * @param y the y-coordinate of the new origin
     */
    public void setOrigin(double x, double y) {
        setOrigin(new Pair<>(x, y));
    }

    /**
     * Returns the height of the rectangle.
     *
     * @return the height of the rectangle
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the rectangle.
     *
     * @param height the new height of the rectangle
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Returns the width of the rectangle.
     *
     * @return the width of the rectangle
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the rectangle.
     *
     * @param width the new width of the rectangle
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Returns the y-coordinate of the rectangle's position.
     *
     * @return the y-coordinate of the rectangle
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the rectangle's position.
     *
     * @param y the new y-coordinate of the rectangle
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the x-coordinate of the rectangle's position.
     *
     * @return the x-coordinate of the rectangle
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the rectangle's position.
     *
     * @param x the new x-coordinate of the rectangle
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Converts the rectangle to a Java AWT {@link Rectangle} object.
     * The rectangle is adjusted based on its origin.
     *
     * @return the {@link Rectangle} representation of the rectangle
     */
    @Override
    public Rectangle toJShape() {
        return new Rectangle(
                (int) (x - origin.first),
                (int) (y - origin.second),
                (int) width,
                (int) height
        );
    }

    /**
     * Sets the size of the rectangle.
     *
     * @param width  the new width of the rectangle
     * @param height the new height of the rectangle
     */
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the position of the rectangle.
     *
     * @param x the new x-coordinate of the rectangle
     * @param y the new y-coordinate of the rectangle
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Scales the rectangle's position and size based on the specified dimensions.
     * The scaling is proportional to the reference window dimensions.
     *
     * @param d the target dimensions for scaling
     */
    @Override
    public void scale(Dimension d) {
        x = x / Params.REF_WIN_W * d.width;
        y = y / Params.REF_WIN_H * d.height;
        width = width / Params.REF_WIN_W * d.width;
        height = height / Params.REF_WIN_H * d.height;
    }
}