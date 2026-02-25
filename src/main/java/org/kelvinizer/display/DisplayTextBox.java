package org.kelvinizer.display;

import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.interfaces.Focusable;
import org.kelvinizer.misc.interfaces.Scalable;
import org.kelvinizer.misc.objects.BoundedString;
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

    private final BoundedString posXVerdict = new BoundedString("X", 30);
    private final BoundedString posYVerdict = new BoundedString("Y", 30);
    private final BoundedString widthVerdict = new BoundedString("Width", 30);
    private final BoundedString heightVerdict = new BoundedString("Height", 30);
    private final BoundedString initialVerdict = new BoundedString("", 20);
    private final BoundedString decreaseVerdict = new BoundedString("", 20);

    private final BoundedString borders = new BoundedString("Object Info", 30);

    int selection_status = 0;

    private void setTextBoxSelector(CRectTextBox c, BoundedString b, int y) {
        CRectButton button = new CRectButton();

        BoundedString normal = new BoundedString("", 20);
        normal.setBounds(new CRect(320, y, 180, 50));
        normal.getBounds().setOutlineColor(Color.WHITE);
        normal.getBounds().setOutlineThickness(3.0);
        button.setNormal(normal);

        BoundedString onFocus = new BoundedString("", 20);
        onFocus.setBounds(new CRect(320, y, 185, 55));
        onFocus.getBounds().setOutlineColor(Color.WHITE);
        onFocus.getBounds().setOutlineThickness(3.0);
        button.setOnFocus(onFocus);

        BoundedString onSelection = new BoundedString("", 20);
        onSelection.setBounds(new CRect(320, y, 180, 50));
        onSelection.getBounds().setOutlineColor(Color.YELLOW);
        onSelection.getBounds().setOutlineThickness(3.0);
        button.setOnSelection(onSelection);

        c.setSelector(button);

        b.setBounds(new CRect(120, y, 180, 50));
        b.getBounds().setOutlineColor(Color.WHITE);
        b.getBounds().setOutlineThickness(3.0);
        b.setStyle(Font.BOLD);
    }

    private void setTextBox(CRectTextBox C, BoundedString text, int y) {
        setTextBoxSelector(C, text, y);
        C.setEntry("0");
        C.setTextUpdateHandler((b, c) -> {
            String s = getUpdatedString(b, c);
            b.setEntry(s);
            if(Display.selectedShape instanceof CRect cr){
                CRect cr2 = cr.clone();
                switch (selection_status){
                    case 1 -> cr2.setX(Double.parseDouble(s) + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                    case 2 -> cr2.setY(Double.parseDouble(s) + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                    case 3 -> cr2.setWidth(Double.parseDouble(s));
                    case 4 -> cr2.setHeight(Double.parseDouble(s));
                    default -> {}
                }
                if(shapeInBoarder(cr2)){
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
                if(shapeInBoarder(cr2)){
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
                if(shapeInBoarder(sr)){
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

    private String getUpdatedString(CRectTextBox b, char c) {
        String s = b.getEntry();
        if (c == '\b') {
            if(s.equals("-0") || s.length() == 1){
                s = "0";
            }
            else{
                s = s.substring(0, s.length() - 1);
            }
        }
        else if(c == '.'){
            if(!s.contains(".")){
                s+= c;
            }
        }
        else if(c == '-'){
            if(s.contains("-")){
                s = s.substring(1);
            }
            else{
                s = '-'+s;
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
        setTextBox(posX, posXVerdict, 440);
        setTextBox(posY, posYVerdict, 515);
        setTextBox(width, widthVerdict, 590);
        setTextBox(height, heightVerdict, 665);
        setTextBox(initial, initialVerdict, 590);
        setTextBox(decrease, decreaseVerdict, 665);
        borders.setBounds(new CRect(220, 530, 400, 350));
        borders.getBounds().setOutlineColor(Color.WHITE);
        borders.getBounds().setOutlineThickness(3.0);
        borders.setStyle(Font.BOLD);
        borders.setRelativeY(0.08);
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
        posXVerdict.render(g2d);
        posYVerdict.render(g2d);
        borders.render(g2d);
    }

    public void renderShape(Graphics2D g2d) {
        width.render(g2d);
        height.render(g2d);
        widthVerdict.render(g2d);
        heightVerdict.render(g2d);
    }

    public void renderRouter(Graphics2D g2d){
        initial.render(g2d);
        decrease.render(g2d);
        initialVerdict.render(g2d);
        decreaseVerdict.render(g2d);
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
        switch (c) {
            case CRect cr -> {
                posX.setEntry(Integer.toString((int) (cr.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
                posY.setEntry(Integer.toString((int) (cr.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
                width.setEntry(Integer.toString((int) (cr.getWidth())));
                height.setEntry(Integer.toString((int) (cr.getHeight())));
            }
            case COval co -> {
                posX.setEntry(Integer.toString((int) (co.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
                posY.setEntry(Integer.toString((int) (co.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
                width.setEntry(Integer.toString((int) (co.getWidth())));
                height.setEntry(Integer.toString((int) (co.getHeight())));
            }
            default -> throw new IllegalStateException("Unexpected value: " + c);
        }
    }

    public void initializeText(Router r){
        posX.setEntry(Integer.toString((int)(r.getX()) - BORDER_CENTER_X + BORDER_SIZE / 2));
        posY.setEntry(Integer.toString((int)(r.getY()) - BORDER_CENTER_Y + BORDER_SIZE / 2));
        initialVerdict.setString("Initial Power");
        initial.setEntry(Double.toString((r.getInitialDBM())));
        decreaseVerdict.setString("Wall Loss Factor");
        decrease.setEntry(Double.toString(r.getWallLoss()));
    }

    public void clearText(){
        posX.setEntry("");
        posY.setEntry("");
        width.setEntry("");
        height.setEntry("");
        initial.setEntry("");
        decrease.setEntry("");
        initialVerdict.setString("");
        decreaseVerdict.setString("");
    }
}
