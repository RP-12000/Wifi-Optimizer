package org.kelvinizer.display;

import org.kelvinizer.shapes.COval;

import java.awt.*;

import static org.kelvinizer.params.BorderParams.*;
import static org.kelvinizer.params.ColorParams.routerPlacedColor;
import static org.kelvinizer.params.ObjectDimensionParams.ROUTER_SIZE;

public class Router extends COval {
    private double initialDBM;
    private double decreaseRate;

    private double decreaseFunction(double x, double y){
        double distance = Math.sqrt(x*x + y*y);
        return - initialDBM - decreaseRate * distance;
    }

    public Router(double x, double y, double initialDBM, double decreaseRate){
        super(ROUTER_SIZE,ROUTER_SIZE);
        super.setFillColor(routerPlacedColor);
        super.setPosition(x, y);
        this.initialDBM = initialDBM;
        this.decreaseRate = decreaseRate;
    }

    public Router(double initialDBM, double decreaseRate) {
        this(BORDER_CENTER_X, BORDER_CENTER_Y, initialDBM, decreaseRate);
    }

    public void calculateSignalStrength(){
        double[][] signalStrength = new double[BORDER_SIZE][BORDER_SIZE];
        for(int i = 0; i < signalStrength.length; i++){
            for(int j = 0; j < signalStrength[0].length; j++){
                double trueX = getX() - (i + BORDER_CENTER_X - (double) BORDER_SIZE / 2);
                double trueY = getY() - (j + BORDER_CENTER_Y - (double) BORDER_SIZE / 2);
                double signal = decreaseFunction(trueX, trueY);
                double exponentDiff = Math.abs(Display.signalStrength[i][j] - signal);
                if(exponentDiff >= 50){
                    Display.signalStrength[i][j] = Math.max(Display.signalStrength[i][j], signal);
                }
                else{
                    Display.signalStrength[i][j] =
                            Math.min(Display.signalStrength[i][j], signal) +
                                    Math.log(1.0+Math.exp(exponentDiff));
                }
            }
        }
    }

    public double getInitialDBM() {
        return initialDBM;
    }

    public void setInitialDBM(double initialDBM) {
        this.initialDBM = initialDBM;
    }

    public double getDecreaseRate() {
        return decreaseRate;
    }

    public void setDecreaseRate(double decreaseRate) {
        this.decreaseRate = decreaseRate;
    }

    @Override
    public Router clone(){
        Router r = new Router(this.getX(), this.getY(), initialDBM, decreaseRate);
        r.setFillColor(this.getFillColor());
        return r;
    }
}