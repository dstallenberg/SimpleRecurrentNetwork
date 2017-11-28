package com.dimitri.nffnn.neural.cells;

import com.dimitri.nffnn.neural.Connection;
import com.dimitri.nffnn.neural.layers.Layer;

public abstract class Cell {

    private Layer layer;
    private int cellIndex;

    private double output;

    public Cell(Layer layer, int cellIndex){
        this.layer = layer;
        this.cellIndex = cellIndex;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public Layer getLayer() {
        return layer;
    }

    public int getCellIndex() {
        return cellIndex;
    }
}
