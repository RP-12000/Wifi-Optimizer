package org.kelvinizer.display;

import org.kelvinizer.misc.interfaces.Drawable;
import org.kelvinizer.misc.objects.BoundedString;
import org.kelvinizer.shapes.CRect;

import java.awt.*;

import static org.kelvinizer.params.BorderParams.*;
import static org.kelvinizer.params.GeneralParams.REF_WIN_H;
import static org.kelvinizer.params.GeneralParams.REF_WIN_W;

public class MouseText implements Drawable {
    private final BoundedString x = new BoundedString();
    private final BoundedString y = new BoundedString();
    private final BoundedString signal = new BoundedString();
    private final BoundedString status = new BoundedString();

    private final BoundedString xVerdict = new BoundedString("X Coordinate", 20);
    private final BoundedString yVerdict = new BoundedString("Y Coordinate", 20);
    private final BoundedString signalVerdict = new BoundedString("Signal Strength", 20);
    private final BoundedString statusVerdict = new BoundedString("Space Status", 20);

    private final BoundedString borders = new BoundedString("Mouse Info", 35);

    private void setBounds(BoundedString bs, BoundedString vd, int x, int y){
        bs.setBounds(new CRect(x, y, 180, 60));
        bs.setMaxStringSize(25);
        bs.getBounds().setOutlineColor(Color.WHITE);
        bs.getBounds().setOutlineThickness(3);

        vd.setBounds(new CRect(x, y-45, 180, 30));
        vd.setMaxStringSize(25);
        vd.getBounds().setOutlineColor(Color.WHITE);
        vd.getBounds().setOutlineThickness(3);
    }

    public MouseText(){
        setBounds(x, xVerdict, 120, 200);
        setBounds(y, yVerdict, 320, 200);
        setBounds(signal, signalVerdict, 120, 300);
        setBounds(status, statusVerdict, 320, 300);
        borders.setBounds(new CRect(220, 222, 400, 246));
        borders.getBounds().setOutlineColor(Color.WHITE);
        borders.getBounds().setOutlineThickness(3);
        borders.setStyle(Font.BOLD);
        borders.setRelativeY(0.08);
    }

    public void clearStatus(){
        x.setString("");
        y.setString("");
        signal.setString("");
        status.setString("");
    }

    public void updateStatus(Point p, Dimension d){
        int X = (int) (p.getX() / d.width * REF_WIN_W + (double) BORDER_SIZE / 2 - BORDER_CENTER_X);
        int Y = (int) (p.getY() / d.height * REF_WIN_H + (double) BORDER_SIZE / 2 - BORDER_CENTER_Y);
        x.setString(Integer.toString(X));
        y.setString(Integer.toString(Y));
        if(Display.signalStrength[X][Y] == Double.NEGATIVE_INFINITY){
            signal.setString("No signal");
        }
        else{
            signal.setString(Math.round(Display.signalStrength[X][Y] * 1000) / 1000.0+" dBm");
        }
        if(Display.wallMask[X][Y]){
            status.setString("In Wall");
        }
        else{
            status.setString("Open Space");
        }
    }

    /**
     * Render the object that implements it.
     *
     * @param g2d the Graphics2D object responsible for drawing
     */
    @Override
    public void render(Graphics2D g2d) {
        x.render(g2d);
        y.render(g2d);
        signal.render(g2d);
        status.render(g2d);
        xVerdict.render(g2d);
        yVerdict.render(g2d);
        signalVerdict.render(g2d);
        statusVerdict.render(g2d);
        borders.render(g2d);
    }
}
