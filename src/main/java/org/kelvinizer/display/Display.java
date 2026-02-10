package org.kelvinizer.display;

import org.kelvinizer.animation.AnimatablePanel;
import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.params.GeneralParams;
import org.kelvinizer.shapes.COval;
import org.kelvinizer.shapes.CRect;
import org.kelvinizer.shapes.CShape;
import org.kelvinizer.textbox.CRectTextBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashSet;

import static org.kelvinizer.params.BorderParams.*;
import static org.kelvinizer.params.ColorParams.*;
import static org.kelvinizer.params.ObjectDimensionParams.*;

public class Display extends AnimatablePanel {
    private final DisplayButtons buttons = new DisplayButtons();
    private final DisplayTextBox textBox = new DisplayTextBox();
    private boolean isButtonSelected = false;

    private final HashSet<CShape> shapes = new HashSet<>();
    private final HashSet<Router> routers = new HashSet<>();
    private CShape selectedShape = null;
    private Router selectedRouter = null;

    public static final double[][] signalStrength = new double[BORDER_SIZE][BORDER_SIZE];
    private final int[] signalPixels;
    private final BufferedImage signalMap;

    private final CRect boundaries = new CRect(BORDER_CENTER_X,BORDER_CENTER_Y, BORDER_SIZE, BORDER_SIZE);
    private CShape currentShape = null;
    private Router currentRouter = null;

    private boolean initPlacement = false;
    private boolean changed = false;
    private boolean optimized = false;

