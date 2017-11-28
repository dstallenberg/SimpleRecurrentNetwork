package com.dimitri.nffnn.gfx;

import com.dimitri.nffnn.neural.Net;

import java.awt.*;

public abstract class GraphicsObject {

    private Net net;
    private int x, y;
    private int width, height;

    public GraphicsObject(Net net, int x, int y, int width, int height){
        this.net = net;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void render(Graphics graphics);

    public Net getNet() {
        return net;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
