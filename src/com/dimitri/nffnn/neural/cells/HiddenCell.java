package com.dimitri.nffnn.neural.cells;

import com.dimitri.nffnn.neural.Connection;
import com.dimitri.nffnn.neural.ProcessorCell;
import com.dimitri.nffnn.neural.layers.Layer;

import java.util.Random;

public class HiddenCell extends Cell{

    private double eta = 0.01;
    private double alpha = 0.9;

    private Connection[] connection;

    private double gradient;

    public HiddenCell(Layer layer, int cellIndex, int inputAmount) {
        super(layer, cellIndex);
        connection = new Connection[inputAmount+1];
        for (int i = 0; i < connection.length; i++) {
            connection[i] = new Connection(getRandom());
        }
    }

    public HiddenCell(Layer layer, int cellIndex, int inputAmount, double[] weights) {
        super(layer, cellIndex);
        connection = new Connection[inputAmount+1];
        for (int i = 0; i < connection.length; i++) {
            connection[i] = new Connection(weights[i]);
        }
    }

    public void feedForward(){
        double sum = 0;
        Layer previousLayer = getLayer().getNet().getLayer(getLayer().getLayerIndex()-1);

        for (int i = 0; i < previousLayer.getCell().length; i++) {
            sum += previousLayer.getCell(i).getOutput() * connection[i].getWeight();
        }
        sum += 1*connection[connection.length-1].getWeight();
        setOutput(ProcessorCell.tanh(sum));
    }

    public void calcHiddenGradient(){
        double sum = 0;

        for (int i = 0; i < getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell().length; i++) {
            sum += ((HiddenCell)getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell(i)).getConnection(getCellIndex()).getWeight()*((HiddenCell)getLayer().getNet().getLayer(getLayer().getLayerIndex()+1).getCell(i)).getGradient();
        }

        gradient = sum * ProcessorCell.derrivativeTanh(getOutput());
    }

    public void updateWeights(){
        for (int i = 0; i < getLayer().getNet().getLayer(getLayer().getLayerIndex()-1).getCell().length; i++) {
            Cell cell = getLayer().getNet().getLayer(getLayer().getLayerIndex()-1).getCell(i);
            double oldDeltaWeight = connection[i].getDeltaWeight();
            double newDeltaWeight = eta * cell.getOutput() * gradient + alpha * oldDeltaWeight;
            connection[i].setDeltaWeight(newDeltaWeight);
            connection[i].setWeight(connection[i].getWeight()+newDeltaWeight);
        }
    }

    public double getGradient() {
        return gradient;
    }

    public Connection[] getConnection(){
        return connection;
    }

    public Connection getConnection(int index){
        return connection[index];
    }

    public double getRandom(){
        return (new Random().nextDouble()*2)-1;
    }

    public double getEta() {
        return eta;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }
}
