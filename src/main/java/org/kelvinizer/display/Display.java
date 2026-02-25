package org.kelvinizer.display;

import org.kelvinizer.animation.AnimatablePanel;
import org.kelvinizer.buttons.CRectButton;
import org.kelvinizer.misc.objects.BoundedString;
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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.kelvinizer.App.panelSize;
import static org.kelvinizer.params.BorderParams.*;
import static org.kelvinizer.params.ColorParams.*;
import static org.kelvinizer.params.GeneralParams.REF_WIN_H;
import static org.kelvinizer.params.GeneralParams.REF_WIN_W;
import static org.kelvinizer.params.ObjectDimensionParams.*;

public class Display extends AnimatablePanel {
    private final DisplayButtons buttons = new DisplayButtons();
    private final DisplayTextBox textBox = new DisplayTextBox();
    private final Separators sep = new Separators();
    private final MouseText mouseText = new MouseText();
    private boolean isButtonSelected = false;

    public static final ArrayList<CShape> shapes = new ArrayList<>();
    public static final boolean[][] wallMask = new boolean[BORDER_SIZE][BORDER_SIZE];
    public static final ArrayList<Router> routers = new ArrayList<>();
    public static CShape selectedShape = null;
    public static Router selectedRouter = null;

    public static final double[][] signalStrength = new double[BORDER_SIZE][BORDER_SIZE];
    private final int[] signalPixels;
    private final BufferedImage signalMap;

    private CShape currentShape = null;
    private Router currentRouter = null;
    private boolean initPlacement = false;
    private boolean inOptimizationProgress = false;

    private static final BoundedString fileNameDisplay = new BoundedString("");

