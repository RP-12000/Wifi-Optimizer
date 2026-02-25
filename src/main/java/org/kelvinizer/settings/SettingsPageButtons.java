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
    public final BoundedString airVerdict = new BoundedString("Air Degeneration Coefficient", 50);

    private void setAirCRectTextBox(){
        CRectButton button = new CRectButton();

        BoundedString normal = new BoundedString("", 50);
        normal.setBounds(new CRect(540, 420, 800, 100));
        normal.getBounds().setOutlineColor(Color.WHITE);
        normal.getBounds().setOutlineThickness(3.0);
        normal.setStyle(Font.PLAIN);
        button.setNormal(normal);

        BoundedString onFocus = new BoundedString("", 50);
        onFocus.setBounds(new CRect(540, 420, 808, 101));
        onFocus.getBounds().setOutlineColor(Color.WHITE);
        onFocus.getBounds().setOutlineThickness(3.0);
        onFocus.setStyle(Font.BOLD);
        button.setOnFocus(onFocus);

        BoundedString onSelection = new BoundedString("", 50);
        onSelection.setBounds(new CRect(540, 420, 800, 100));
        onSelection.getBounds().setOutlineColor(Color.YELLOW);
        onSelection.getBounds().setOutlineThickness(3.0);
        onSelection.setStyle(Font.BOLD);
        button.setOnSelection(onSelection);

        air.setSelector(button);
        BoundedString entry = new BoundedString("", 50);
        entry.setStyle(Font.PLAIN);
        air.setEntry(Double.toString(BorderParams.AIR_LOSS_PER_UNIT));
        air.setTextUpdateHandler((box, c) -> {
            String s = box.getEntry();
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
            box.setEntry(s);
            BorderParams.AIR_LOSS_PER_UNIT = Double.parseDouble(s);
            BorderParams.environmentChanged = true;
        });
    }

    public SettingsPageButtons() {
        BoundedString normal = new BoundedString("", 20);
        normal.setBounds(new CRect(100, 50, 100, 100));
        normal.setStyle(Font.PLAIN);
        BoundedString onFocus = new BoundedString("", 20);
        onFocus.setBounds(new CRect(100, 55, 110, 110));
        onFocus.setStyle(Font.BOLD);
        if(!back.setIcon("back.png")){
            normal.setString("back");
            onFocus.setString("back");
        }
        back.setNormal(normal);
        back.setOnFocus(onFocus);

        airVerdict.setBounds(new CRect(540, 300, 800, 100));
        airVerdict.getBounds().setOutlineColor(Color.WHITE);
        airVerdict.getBounds().setOutlineThickness(3.0);

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
        airVerdict.render(g2d);
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
