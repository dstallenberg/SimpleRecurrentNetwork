package com.dimitri.nffnn.gfx;

import com.dimitri.nffnn.neural.Net;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TimeGraph extends GraphicsObject{

    private List<Double> xAxis;
    private List<Double> yAxis;
    
    private int scaling = 1;

    private String yAxisName;
    private long timer = 0;
    private long startTime = 0;
    private long timeSec = 0;

    public TimeGraph(Net net, int x, int y, int width, int height, String yAxisName) {
        super(net, x, y, width, height);
        xAxis = new ArrayList<>();
        yAxis = new ArrayList<>();
        startTime = System.nanoTime();
        this.yAxisName = yAxisName;
    }

    public void addValue(double value){
        xAxis.add(value);
    }
    
    @Override
    public void update() {
        timer = System.nanoTime()-startTime;
        timeSec = timer/1000000;
        timeSec = timeSec/1000;
        while(xAxis.size() > getWidth()){
            xAxis.remove(0);
        }
    }

    @Override
    public void render(Graphics graphics) {
        graphics.translate(getX(), getY());
        graphics.drawRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < xAxis.size()-1; i+=scaling) {
            graphics.drawLine((int)(i/scaling), (int)(getHeight()-xAxis.get(i)*getHeight()), (int)((i+1)/scaling), (int)(getHeight()-xAxis.get(i+1)*getHeight()));
        }
        graphics.translate(-getX(), -getY());
    }

    public void setScaling(int scaling) {
        this.scaling = scaling;
    }
}
