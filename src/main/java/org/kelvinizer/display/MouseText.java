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

    private void setBounds(BoundedString bs, int x){
        bs.setBounds(new CRect(x, 710, 200, 20));
        bs.setMaxStringSize(10);
    }

    public MouseText(){
        setBounds(x, 530);
        setBounds(y, 730);
        setBounds(signal, 930);
    }

    public void updateStatus(Point p, Dimension d){
        int X = (int) (p.getX() / d.width * REF_WIN_W + (double) BORDER_SIZE / 2 - BORDER_CENTER_X);
        int Y = (int) (p.getY() / d.height * REF_WIN_H + (double) BORDER_SIZE / 2 - BORDER_CENTER_Y);
        x.setString("X: "+X);
        y.setString("Y: "+Y);
        signal.setString("Signal Strength: "+Display.signalStrength[X][Y]+" dBm");
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
    }
}
