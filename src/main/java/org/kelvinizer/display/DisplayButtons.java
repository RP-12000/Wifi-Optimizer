package org.kelvinizer.display;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.shapes.CRect;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DisplayButtons implements Scalable, Drawable, Focusable {
    public final CRectButton rectangle = new CRectButton();
    public final CRectButton oval = new  CRectButton();
    public final CRectButton router = new CRectButton();

    public final CRectButton optimize = new CRectButton();
    public final CRectButton save = new CRectButton();
    public final CRectButton delete = new CRectButton();
    public final CRectButton duplicate = new CRectButton();
    public final CRectButton load = new CRectButton();
    public final CRectButton clear = new CRectButton();

    private void setShapeButton(CRectButton button, String label, int x) {
        BoundedString normal = new BoundedString("", 20);
        normal.setBounds(new CRect(x, 50, 60, 60));
        normal.getBounds().setOutlineColor(Color.WHITE);
        normal.getBounds().setOutlineThickness(2.0);
        normal.setStyle(Font.PLAIN);

        BoundedString onFocus = new BoundedString("", 20);
        onFocus.setBounds(new CRect(x, 50, 63, 63));
        onFocus.getBounds().setOutlineColor(Color.WHITE);
        onFocus.getBounds().setOutlineThickness(5.0);
        onFocus.setStyle(Font.BOLD);

        BoundedString onSelection = new BoundedString("", 20);
        onSelection.setBounds(new CRect(x, 50, 60, 60));
        onSelection.getBounds().setOutlineColor(Color.GREEN);
        onSelection.getBounds().setOutlineThickness(5.0);
        onSelection.setStyle(Font.BOLD);

        if(!button.setIcon(label+".png")){
            normal.setString(label);
            onFocus.setString(label);
            onSelection.setString(label);
        }

        button.setNormal(normal);
        button.setOnFocus(onFocus);
        button.setOnSelection(onSelection);
    }

    public DisplayButtons() {
        setShapeButton(rectangle, "rectangle", 240);
        setShapeButton(oval, "oval", 340);
        setShapeButton(router, "router", 440);
        setShapeButton(save, "save", 540);
        setShapeButton(delete, "delete", 640);
        setShapeButton(duplicate, "duplicate", 740);
        setShapeButton(load, "load", 840);
        setShapeButton(clear, "clear", 940);
        setShapeButton(optimize, "optimize", 1040);
    }

    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        rectangle.render(g2d);
        oval.render(g2d);
        router.render(g2d);

        optimize.render(g2d);
        save.render(g2d);
        delete.render(g2d);
        duplicate.render(g2d);
        load.render(g2d);
        clear.render(g2d);
    }

    /**
     * Set an object's focus state based on a {@code MouseEvent} object
     *
     * @param e The event to be processed
     */
    @Override
    public void setFocused(MouseEvent e) {
        rectangle.setFocused(e);
        oval.setFocused(e);
        router.setFocused(e);

        optimize.setFocused(e);
        save.setFocused(e);
        delete.setFocused(e);
        duplicate.setFocused(e);
        load.setFocused(e);
        clear.setFocused(e);
    }

    /**
     * Scaling the object with the new window dimension.
     *
     * @param d The new dimension of the window
     */
    @Override
    public void scale(Dimension d) {
        rectangle.scale(d);
        oval.scale(d);
        router.scale(d);

        optimize.scale(d);
        save.scale(d);
        delete.scale(d);
        duplicate.scale(d);
        load.scale(d);
        clear.scale(d);
    }
}
