package org.kelvinizer.misc.objects;

import org.kelvinizer.shapes.CRect;
import org.kelvinizer.misc.interfaces.Drawable;

import java.awt.*;

/**
 * The {@code BoundedString} class represents a drawable string with a specified bounding rectangle.
 * It allows the string to be rendered with various customization options such as font family, style,
 * size, color, and positioning within the specified bounds.
 * It also implements the {@link Drawable} interface.
 * @author Boyan Hu
 */
public class BoundedString implements Drawable {

    /** The bounds of the string as a rectangle. */
    private CRect bounds;

    /** The string content. */
    private String string;

    /** The font family of the string. Default is "Arial". */
    private String fontFamily = "Arial";

    /** The style of the font. Default is {@link Font#BOLD}. */
    private int style = Font.BOLD;

    /** The size of the string in pixels. */
    private int stringSize;

    /** The color of the string. Default is white. */
    private Color stringColor = Color.WHITE;

    /** Horizontal white space around the string within the bounds. */
    private double horizontalWhiteSpace = 3;

    /** Vertical white space around the string within the bounds. */
    private double verticalWhiteSpace = 2;

    /** The maximum size of the string. Default is -1, meaning no maximum. */
    private int maxStringSize = -1;

    /** The relative X-coordinate for string positioning. Default is 0.5 (centered). */
    private double relativeX = 0.5;

    /** The relative Y-coordinate for string positioning. Default is 0.5 (centered). */
    private double relativeY = 0.5;

    /** The origin point of the string for positioning. */
    private Pair<Double, Double> stringOrigin = new Pair<>(0.5, 0.5);

    /**
     * Constructs a new {@code BoundedString} with the specified string, size, position, and bounds.
     *
     * @param d The string to be displayed.
     * @param stringSize The size of the string.
     * @param x The X-coordinate for the position of the string.
     * @param y The Y-coordinate for the position of the string.
     * @param width The width of the bounding rectangle.
     * @param height The height of the bounding rectangle.
     */
    public BoundedString(String d, int stringSize, double x, double y, double width, double height) {
        string = d;
        this.stringSize = stringSize;
        bounds = new CRect(x, y, width, height);
    }

    /**
     * Constructs a new {@code BoundedString} with the specified string, size, and position.
     * The bounding rectangle will have zero width and height.
     *
     * @param d The string to be displayed.
     * @param stringSize The size of the string.
     * @param x The X-coordinate for the position of the string.
     * @param y The Y-coordinate for the position of the string.
     */
    public BoundedString(String d, int stringSize, double x, double y) {
        this(d, stringSize, x, y, 0, 0);
    }

    /**
     * Constructs a new {@code BoundedString} with the specified string and size.
     * The position will be set to (0, 0) and the bounding rectangle will have zero width and height.
     *
     * @param d The string to be displayed.
     * @param stringSize The size of the string.
     */
    public BoundedString(String d, int stringSize) {
        this(d, stringSize, 0, 0);
    }

    /**
     * Constructs a new {@code BoundedString} with the specified string and a default size of 0.
     */
    public BoundedString(String d) {
        this(d, 0);
    }

    /**
     * Constructs a new {@code BoundedString} with an empty string and a default size of 0.
     */
    public BoundedString() {
        this("");
    }

    /**
     * Validates if the string fits within the bounds with the specified font and string size.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     * @param f The font to use for rendering the string.
     * @param d The string to be rendered.
     * @return {@code true} if the string fits within the bounds, otherwise {@code false}.
     */
    private boolean isValidSize(Graphics2D g2d, Font f, String d) {
        FontMetrics fm = g2d.getFontMetrics(f);
        return (bounds.getHeight()*relativeY - (double) (fm.getAscent()+fm.getDescent())/2 > verticalWhiteSpace) &&
                (bounds.getHeight()*(1-relativeY) - (double) (fm.getAscent()+fm.getDescent())/2 > verticalWhiteSpace) &&
                (bounds.getWidth()*relativeX - (double) (fm.stringWidth(d))/2 > horizontalWhiteSpace) &&
                (bounds.getWidth()*(1-relativeX) - (double) (fm.stringWidth(d))/2 > horizontalWhiteSpace);
    }

    /**
     * Calculates the rendering point for the string based on the bounding rectangle and origin.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     * @param d The string to be rendered.
     * @return A {@code Pair} containing the X and Y coordinates for the string's position.
     */
    protected Pair<Double, Double> getRenderPoint(Graphics2D g2d, String d) {
        FontMetrics fm = g2d.getFontMetrics();
        return new Pair<>(
                bounds.getX() - bounds.getOrigin().first + bounds.getWidth()*relativeX - fm.stringWidth(d) * stringOrigin.first,
                bounds.getY() - bounds.getOrigin().second + bounds.getHeight()*relativeY -
                        (fm.getAscent() + fm.getDescent())*stringOrigin.second + fm.getAscent()
        );
    }

    /**
     * Renders the string within the specified bounds using the current font settings.
     *
     * @param g2d The {@code Graphics2D} object used for rendering.
     */
    @Override
    public void render(Graphics2D g2d) {
        bounds.render(g2d);
        if(maxStringSize >= 0) {
            int size = 0;
            while(isValidSize(g2d, new Font(fontFamily, style, size), string) && size <= maxStringSize) {
                size++;
            }
            stringSize = size - 1;
        }
        g2d.setFont(new Font(fontFamily, style, stringSize));
        g2d.setColor(stringColor);
        Pair<Double, Double> p = getRenderPoint(g2d, string);
        g2d.drawString(string, (float)(double)p.first, (float)(double)p.second);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.0f));
    }

    /**
     * Sets the font style for the string.
     *
     * @param style The font style to set (e.g., {@link Font#BOLD}, {@link Font#ITALIC}).
     */
    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * Sets the string content to be displayed.
     *
     * @param s The string to set.
     */
    public void setString(String s) {
        string = s;
    }

    /**
     * Returns the current string content.
     *
     * @return The current string.
     */
    public String getString() {
        return string;
    }

    /**
     * Sets the color of the string.
     *
     * @param stringColor The color to set for the string.
     */
    public void setStringColor(Color stringColor) {
        this.stringColor = stringColor;
    }

    /**
     * Sets the bounds of the string.
     *
     * @param c The {@code CRect} representing the new bounds.
     */
    public void setBounds(CRect c) {
        bounds = c;
    }

    /**
     * Returns the current bounds of the string.
     *
     * @return The {@code CRect} representing the bounds of the string.
     */
    public CRect getBounds() {
        return bounds;
    }

    /**
     * Sets the maximum allowed size for the string.
     *
     * @param maxStringSize The maximum string size.
     */
    public void setMaxStringSize(int maxStringSize) {
        this.maxStringSize = maxStringSize;
    }

    /**
     * Sets the relative X position for the string.
     *
     * @param x The relative X coordinate (0.0 to 1.0).
     */
    public void setRelativeX(double x) {
        relativeX = x;
    }

    /**
     * Sets the relative Y position for the string.
     *
     * @param y The relative Y coordinate (0.0 to 1.0).
     */
    public void setRelativeY(double y) {
        relativeY = y;
    }

    /**
     * Sets the origin point for the string positioning.
     *
     * @param p A {@code Pair} representing the X and Y coordinates of the origin.
     */
    public void setStringOrigin(Pair<Double, Double> p) {
        stringOrigin = p;
    }

    /**
     * Returns the current color of the string.
     *
     * @return The {@code Color} of the string.
     */
    public Color getStringColor() {
        return stringColor;
    }
}