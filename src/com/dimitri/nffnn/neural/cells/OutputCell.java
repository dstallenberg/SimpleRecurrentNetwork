package com.dimitri.nffnn.neural.cells;

import com.dimitri.nffnn.neural.ProcessorCell;
import com.dimitri.nffnn.neural.layers.Layer;

public class OutputCell extends HiddenCell{

    public OutputCell(Layer layer, int cellIndex, int inputAmount) {
        super(layer, cellIndex, inputAmount);
    }

    public OutputCell(Layer layer, int cellIndex, int inputAmount, double[] weights) {
        super(layer, cellIndex, inputAmount, weights);
    }

    public void calcOutputGradient(double targetOutput){
        double delta = targetOutput - getOutput();
        setGradient(delta * ProcessorCell.derrivativeTanh(getOutput()));
    }

    @Override
    public void feedForward() {
        super.feedForward();
    }

    @Override
    public void updateWeights() {
        super.updateWeights();
    }
}
