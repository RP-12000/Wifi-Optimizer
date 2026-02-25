package org.kelvinizer.textbox;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CRectTextBox implements Drawable, Scalable, Focusable {
    private String entry;
    private CRectButton selector;
    private TextUpdateHandler handler;

    public void setEntry(String entry) {
        this.entry = entry;
        refreshText();
    }

    public String getEntry() {
        return entry;
    }

    private void refreshText(){
        if(selector.getNormal()!=null){
            selector.getNormal().setString(entry);
        }
        if(selector.getOnFocus()!=null){
            selector.getOnFocus().setString(entry);
        }
        if(selector.getOnSelection()!=null){
            selector.getOnSelection().setString(entry);
        }
    }

    /**
     * Constructs a new {@code CRectTextBox}.
     */
    public CRectTextBox(CRectButton selector, String entry, TextUpdateHandler handler) {
        this.selector = selector;
        this.entry = entry;
        refreshText();
        this.handler = handler;
    }

    public CRectTextBox(TextUpdateHandler handler) {
        this(new CRectButton(), "", handler);
    }

    public CRectTextBox() {
        this(new TextUpdateHandler() {
            @Override
            public void update(CRectTextBox box, char c) {
                box.entry+=c;
                box.refreshText();
            }
        });
    }

    public void setSelector(CRectButton selector) {
        this.selector = selector;
    }

    public void setTextUpdateHandler(TextUpdateHandler handler) {
        this.handler = handler;
    }

    public void updateText(char c) {
        if(handler == null) {
            handler = new TextUpdateHandler() {
                @Override
                public void update(CRectTextBox box, char c) {
                    box.entry+=c;
                    box.refreshText();
                }
            };
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
}