    private void bindNumbers(int num){
        addKeyBinding(0x30 + num, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedShape != null || selectedRouter != null){
                    textBox.updateText((char) ('0' + num));
                }
            }
        });
    }

    private void deleteThing(){
        if(selectedShape != null){
            shapes.remove(selectedShape);
            selectedShape = null;
            recalibrateWallMask();
        }
        else{
            routers.remove(selectedRouter);
            selectedRouter = null;
        }
        refreshStatus();
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
            recalibrateWallMask();
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
        }
        refreshStatus();
    }

    private void save() {
        try {
            Path layoutDir = Paths.get("Layouts");
            if (!Files.exists(layoutDir)) {
                Files.createDirectory(layoutDir);
            }
            Path filePath = layoutDir.resolve(fileNameDisplay.getString()+".layout");
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {

                for (CShape s : shapes) {
                    if (s instanceof CRect r) {
                        writer.write(String.format("1 %.4f %.4f %.4f %.4f",
                                r.getX(), r.getY(),
                                r.getWidth(), r.getHeight()));
                    } else if (s instanceof COval o) {
                        writer.write(String.format("2 %.4f %.4f %.4f %.4f",
                                o.getX(), o.getY(),
                                o.getWidth(), o.getHeight()));
                    }
                    writer.newLine();
                }

                for (Router r : routers) {
                    writer.write(String.format("3 %.4f %.4f %.4f %.4f",
                            r.getX(), r.getY(),
                            r.getInitialDBM(), r.getWallLoss()));
                    writer.newLine();
                }

                writer.write(Double.toString(AIR_LOSS_PER_UNIT));
            }
            isSaved = true;
        } catch (IOException _) {}
    }

    private void load(File f) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 1) {
                    AIR_LOSS_PER_UNIT = Double.parseDouble(parts[0]);
                    break;
                }
                else if(parts.length == 5){
                    int type = Integer.parseInt(parts[0]);
                    double a = Double.parseDouble(parts[1]);
                    double b = Double.parseDouble(parts[2]);
                    double c = Double.parseDouble(parts[3]);
                    double d = Double.parseDouble(parts[4]);
                    switch (type) {
                        case 1 -> {
                            CRect r = new CRect(a, b, c, d);
                            r.setFillColor(colorPlaced);
                            shapes.add(r);
                        }
                        case 2 -> {
                            COval o = new COval(a, b, c, d);
                            o.setFillColor(colorPlaced);
                            shapes.add(o);
                        }
                        case 3 -> {
                            Router r = new Router();
                            r.setPosition(a, b);
                            r.setInitialDBM(c);
                            r.setWallLoss(d);
                            r.setFillColor(routerPlacedColor);
                            r.recalibrateSignalStrength();
                            routers.add(r);
                        }
                    }
                }
            }
        }
        recalibrateWallMask();
        refreshStatus();
        isSaved = true;
    }

    private void load() {
        try {
            Path layoutDir = Paths.get("Layouts");
            if (!Files.exists(layoutDir)) {
                Files.createDirectory(layoutDir);
            }

            JFileChooser chooser = new JFileChooser(layoutDir.toFile());
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Layout Files", "layout"));

            int result = chooser.showOpenDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = chooser.getSelectedFile();
            clearShape();
            load(file);
            fileNameDisplay.setString(file.getName().replace(".layout", ""));
        } catch (Exception _) {}
    }

    private void clearShape(){
        shapes.clear();
        routers.clear();
        selectedShape = null;
        selectedRouter = null;
        currentShape = null;
        currentRouter = null;
        recalibrateWallMask();
        layoutChanged = true;
    }

    public static void refreshStatus(){
        layoutChanged = true;
        isSaved = false;
    }

    private void generateRandomFileName(){
        Random random = new Random();
        long number = Math.abs(random.nextLong()) % 1_000_000_0000L;
        fileNameDisplay.setString("Untitled_Design_"+String.format("%010d", number));
    }

    /**
     * Creates an {@code AnimatablePanel} with a specified start duration for the appearance animation.
     */
    public Display() {
        super(1000, REF_WIN_W, GeneralParams.REF_WIN_H);
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
                textBox.updateText('\b');
            }
        });
        addKeyBinding(KeyEvent.VK_PERIOD, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textBox.updateText('.');
            }
        });
        addKeyBinding(KeyEvent.VK_MINUS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textBox.updateText('-');
            }
        });
        addKeyBinding(KeyEvent.VK_DELETE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteThing();
            }
        });
        addKeyBinding(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                duplicateThing();
            }
        });
        addKeyBinding(KeyEvent.VK_C, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearShape();
            }
        });
        addKeyBinding(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedRouter != null){
                    optimize();
                }
            }
        });
        addKeyBinding(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        addKeyBinding(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                load();
            }
        });
        addKeyBinding(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
                clearShape();
                generateRandomFileName();
            }
        });
        addKeyBinding(KeyEvent.VK_LEFT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(-1, 0, false);
            }
        });
        addKeyBinding(KeyEvent.VK_RIGHT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(1, 0, false);
            }
        });
        addKeyBinding(KeyEvent.VK_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(0, -1, false);
            }
        });
        addKeyBinding(KeyEvent.VK_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(0, 1, false);
            }
        });

        addKeyBinding(KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(-1, 0, true);
            }
        });
        addKeyBinding(KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(1, 0, true);
            }
        });
        addKeyBinding(KeyEvent.VK_UP, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(0, -1, true);
            }
        });
        addKeyBinding(KeyEvent.VK_DOWN, KeyEvent.CTRL_DOWN_MASK, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardMoveOrResize(0, 1, true);
            }
        });
        if(fileNameDisplay.getString().isEmpty()){
            fileNameDisplay.setBounds(new CRect(100, 45, 180, 60));
            fileNameDisplay.getBounds().setFillColor(Color.WHITE);
            fileNameDisplay.setStringColor(Color.BLACK);
            fileNameDisplay.setMaxStringSize(20);
            fileNameDisplay.setStyle(Font.BOLD);
            generateRandomFileName();
            for(int i=0; i<signalStrength.length; i++){
                for(int j=0; j<signalStrength[0].length; j++){
                    signalStrength[i][j] = Double.NEGATIVE_INFINITY;
                }
            }
        }
    }

    private void mouseMoveOrResize(MouseEvent e){
        if(selectedShape instanceof CRect cr){
            CRect test = cr.clone();
            test.setPosition(e, panelSize);
            if(shapeInBoarder(test)){
                cr.setPosition(e, panelSize);
                recalibrateWallMask();
                refreshStatus();
            }
            textBox.initializeText(selectedShape);
        }
        else if(selectedShape instanceof COval cr){
            COval test = cr.clone();
            test.setPosition(e, panelSize);
            if(shapeInBoarder(test)){
                cr.setPosition(e, panelSize);
                recalibrateWallMask();
                refreshStatus();
            }
            textBox.initializeText(selectedShape);
        }
        else if(selectedRouter != null){
            Router test = selectedRouter.clone();
            test.setPosition(e, panelSize);
            if(shapeInBoarder(test)){
                selectedRouter.setPosition(e, panelSize);
            }
            textBox.initializeText(selectedRouter);
        }

    }

    private void keyboardMoveOrResize(int dx, int dy, boolean ctrl) {
        if(selectedShape instanceof CRect cr){
            CRect test = cr.clone();
            if(ctrl){
                if(dx != 0){test.setWidth(Math.max(1, cr.getWidth() + dx));}
                if(dy != 0){test.setHeight(Math.max(1, cr.getHeight() + dy));}
                if(shapeInBoarder(test)){
                    if(dx != 0){cr.setWidth(test.getWidth());}
                    if(dy != 0){cr.setHeight(test.getHeight());}
                    recalibrateWallMask();
                    refreshStatus();
                }
            }
            else{
                test.setPosition(test.getX() + dx, test.getY() + dy);
                if(shapeInBoarder(test)){
                    cr.setPosition(test.getX(), test.getY());
                    recalibrateWallMask();
                    refreshStatus();
                }
            }
            textBox.initializeText(selectedShape);
        }
        else if(selectedShape instanceof COval cr){
            COval test = cr.clone();
            if(ctrl){
                if(dx != 0){test.setWidth(Math.max(1, cr.getWidth() + dx));}
                if(dy != 0){test.setHeight(Math.max(1, cr.getHeight() + dy));}
                if(shapeInBoarder(test)){
                    if(dx != 0){cr.setWidth(test.getWidth());}
                    if(dy != 0){cr.setHeight(test.getHeight());}
                    recalibrateWallMask();
                    refreshStatus();
                }
            }
            else{
                test.setPosition(test.getX() + dx, test.getY() + dy);
                if(shapeInBoarder(test)){
                    cr.setPosition(test.getX(), test.getY());
                    recalibrateWallMask();
                    refreshStatus();
                }
            }
            textBox.initializeText(selectedShape);
        }
        else if(selectedRouter != null){
            if(ctrl){
                if(dx != 0){selectedRouter.setInitialDBM(selectedRouter.getInitialDBM() + dx);}
                if(dy != 0){selectedRouter.setWallLoss(selectedRouter.getWallLoss() + dy);}
                refreshStatus();
            }
            else{
                Router test = selectedRouter.clone();
                test.setPosition(selectedRouter.getX() + dx, selectedRouter.getY() + dy);
                if(shapeInBoarder(test)){
                    selectedRouter.setPosition(test.getX(), test.getY());
                    refreshStatus();
                }
            }
            textBox.initializeText(selectedRouter);
        }
    }

    @Override
    public void scale(Dimension d){
        buttons.scale(d);
        textBox.scale(d);
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
        selectButton(buttons.NEW, false);
        selectButton(buttons.duplicate, false);
        selectButton(buttons.load, false);
        selectButton(buttons.save, false);
        selectButton(buttons.optimize, false);
        selectButton(buttons.settings, false);
    }

    private void selectShape(MouseEvent e){
        if(selectedShape != null){
            selectedShape.setFillColor(colorPlaced);
        }
        selectedShape = null;
        for (CShape shape : shapes) {
            if (shape.contains(e.getPoint(), panelSize)) {
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
            if (r.toJShape().contains((double) e.getX() / panelSize.width * REF_WIN_W, (double) e.getY() / panelSize.height * REF_WIN_H)) {
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
        if(boundaries.contains(e.getPoint(), panelSize)){
            mouseText.updateStatus(e.getPoint(), panelSize);
        }
        else{
            mouseText.clearStatus();
        }
        if(selectedShape != null){
            textBox.initializeText(selectedShape);
        }
        else if(selectedRouter != null){
            textBox.initializeText(selectedRouter);
        }
        else{
            textBox.clearText();
        }
    }

    private void clearSelectedObject(){
        if(selectedShape != null){
            selectedShape.setFillColor(colorPlaced);
            selectedShape = null;
        }
        if(selectedRouter != null){
            selectedRouter.setFillColor(routerPlacedColor);
            selectedRouter = null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        selectMenuButtons();
        if(isButtonSelected){
            if(currentShape != null){
                currentShape.setFillColor(colorSelected);
                currentShape = null;
            }
            if(currentRouter != null){
                currentRouter.setFillColor(routerSelectedColor);
                currentRouter = null;
            }
            if(buttons.rectangle.isFocused() && buttons.rectangle.isSelected()){
                clearSelectedObject();
                currentShape = new CRect(e.getX(), e.getY(), 50, 50);
                currentShape.setFillColor(colorNotPlaced);
                initPlacement = true;
            }
            else if(buttons.oval.isFocused() && buttons.oval.isSelected()){
                clearSelectedObject();
                currentShape = new COval(e.getX(), e.getY(), INITIAL_WIDTH, INITIAL_HEIGHT);
                currentShape.setFillColor(colorNotPlaced);
                initPlacement = true;
            }
            else if(buttons.router.isFocused()) {
                clearSelectedObject();
                currentRouter = new Router();
                currentRouter.setFillColor(routerNotPlacedColor);
                initPlacement = true;
            }
            else if(buttons.delete.isFocused()){
                deleteThing();
            }
            else if(buttons.NEW.isFocused()){
                save();
                clearShape();
                generateRandomFileName();
            }
            else if(buttons.duplicate.isFocused()){
                duplicateThing();
            }
            else if(buttons.optimize.isFocused() && selectedRouter != null){
                optimize();
            }
            else if(buttons.save.isFocused()){
                save();
            }
            else if(buttons.load.isFocused()){
                load();
            }
            else if(buttons.settings.isFocused()){
                exit(1000);
            }

        }
        else{
            if(initPlacement && shapeInBoarder(currentShape)){
                currentShape.setFillColor(colorSelected);
                selectedShape = currentShape;
                textBox.initializeText(selectedShape);
                shapes.add(currentShape);
                currentShape = null;
                initPlacement = false;
                recalibrateWallMask();
                refreshStatus();
            }
            else if(initPlacement && shapeInBoarder(currentRouter)){
                currentRouter.setFillColor(routerSelectedColor);
                currentRouter.recalibrateSignalStrength();
                selectedRouter = currentRouter;
                textBox.initializeText(selectedRouter);
                routers.add(currentRouter);
                currentRouter = null;
                initPlacement = false;
                refreshStatus();
            }
            else{
                selectShape(e);
                selectRouter(e);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(selectedShape != null || selectedRouter != null){
            mouseMoveOrResize(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(selectedRouter != null){
            refreshStatus();
        }
    }

    private double expCombine(double a, double b){
        if(a == Double.NEGATIVE_INFINITY && b == Double.NEGATIVE_INFINITY){
            return Double.NEGATIVE_INFINITY;
        }
        else if(a == Double.NEGATIVE_INFINITY){
            return b;
        }
        else if(b == Double.NEGATIVE_INFINITY){
            return a;
        }
        else{
            double diff = Math.abs(a - b);
            if (diff >= 50) {
                return Math.max(a, b);
            }
            double max = Math.max(a, b);
            double min = Math.min(a, b);
            return max + 10 * Math.log10(1 + Math.pow(10, (min - max) / 10));
        }
    }

    private void recalibrateSignalStrength(){
        for(Router r : routers){
            r.recalibrateSignalStrength();
        }
    }

    public static void recalibrateWallMask(){
        for (boolean[] booleans : wallMask) {
            Arrays.fill(booleans, false);
        }
        for(int i=0; i<wallMask.length; i++){
            for(int j=0; j<wallMask[i].length; j++){
                for(CShape shape : shapes){
                    if(shape.toJShape().contains(i - (double) BORDER_SIZE / 2 + BORDER_CENTER_X, j - (double) BORDER_SIZE / 2 + BORDER_CENTER_Y)){
                        wallMask[i][j] = true;
                    }
                }
            }
        }
    }

    private void calculateSignalStrength(){
        for(int i=0; i<signalStrength.length; i++){
            for(int j=0; j<signalStrength[0].length; j++){
                signalStrength[i][j] = Double.NEGATIVE_INFINITY;
            }
        }
        for(Router r : routers){
            for(int i = 0; i < r.signal.length; i++){
                for(int j = 0; j < r.signal[0].length; j++){
                    signalStrength[i][j] = expCombine(signalStrength[i][j], r.signal[i][j]);
                }
            }
        }
    }

    private double calculateScore(Router r){
        double score = 0;
        for(int i = 0; i < BORDER_SIZE; i++){
            for(int j = 0; j < BORDER_SIZE; j++){
                score += expCombine(signalStrength[i][j], r.signal[i][j]);
            }
        }
        return score;
    }

    private Point optimize(Point upper, Point lower){
        int width  = lower.x - upper.x;
        int height = lower.y - upper.y;

        if(width <= 2 || height <= 2){
            return new Point(
                    (lower.x+upper.x) / 2,
                    (lower.y+upper.y) / 2
            );
        }

        int dx = width / Router.CHUNK_SIZE;
        int dy = height / Router.CHUNK_SIZE;

        double bestScore = -1;
        Point bestCenter = null;
        Point bestUpper = null;
        Point bestLower = null;

        for(int i=0;i<Router.CHUNK_SIZE;i++){
            for(int j=0;j<Router.CHUNK_SIZE;j++){

                int x0 = upper.x + i*dx;
                int y0 = upper.y + j*dy;

                int x1 = (i==Router.CHUNK_SIZE-1) ? lower.x : x0 + dx;
                int y1 = (j==Router.CHUNK_SIZE-1) ? lower.y : y0 + dy;

                int cx = (x0 + x1) / 2;
                int cy = (y0 + y1) / 2;

                selectedRouter.recalibrateSignalStrength(cx, cy);

                double score = calculateScore(selectedRouter);

                if(bestCenter == null ||
                        score > bestScore ||
                        (score == bestScore &&
                                (cx < bestCenter.x ||
                                        (cx == bestCenter.x && cy < bestCenter.y)))){

                    bestScore = score;
                    bestCenter = new Point(cx, cy);
                    bestUpper  = new Point(x0, y0);
                    bestLower  = new Point(x1, y1);
                }
            }
        }

        return optimize(bestUpper, bestLower);
    }

    private void optimize(){
        inOptimizationProgress = true;
        routers.remove(selectedRouter);
        recalibrateSignalStrength();
        calculateSignalStrength();
        Point p = optimize(new Point(0, 0), new Point(BORDER_SIZE, BORDER_SIZE));
        selectedRouter.setPosition(
                p.getX() - (double) BORDER_SIZE / 2 + BORDER_CENTER_X,
                p.getY()- (double) BORDER_SIZE / 2 + BORDER_CENTER_Y
        );
        routers.add(selectedRouter);
        selectedRouter.setFillColor(colorPlaced);
        selectedRouter = null;
        layoutChanged = true;
        inOptimizationProgress = false;
    }

    private void renderSignalStrength(Graphics2D g2d){
        int idx = 0;
        for (int y = 0; y < BORDER_SIZE; y++) {
            for (int x = 0; x < BORDER_SIZE; x++) {
                byte alpha = (byte) Math.min(255.0 * Math.pow(255, signalStrength[x][y] / 255), 255);
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
        if(isSaved){
            fileNameDisplay.getBounds().setFillColor(Color.GREEN);
        }
        else{
            fileNameDisplay.getBounds().setFillColor(Color.WHITE);
        }
        fileNameDisplay.render(g2d);
        if(selectedShape != null){
            textBox.renderShape(g2d);
        }
        else if(selectedRouter != null){
            textBox.renderRouter(g2d);
        }
        boundaries.render(g2d);
        if(environmentChanged){
            recalibrateSignalStrength();
            calculateSignalStrength();
            environmentChanged = false;
        }
        if (layoutChanged) {
            recalibrateSignalStrength();
            calculateSignalStrength();
            layoutChanged = false;
        }
        if (!routers.isEmpty() && !inOptimizationProgress) {
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
        mouseText.render(g2d);
        textBox.render(g2d);
        if(selectedShape != null){
            textBox.renderShape(g2d);
        }
        else{
            textBox.renderRouter(g2d);
        }
        sep.render(g2d);
    }

    @Override
    public void toNextPanel(){
        GeneralParams.panelIndex=1;
    }
}