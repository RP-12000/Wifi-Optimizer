package org.kelvinizer.shapes;

import org.kelvinizer.params.GeneralParams;
import org.kelvinizer.misc.objects.Pair;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class COval extends CShape {
    private double x;

    private double y;

    private double width;

    private double height;

    private Pair<Double, Double> origin;

    public COval(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.origin = new Pair<>(width / 2, height / 2);
    }

    public COval(double width, double height) {
        this(0, 0, width, height);
    }

    public COval() {
        this(0, 0);
    }

    public Pair<Double, Double> getOrigin() {
        return origin;
    }

    public void setOrigin(Pair<Double, Double> origin) {
        this.origin = origin;
    }

    public void setOrigin(double x, double y) {
        setOrigin(new Pair<>(x, y));
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public Ellipse2D.Double toJShape() {
        return new Ellipse2D.Double(x-origin.first, y-origin.second, width, height);
    }

    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(MouseEvent e, Dimension d){
        setPosition((double) (e.getX()) / d.width * GeneralParams.REF_WIN_W, (double) (e.getY()) * GeneralParams.REF_WIN_H / d.height);
    }

    @Override
    public void scale(Dimension d) {
        x = x / GeneralParams.REF_WIN_W * d.width;
        y = y / GeneralParams.REF_WIN_H * d.height;
        width = width / GeneralParams.REF_WIN_W * d.width;
        height = height / GeneralParams.REF_WIN_H * d.height;
    }

    @Override
    public COval clone() {
        COval copy = new COval();

        // 拷贝父类状态
        copy.setFillColor(this.getFillColor());
        copy.setOutlineColor(this.getOutlineColor());
        copy.setOutlineThickness(this.getOutlineThickness());

        // 拷贝基本类型
        copy.x = this.x;
        copy.y = this.y;
        copy.width = this.width;
        copy.height = this.height;

        // ⭐ 关键：深拷贝 origin
        copy.origin = new Pair<>(
                this.origin.first,
                this.origin.second
        );

        return copy;
    }
}