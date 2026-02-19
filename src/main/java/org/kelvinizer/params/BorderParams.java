package org.kelvinizer.params;

import org.kelvinizer.shapes.COval;
import org.kelvinizer.shapes.CRect;
import org.kelvinizer.shapes.CShape;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BorderParams {
    public static int BORDER_SIZE = 600;
    public static Color BORDER_COLOR = Color.WHITE;
    public static int BORDER_CENTER_X = 730;
    public static int BORDER_CENTER_Y = 400;

    public static CRect boundaries = new CRect(BORDER_CENTER_X, BORDER_CENTER_Y, BORDER_SIZE, BORDER_SIZE);

    public static int DUPLICATE_DIST = 20;
    public static double AIR_LOSS_PER_UNIT = 0.08;

    public static boolean environmentChanged = false;
    public static boolean layoutChanged = false;
    public static boolean isSaved = false;

    public static boolean shapeInBoarder(CShape shape) {
        if(shape instanceof CRect) {
            Rectangle r = ((CRect) shape).toJShape();
            Rectangle b = boundaries.toJShape();
            return b.contains(r);
        }
        else if(shape instanceof COval){
            Ellipse2D.Double o = ((COval) shape).toJShape();
            Rectangle b = boundaries.toJShape();
            return b.contains(o.getBounds());
        }
        else{
            return false;
        }
    }
}
