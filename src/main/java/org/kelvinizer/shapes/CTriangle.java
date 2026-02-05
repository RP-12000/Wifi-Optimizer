package org.kelvinizer.shapes;

import java.awt.*;

import org.kelvinizer.animation.Params;

/**
 * Represents a triangle shape defined by three points.
 * Extends the {@link CShape} class and provides specific implementation for
 * converting to a {@link Polygon} and scaling the triangle.
 *
 * @see CShape
 * @author Boyan Hu
 */
public class CTriangle extends CShape {

    /** The x-coordinate of the first point of the triangle. */
    private double x1;

    /** The y-coordinate of the first point of the triangle. */
    private double y1;

    /** The x-coordinate of the second point of the triangle. */
    private double x2;

    /** The y-coordinate of the second point of the triangle. */
    private double y2;

    /** The x-coordinate of the third point of the triangle. */
    private double x3;

    /** The y-coordinate of the third point of the triangle. */
    private double y3;

    /**
     * Constructs a triangle with the specified coordinates for its three points.
     *
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     * @param x3 the x-coordinate of the third point
     * @param y3 the y-coordinate of the third point
     */
    public CTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    /**
     * Constructs a triangle with all points initialized to (0, 0).
     */
    public CTriangle() {
        this(0, 0, 0, 0, 0, 0);
    }

    /**
     * Sets the coordinates of the first point of the triangle.
     *
     * @param x the x-coordinate of the first point
     * @param y the y-coordinate of the first point
     */
    public void setFirstPoint(double x, double y) {
        x1 = x;
        y1 = y;
    }

    /**
     * Sets the coordinates of the second point of the triangle.
     *
     * @param x the x-coordinate of the second point
     * @param y the y-coordinate of the second point
     */
    public void setSecondPoint(double x, double y) {
        x2 = x;
        y2 = y;
    }

    /**
     * Sets the coordinates of the third point of the triangle.
     *
     * @param x the x-coordinate of the third point
     * @param y the y-coordinate of the third point
     */
    public void setThirdPoint(double x, double y) {
        x3 = x;
        y3 = y;
    }

    /**
     * Converts the triangle to a Java AWT {@link Polygon} object.
     *
     * @return the {@link Polygon} representation of the triangle
     */
    @Override
    public Polygon toJShape() {
        return new Polygon(
                new int[]{(int) x1, (int) x2, (int) x3},
                new int[]{(int) y1, (int) y2, (int) y3},
                3
        );
    }

    /**
     * Scales the triangle's coordinates proportionally based on the specified dimensions.
     *
     * @param d the target dimensions for scaling
     */
    @Override
    public void scale(Dimension d) {
        x1 = x1 / Params.REF_WIN_W * d.width;
        y1 = y1 / Params.REF_WIN_H * d.height;
        x2 = x2 / Params.REF_WIN_W * d.width;
        y2 = y2 / Params.REF_WIN_H * d.height;
        x3 = x3 / Params.REF_WIN_W * d.width;
        y3 = y3 / Params.REF_WIN_H * d.height;
    }

    /**
     * Returns the x-coordinate of the first point of the triangle.
     *
     * @return the x-coordinate of the first point
     */
    public double getX1() {
        return x1;
    }

    /**
     * Returns the x-coordinate of the second point of the triangle.
     *
     * @return the x-coordinate of the second point
     */
    public double getX2() {
        return x2;
    }

    /**
     * Returns the x-coordinate of the third point of the triangle.
     *
     * @return the x-coordinate of the third point
     */
    public double getX3() {
        return x3;
    }

    /**
     * Returns the y-coordinate of the first point of the triangle.
     *
     * @return the y-coordinate of the first point
     */
    public double getY1() {
        return y1;
    }

    /**
     * Returns the y-coordinate of the second point of the triangle.
     *
     * @return the y-coordinate of the second point
     */
    public double getY2() {
        return y2;
    }

    /**
     * Returns the y-coordinate of the third point of the triangle.
     *
     * @return the y-coordinate of the third point
     */
    public double getY3() {
        return y3;
    }
}