    private boolean shapeInBoarder(CShape shape) {
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

    private void bindingUpdates(String s){
        try{
            if(selectedShape instanceof CRect cr){
                if(textBox.posX.isSelected()){
                    cr.setX(Double.parseDouble(s));
                }
                else if(textBox.posY.isSelected()){
                    cr.setY(Double.parseDouble(s));
                }
                else if(textBox.width.isSelected()){
                    cr.setWidth(Double.parseDouble(s));
                }
                else if(textBox.height.isSelected()){
                    cr.setHeight(Double.parseDouble(s));
                }
            }
            else if(selectedShape instanceof COval cr){
                if(textBox.posX.isSelected()){
                    cr.setX(Double.parseDouble(s));
                }
                else if(textBox.posY.isSelected()){
                    cr.setY(Double.parseDouble(s));
                }
                else if(textBox.width.isSelected()){
                    cr.setWidth(Double.parseDouble(s));
                }
                else if(textBox.height.isSelected()){
                    cr.setHeight(Double.parseDouble(s));
                }
            }
            else if(selectedRouter != null){
                if(textBox.posX.isSelected()){
                    selectedRouter.setX(Double.parseDouble(s));
                }
                else if(textBox.posY.isSelected()){
                    selectedRouter.setY(Double.parseDouble(s));
                }
                else if(textBox.initial.isSelected()){
                    selectedRouter.setInitialDBM(Double.parseDouble(s));
                }
                else if(textBox.decrease.isSelected()){
                    selectedRouter.setDecreaseRate(Double.parseDouble(s));
                }
                changed = true;
            }
            else{
                System.err.println("Invalid Shape");
            }
        } catch (NumberFormatException _){}
    }

    private void bindNumbers(int num){
        addKeyBinding(0x30 + num, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedShape != null || selectedRouter != null){
                    bindingUpdates(textBox.updateText((char) ('0' + num)));
                }
            }
        });
    }

    private void deleteThing(){
        shapes.remove(selectedShape);
        routers.remove(selectedRouter);
        selectedShape = null;
        selectedRouter = null;
    }

    private void duplicateThing(){
        if(selectedShape != null){
            CShape newShape = selectedShape.clone();
            if(newShape instanceof CRect cr){
                cr.setPosition(cr.getX()+DUPLICATE_DIST, cr.getY()+DUPLICATE_DIST);
                if(!shapeInBoarder(cr)){
                    cr.setPosition(BORDER_CENTER_X, BORDER_CENTER_Y);
                }
                selectedShape.setFillColor(colorPlaced);
                selectedShape = cr;
                shapes.add(cr);
            }
            else if(newShape instanceof COval cr){
                cr.setPosition(cr.getX()+DUPLICATE_DIST, cr.getY()+DUPLICATE_DIST);
                if(!shapeInBoarder(cr)){
                    cr.setPosition(BORDER_CENTER_X, BORDER_CENTER_Y);
                }
                selectedShape.setFillColor(colorPlaced);
                selectedShape = cr;
                shapes.add(cr);
            }
            else{
                System.err.println("Invalid Shape");
            }
        }
        else if(selectedRouter != null){
            Router newRouter = selectedRouter.clone();
            newRouter.setPosition(newRouter.getX()+DUPLICATE_DIST, newRouter.getY()+DUPLICATE_DIST);
            if(!shapeInBoarder(newRouter)){
                newRouter.setPosition(BORDER_CENTER_X, BORDER_CENTER_Y);
            }
            selectedRouter.setFillColor(routerPlacedColor);
            selectedRouter = newRouter;
            routers.add(newRouter);
            changed = true;
        }
    }

    private void clearShape(){
        shapes.clear();
        routers.clear();
        selectedShape = null;
        selectedRouter = null;
        currentShape = null;
        currentRouter = null;
    }

    /**
     * Creates an {@code AnimatablePanel} with a specified start duration for the appearance animation.
     */
    public Display() {
        super(500, GeneralParams.REF_WIN_W, GeneralParams.REF_WIN_H);
        boundaries.setFillColor(BORDER_COLOR);
        signalMap = new BufferedImage(
                BORDER_SIZE,BORDER_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );
        signalPixels = ((DataBufferInt)
                signalMap.getRaster().getDataBuffer()).getData();
        for(int i=0; i<=9; i++){
            bindNumbers(i);
        }
        addKeyBinding(KeyEvent.VK_BACK_SPACE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bindingUpdates(textBox.updateText('\b'));
            }
        });
        addKeyBinding(KeyEvent.VK_PERIOD, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bindingUpdates(textBox.updateText('.'));
            }
        });
        addKeyBinding(KeyEvent.VK_DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteThing();
            }
        });
        addKeyBinding(KeyEvent.VK_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearShape();
            }
        });
    }

    @Override
    public void scale(Dimension d){
        buttons.scale(d);
        panelSize = d;
    }

    private void selectButton(Object button, boolean hasSelection){
        if(button instanceof CRectButton crb){
            if(crb.isFocused()){
                isButtonSelected = true;
                if(hasSelection){
                    crb.select();
                }
            }
            else{
                crb.select(false);
            }
        }
        else if(button instanceof CRectTextBox crbt){
            if(crbt.isFocused()){
                crbt.select();
                isButtonSelected = true;
            }
            else{
                crbt.select(false);
            }
        }
        else{
            System.err.println("Error: Invalid button type");
        }
    }

    private void selectMenuButtons(){
        isButtonSelected = false;

        selectButton(buttons.rectangle, true);
        selectButton(buttons.oval, true);
        selectButton(buttons.router, true);

        if(selectedShape != null){
            selectButton(textBox.posX, true);
            selectButton(textBox.posY, true);
            selectButton(textBox.height, true);
            selectButton(textBox.width, true);
        }
        else if(selectedRouter != null){
            selectButton(textBox.posX, true);
            selectButton(textBox.posY, true);
            selectButton(textBox.initial, true);
            selectButton(textBox.decrease, true);
        }

        selectButton(buttons.delete, false);
        selectButton(buttons.forward, false);
        selectButton(buttons.back, false);
        selectButton(buttons.clear, false);
        selectButton(buttons.duplicate, false);
        selectButton(buttons.load, false);
        selectButton(buttons.save, false);
        selectButton(buttons.optimize, false);
    }

    private void selectShape(MouseEvent e){
        if(selectedShape != null){
            selectedShape.setFillColor(colorPlaced);
        }
        selectedShape = null;
        for (CShape shape : shapes) {
            if (shape.toJShape().contains(e.getPoint())) {
                selectedShape = shape;
                textBox.initializeText(shape);
                shape.setFillColor(colorSelected);
                return;
            }
        }
    }

    private void selectRouter(MouseEvent e){
        if(selectedRouter != null){
            selectedRouter.setFillColor(routerPlacedColor);
        }
        selectedRouter = null;
        for (Router r : routers) {
            if (r.toJShape().contains(e.getPoint())) {
                selectedRouter = r;
                textBox.initializeText(r);
                selectedRouter.setFillColor(routerSelectedColor);
                return;
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        buttons.setFocused(e);
        if(selectedShape != null){
            textBox.setFocused(e);
            textBox.focusShape(e);
        }
        else if(selectedRouter != null){
            textBox.setFocused(e);
            textBox.focusRouter(e);
        }
        if(currentShape != null){
            if(currentShape instanceof CRect cr){
                cr.setPosition(e, panelSize);
            }
            else if(currentShape instanceof COval cr){
                cr.setPosition(e, panelSize);
            }
        }
        else if(currentRouter != null){
            currentRouter.setPosition(e, panelSize);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        selectMenuButtons();
        if(isButtonSelected){
            currentShape = null;
            currentRouter = null;
            if(buttons.rectangle.isFocused()){
                currentShape = new CRect(e.getX(), e.getY(), 50, 50);
                currentShape.setFillColor(colorNotPlaced);
                initPlacement = true;
            }
            else if(buttons.oval.isFocused()){
                currentShape = new COval(e.getX(), e.getY(), INITIAL_WIDTH, INITIAL_HEIGHT);
                currentShape.setFillColor(colorNotPlaced);
                initPlacement = true;
            }
            else if(buttons.router.isFocused()) {
                currentRouter = new Router(INITIAL_DBM, DECREASE_RATE);
                currentRouter.setFillColor(routerNotPlacedColor);
                initPlacement = true;
            }
            else if(buttons.delete.isFocused()){
                if(selectedShape != null){
                    deleteThing();
                }
            }
            else if(buttons.clear.isFocused()){
                clearShape();
            }
            else if(buttons.duplicate.isFocused()){
                duplicateThing();
            }
            else if(buttons.optimize.isFocused() && !optimized){
                optimize();
                optimized = true;
            }
        }
        else{
            if(initPlacement && shapeInBoarder(currentShape)){
                currentShape.setFillColor(colorPlaced);
                shapes.add(currentShape);
                currentShape = null;
                initPlacement = false;
            }
            else if(initPlacement && currentRouter != null){
                currentRouter.setFillColor(routerPlacedColor);
                routers.add(currentRouter);
                currentRouter = null;
                initPlacement = false;
                changed = true;
            }
            else{
                selectShape(e);
                selectRouter(e);
            }
        }
    }

    private void calculateSignalStrength(){
        for(int i=0; i<signalStrength.length; i++){
            for(int j=0; j<signalStrength[0].length; j++){
                signalStrength[i][j] = -256;
            }
        }
        for(Router r : routers){
            r.calculateSignalStrength();
        }
    }

    private void optimize(){}

    private void renderSignalStrength(Graphics2D g2d){
        int idx = 0;
        for (int y = 0; y < BORDER_SIZE; y++) {
            for (int x = 0; x < BORDER_SIZE; x++) {
                double strength = signalStrength[x][y];
                byte alpha = 0;
                if(strength != Double.POSITIVE_INFINITY){
                    alpha = (byte) Math.clamp(255.0 + strength, 0, 255);
                }
                // ARGB: A R G B
                signalPixels[idx++] =
                        (alpha << 24) |  (0x00FF00); // Green color with variable alpha
            }
        }
        g2d.drawImage(
                signalMap,
                BORDER_CENTER_X - BORDER_SIZE / 2,
                BORDER_CENTER_Y - BORDER_SIZE / 2,
                null
        );
    }

    @Override
    public void render(Graphics2D g2d) {
        buttons.render(g2d);
        if(selectedShape != null){
            textBox.render(g2d);
            textBox.renderShape(g2d);
        }
        else if(selectedRouter != null){
            textBox.render(g2d);
            textBox.renderRouter(g2d);
        }
        boundaries.render(g2d);

        if (changed) {
            calculateSignalStrength();
            changed = false;
        }
        if (!routers.isEmpty()) {
            renderSignalStrength(g2d);
        }

        if (shapeInBoarder(currentShape)) {
            currentShape.render(g2d);
        }
        if (shapeInBoarder(currentRouter)) {
            currentRouter.render(g2d);
        }
        for (CShape s: shapes) {
            s.render(g2d);
        }
        for (Router r : routers) {
            r.render(g2d);
        }
    }
}
