package org.kelvinizer.display;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.params.BorderParams;
import org.kelvinizer.shapes.COval;
import org.kelvinizer.shapes.CRect;
import org.kelvinizer.shapes.CShape;
import org.kelvinizer.textbox.CRectTextBox;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.kelvinizer.params.BorderParams.*;

public class DisplayTextBox implements Scalable, Drawable, Focusable {
    public final CRectTextBox posX = new CRectTextBox();
    public final CRectTextBox posY = new CRectTextBox();
    public final CRectTextBox width = new CRectTextBox();
    public final CRectTextBox height = new CRectTextBox();
    public final CRectTextBox initial = new CRectTextBox();
    public final CRectTextBox decrease = new CRectTextBox();

    int selection_status = 0;

    private void setTextBoxSelector(CRectTextBox c, String text, int y) {
        CRectButton button = new CRectButton();

        BoundedString normal = new BoundedString(text, 20);
        normal.setBounds(new CRect(100, y, 100, 100));
        normal.getBounds().setOutlineColor(Color.WHITE);
        normal.getBounds().setOutlineThickness(3.0);
        normal.setStyle(Font.PLAIN);
        button.setNormal(normal);

        BoundedString onFocus = new BoundedString(text, 20);
        onFocus.setBounds(new CRect(100, y, 100, 100));
        onFocus.getBounds().setOutlineColor(Color.YELLOW);
        onFocus.getBounds().setOutlineThickness(3.0);
        onFocus.setStyle(Font.BOLD);
        button.setOnFocus(onFocus);

        BoundedString onSelection = new BoundedString(text, 20);
        onSelection.setBounds(new CRect(100, y, 100, 100));
        onSelection.getBounds().setOutlineColor(Color.RED);
        onSelection.getBounds().setOutlineThickness(3.0);
        onSelection.setStyle(Font.BOLD);
        button.setOnSelection(onSelection);

        c.setSelector(button);
    }

    private void setTextBox(CRectTextBox C, String text, int y) {
        setTextBoxSelector(C, text, y);
        BoundedString entry = new BoundedString("", 50);
        entry.setStyle(Font.PLAIN);
        entry.setBounds(new CRect(260, y, 200, 100));
        entry.getBounds().setOutlineColor(Color.WHITE);
        entry.getBounds().setOutlineThickness(2.0);
        entry.setString("0");
        C.setEntry(entry);
        C.setTextUpdateHandler((b, c) -> {
            String s = getUpdatedString(b, c);
            b.getEntry().setString(s);
            if(Display.selectedShape instanceof CRect cr){
                CRect cr2 = cr.clone();
                switch (selection_status){
                    case 1 -> cr2.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                    case 2 -> cr2.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                    case 3 -> cr2.setWidth(Double.parseDouble(s));
                    case 4 -> cr2.setHeight(Double.parseDouble(s));
                    default -> {}
                }
                if(BorderParams.shapeInBoarder(cr2)){
                    switch (selection_status){
                        case 1 -> cr.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                        case 2 -> cr.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                        case 3 -> cr.setWidth(Double.parseDouble(s));
                        case 4 -> cr.setHeight(Double.parseDouble(s));
                        default -> {}
                    }
                    Display.refreshStatus();
                    Display.recalibrateWallMask();
                }
            }
            else if(Display.selectedShape instanceof COval cr){
                COval cr2 = cr.clone();
                switch (selection_status){
                    case 1 -> cr2.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                    case 2 -> cr2.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                    case 3 -> cr2.setWidth(Double.parseDouble(s));
                    case 4 -> cr2.setHeight(Double.parseDouble(s));
                    default -> {}
                }
                if(BorderParams.shapeInBoarder(cr2)){
                    switch (selection_status){
                        case 1 -> cr.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                        case 2 -> cr.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                        case 3 -> cr.setWidth(Double.parseDouble(s));
                        case 4 -> cr.setHeight(Double.parseDouble(s));
                        default -> {}
                    }
                    Display.refreshStatus();
                    Display.recalibrateWallMask();
                }
            }
            else if(Display.selectedRouter != null){
                Router sr = Display.selectedRouter.clone();
                switch (selection_status){
                    case 1 -> sr.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                    case 2 -> sr.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                    case 3 -> sr.setInitialDBM(Double.parseDouble(s));
                    case 4 -> sr.setWallLoss(Double.parseDouble(s));
                    default -> {}
                }
                if(BorderParams.shapeInBoarder(sr)){
                    switch (selection_status){
                        case 1 -> Display.selectedRouter.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                        case 2 -> Display.selectedRouter.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                        case 3 -> Display.selectedRouter.setInitialDBM(Double.parseDouble(s));
                        case 4 -> Display.selectedRouter.setWallLoss(Double.parseDouble(s));
                        default -> {}
                    }
                    Display.refreshStatus();
                }
            }
        });
    }

