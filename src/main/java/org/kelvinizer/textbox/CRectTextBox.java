package org.kelvinizer.textbox;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CRectTextBox implements Drawable, Scalable, Focusable {
    private CRectButton selector;
    private BoundedString entry;
    private TextUpdateHandler handler;

    /**
     * Constructs a new {@code CRectTextBox}.
     */
    public CRectTextBox(CRectButton selector, BoundedString entry, TextUpdateHandler handler) {
        this.selector = selector;
        this.entry = entry;
        this.handler = handler;
    }

    public CRectTextBox(TextUpdateHandler handler) {
        this(new CRectButton(), new BoundedString(), handler);
    }

    public CRectTextBox() {
        this((box, c) -> box.entry.setString(box.entry.getString() + c));
    }

    public void setSelector(CRectButton selector) {
        this.selector = selector;
    }

    public void setEntry(BoundedString entry) {
        this.entry = entry;
    }

    public void setTextUpdateHandler(TextUpdateHandler handler) {
        this.handler = handler;
    }

    public void updateText(char c) {
        if(handler == null) {
            handler = (box, c1) -> box.entry.setString(box.entry.getString() + c1);
        }
        handler.update(this, c);
    }

    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        selector.render(g2d);
        entry.render(g2d);
    }

    /**
     * Set an object's focus state based on a {@code MouseEvent} object
     *
     * @param e The event to be processed
     */
    @Override
    public void setFocused(MouseEvent e) {
        selector.setFocused(e);
    }

    public boolean isFocused(){
        return selector.isFocused();
    }

    public void select(){
        selector.select();
    }

    public void select(boolean option){
        selector.select(option);
    }

    public boolean isSelected(){
        return selector.isSelected();
    }

    /**
     * Scaling the object with the new window dimension.
     *
     * @param d The new dimension of the window
     */
    @Override
    public void scale(Dimension d) {
        selector.scale(d);
    }

    public BoundedString getEntry() {
        return entry;
    }
}
