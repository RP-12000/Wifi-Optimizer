package org.kelvinizer.display;

import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.shapes.CRect;

import java.awt.*;

import static org.kelvinizer.params.ColorParams.seperatorColor;

public class Separators implements Drawable {
    private final CRect horizontal = new CRect(540, 90, 1080, 5);
    private final CRect first = new CRect(200, 45, 5, 90);
    private final CRect second = new CRect(455, 45, 5, 90);
    private final CRect third = new CRect(630, 45, 5, 90);
    private final CRect fourth = new CRect(975, 45, 5, 90);

    public Separators(){
        horizontal.setFillColor(seperatorColor);
        first.setFillColor(seperatorColor);
        second.setFillColor(seperatorColor);
        third.setFillColor(seperatorColor);
        fourth.setFillColor(seperatorColor);
    }
    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        horizontal.render(g2d);
        first.render(g2d);
        second.render(g2d);
        third.render(g2d);
        fourth.render(g2d);
    }
}