    private static String getUpdatedString(CRectTextBox b, char c) {
        String s = b.getEntry().getString();
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
        return s;
    }

    public DisplayTextBox() {
        setTextBox(posX, "X", 150);
        setTextBox(posY, "Y", 317);
        setTextBox(width, "Width", 483);
        setTextBox(height, "Height", 650);
        setTextBox(initial, "Initial", 483);
        setTextBox(decrease, "D.R.", 650);
    }
    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        posX.render(g2d);
        posY.render(g2d);
    }

    public void renderShape(Graphics2D g2d) {
        width.render(g2d);
        height.render(g2d);
    }

    public void renderRouter(Graphics2D g2d){
        initial.render(g2d);
        decrease.render(g2d);
    }

    /**
     * Set an object's focus state based on a {@code MouseEvent} object
     *
     * @param e The event to be processed
     */
    @Override
    public void setFocused(MouseEvent e) {
        posX.setFocused(e);
        posY.setFocused(e);
    }

    public void focusShape(MouseEvent e) {
        width.setFocused(e);
        height.setFocused(e);
    }

    public void focusRouter(MouseEvent e) {
        initial.setFocused(e);
        decrease.setFocused(e);
    }
    /**
     * Scaling the object with the new window dimension.
     *
     * @param d The new dimension of the window
     */
    @Override
    public void scale(Dimension d) {
        posX.scale(d);
        posY.scale(d);
        width.scale(d);
        height.scale(d);
        initial.scale(d);
        decrease.scale(d);
    }

    public void updateText(char c){
        if(posX.isSelected()){
            selection_status = 1;
            posX.updateText(c);
        }
        else if(posY.isSelected()){
            selection_status = 2;
            posY.updateText(c);
        }
        else if(width.isSelected()){
            selection_status = 3;
            width.updateText(c);
        }
        else if(height.isSelected()){
            selection_status = 4;
            height.updateText(c);
        }
        else if(initial.isSelected()){
            selection_status = 3;
            initial.updateText(c);
        }
        else if(decrease.isSelected()){
            selection_status = 4;
            decrease.updateText(c);
        }
        selection_status = 0;
    }

    public void initializeText(CShape c){
        if(c instanceof CRect cr){
            posX.getEntry().setString(Integer.toString((int)(cr.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
            posY.getEntry().setString(Integer.toString((int)(cr.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
            width.getEntry().setString(Integer.toString((int)(cr.getWidth())));
            height.getEntry().setString(Integer.toString((int)(cr.getHeight())));
        }
        else if (c instanceof COval co) {
            posX.getEntry().setString(Integer.toString((int)(co.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
            posY.getEntry().setString(Integer.toString((int)(co.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
            width.getEntry().setString(Integer.toString((int)(co.getWidth())));
            height.getEntry().setString(Integer.toString((int)(co.getHeight())));
        }
        else{
            System.err.println("Bad shape");
        }
    }

    public void initializeText(Router r){
        posX.getEntry().setString(Integer.toString((int)(r.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
        posY.getEntry().setString(Integer.toString((int)(r.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
        initial.getEntry().setString(Double.toString((r.getInitialDBM())));
        decrease.getEntry().setString(Double.toString(r.getWallLoss()));
    }
}
