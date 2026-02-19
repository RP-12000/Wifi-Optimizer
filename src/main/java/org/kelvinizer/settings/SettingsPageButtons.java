package org.kelvinizer.settings;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.params.BorderParams;
import org.kelvinizer.shapes.CRect;
import org.kelvinizer.textbox.CRectTextBox;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SettingsPageButtons implements Scalable, Drawable, Focusable {
    public final CRectButton back = new CRectButton();
    public final CRectTextBox air = new CRectTextBox();

    private void setAirCRectTextBox(){
        CRectButton button = new CRectButton();

        String text = "Air Degeneration Coefficient";
        BoundedString normal = new BoundedString(text, 50);
        normal.setBounds(new CRect(540, 200, 800, 100));
        normal.getBounds().setOutlineColor(Color.WHITE);
        normal.getBounds().setOutlineThickness(3.0);
        normal.setStyle(Font.PLAIN);
        button.setNormal(normal);

        BoundedString onFocus = new BoundedString(text, 50);
        onFocus.setBounds(new CRect(540, 200, 800, 100));
        onFocus.getBounds().setOutlineColor(Color.YELLOW);
        onFocus.getBounds().setOutlineThickness(3.0);
        onFocus.setStyle(Font.BOLD);
        button.setOnFocus(onFocus);

        BoundedString onSelection = new BoundedString(text, 50);
        onSelection.setBounds(new CRect(540, 200, 800, 100));
        onSelection.getBounds().setOutlineColor(Color.RED);
        onSelection.getBounds().setOutlineThickness(3.0);
        onSelection.setStyle(Font.BOLD);
        button.setOnSelection(onSelection);

        air.setSelector(button);
        BoundedString entry = new BoundedString("", 50);
        entry.setStyle(Font.PLAIN);
        entry.setBounds(new CRect(540, 400, 400, 100));
        entry.getBounds().setOutlineColor(Color.WHITE);
        entry.getBounds().setOutlineThickness(2.0);
        entry.setString(Double.toString(BorderParams.AIR_LOSS_PER_UNIT));
        air.setEntry(entry);
        air.setTextUpdateHandler((box, c) -> {
            String s = box.getEntry().getString();
            if (c == '\b') {
                if (s.length() > 1) {
                    s = s.substring(0, s.length() - 1);
                }
                else{
                    s = "0";
                }
            }
            else if(c == '.'){
                if(!s.contains(".")){
                    s+= c;
                }
            }
            else{
                if(s.equals("0")){
                    s = "";
                }
                s+= c;
            }
            box.getEntry().setString(s);
            BorderParams.AIR_LOSS_PER_UNIT = Double.parseDouble(s);
            BorderParams.environmentChanged = true;
        });
    }

    public SettingsPageButtons() {
        BoundedString normal = new BoundedString("", 20);
        normal.setBounds(new CRect(100, 50, 60, 60));
        normal.setStyle(Font.PLAIN);
        BoundedString onFocus = new BoundedString("", 20);
        onFocus.setBounds(new CRect(100, 50, 63, 63));
        onFocus.setStyle(Font.BOLD);
        if(!back.setIcon("back.png")){
            normal.setString("back");
            onFocus.setString("back");
        }
        back.setNormal(normal);
        back.setOnFocus(onFocus);
        setAirCRectTextBox();
    }

    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        back.render(g2d);
        air.render(g2d);
    }

    /**
     * Set an object's focus state based on a {@code MouseEvent} object
     *
     * @param e The event to be processed
     */
    @Override
    public void setFocused(MouseEvent e) {
        back.setFocused(e);
        air.setFocused(e);
    }

    /**
     * Scaling the object with the new window dimension.
     *
     * @param d The new dimension of the window
     */
    @Override
    public void scale(Dimension d) {
        back.scale(d);
        air.scale(d);
    }
}
