package com.dimitri.nffnn.neural.cells;

import com.dimitri.nffnn.neural.Connection;
import com.dimitri.nffnn.neural.ProcessorCell;
import com.dimitri.nffnn.neural.layers.Layer;

public class RecurrentHiddenCell extends HiddenCell{

    private Connection[] recurrentConnections;

    public RecurrentHiddenCell(Layer layer, int cellIndex, int inputAmount) {
        super(layer, cellIndex, inputAmount);
        recurrentConnections = new Connection[layer.getNet().getTopology(layer.getNet().getTopology().length-1)];
        for (int i = 0; i < recurrentConnections.length; i++) {
            recurrentConnections[i] = new Connection(getRandom());
        }

    }

    public RecurrentHiddenCell(Layer layer, int cellIndex, int inputAmount, double[] weights, double[] recurrentWeights) {
        super(layer, cellIndex, inputAmount, weights);
        recurrentConnections = new Connection[recurrentWeights.length];
        for (int i = 0; i < recurrentConnections.length; i++) {
            recurrentConnections[i] = new Connection(recurrentWeights[i]);
        }
    }

    @Override
    public void feedForward(){
        double sum = 0;

        Layer lastLayer = getLayer().getNet().getLayer(getLayer().getNet().getLayer().length-1);

        for (int i = 0; i < lastLayer.getCell().length; i++) {
            sum += lastLayer.getCell(i).getOutput() * recurrentConnections[i].getWeight();
        }

        sum += 1*recurrentConnections[recurrentConnections.length-1].getWeight();

        Layer previousLayer = getLayer().getNet().getLayer(getLayer().getLayerIndex()-1);

        for (int i = 0; i < previousLayer.getCell().length; i++) {
            sum += previousLayer.getCell(i).getOutput() * getConnection(i).getWeight();
        }

        sum += 1*getConnection(getConnection().length-1).getWeight();

        setOutput(ProcessorCell.tanh(sum));
    }

    @Override
    public void calcHiddenGradient(){
        double sum = 0;

        Layer lastLayer = getLayer().getNet().getLayer(getLayer().getNet().getLayer().length-1);

        for (int i = 0; i < lastLayer.getCell().length; i++) {
            sum += recurrentConnections[i].getWeight() * ((OutputCell)lastLayer.getCell(i)).getGradient();
//            sum += ((RecurrentHiddenCell)getLayer().getCell(i)).getRecurrentConnections(getCellIndex()).getWeight()*((RecurrentHiddenCell) getLayer().getCell(i)).getGradient();
        }

        for (int i = 0; i < getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell().length; i++) {
            sum += ((HiddenCell)getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell(i)).getConnection(getCellIndex()).getWeight()*((HiddenCell)getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell(i)).getGradient();
        }
        setGradient(sum * ProcessorCell.derrivativeTanh(getOutput()));
    }

    @Override
    public void updateWeights(){
        super.updateWeights();

        for (int i = 0; i < recurrentConnections.length; i++) {
            Cell cell = getLayer().getNet().getLayer(getLayer().getNet().getLayer().length-1).getCell(i);
            double oldDeltaWeight = recurrentConnections[i].getDeltaWeight();
            double newDeltaWeight = getEta() * cell.getOutput() * getGradient() + getAlpha() * oldDeltaWeight;
            recurrentConnections[i].setDeltaWeight(newDeltaWeight);
            recurrentConnections[i].setWeight(recurrentConnections[i].getWeight()+newDeltaWeight);
        }
    }

    public Connection[] getRecurrentConnections() {
        return recurrentConnections;
    }

    public Connection getRecurrentConnections(int index) {
        return recurrentConnections[index];
    }
}
