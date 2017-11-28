package com.dimitri.nffnn.neural;

import com.dimitri.nffnn.neural.cells.HiddenCell;
import com.dimitri.nffnn.neural.cells.OutputCell;
import com.dimitri.nffnn.neural.cells.RecurrentHiddenCell;
import com.dimitri.nffnn.neural.layers.*;

import java.io.IOException;

/**
 * Neural net Head Class API
 * @author Dimitri Stallenberg
 * @version 1.0
 */
public class Net {

    private int[] topology = null;

    /**
     * Input Output Object
     */
    private IO io;
    /**
     * FilePath used by the IO object.
     */
    private String filePath = "StandardFilePath.txt";
    /**
     * Array consisting of the layers contained in this net.
     */
    private Layer[] layer;

    /**
     * Array consisting of current input.
     */
    private double[] input;
    /**
     * Array consisting of current output.
     */
    private double[] output;
    /**
     * Array consisting of current targetOutput.
     */
    private double[] targetOutput;

    /**
     * The current error rate.
     */
    private double error = 0;
    /**
     * The recent average error rate.
     */
    private double recentAverageError = 0;
    /**
     * The smoothing factor of which the average error rate is calculated. This lies between 0 and 1.
     */
    private double recentAverageSmoothingFactor = 0.9;

    private boolean recurrent;

    /**
     * Standard constructor for a fresh Neural net
     * @param topology The layout of the net. <br>
     *                 Each int represents the amount of neurons in that particular layer.
     */
    public Net(int[] topology, boolean recurrent){
        this.topology = topology;
        this.recurrent = recurrent;
        io = new IO(filePath);
        if(topology.length >= 2){
            input = new double[topology[0]];
            output = new double[topology[topology.length-1]];
            targetOutput = new double[topology[topology.length-1]];
            layer = new Layer[topology.length];
            layer[0] = new InputLayer(this, topology[0]);
            if(recurrent){
                for (int i = 1; i < topology.length-1; i++) {
                    layer[i] = new RecurrentHiddenLayer(this, i, topology[i]);
                }
            }else {
                for (int i = 1; i < topology.length-1; i++) {
                    layer[i] = new HiddenLayer(this, i, topology[i]);
                }
            }
            layer[layer.length-1] = new OutputLayer(this, layer.length-1, topology[layer.length-1]);
        }else{
            throw new IllegalArgumentException("Nets need at least 2 layers");
        }
    }

    /**
     * Constructor which takes in a String as a filePath. <br>
     * This constructor uses the file as a database for the weights.
     * @param filePath The String of the path to the file to be used.
     * @throws IOException For when the file doesn't exist or has a wrong format.
     */
    public Net(String filePath, boolean recurrent) throws IOException {
        this.recurrent = recurrent;
        io = new IO(filePath);
        io.Read();
        int[] topology = io.getTopology();

        if(topology.length >= 2){
            input = new double[topology[0]];
            output = new double[topology[topology.length-1]];
            targetOutput = new double[topology[topology.length-1]];
            layer = new Layer[topology.length];
            layer[0] = new InputLayer(this, topology[0]);
            if(recurrent){
                for (int i = 1; i < topology.length-1; i++) {
                    layer[i] = new RecurrentHiddenLayer(this, i, io.getLayerWeights(i), io.getRecurrentLayerWeights(i));
                }
            }else{
                for (int i = 1; i < topology.length-1; i++) {
                    layer[i] = new HiddenLayer(this, i, io.getLayerWeights(i));
                }
            }
            layer[layer.length-1] = new OutputLayer(this, layer.length-1, io.getLayerWeights(layer.length-1));
        }else{
            throw new IllegalArgumentException("Nets need at least 2 layers");
        }
    }

    /**
     * This method feeds the input array into the input cells. <br>
     * It then feeds this forward through the network.
     * @param input The new input array.
     */
    public void feedForward(double[] input){
        this.input = input.clone();
        if(input.length == layer[0].getCell().length){
            for (int i = 0; i < input.length; i++) {
                layer[0].getCell(i).setOutput(input[i]);
            }
//
//            if(recurrent){
//                for (int i = 1; i < layer.length; i++) {
//                    for (int j = 0; j < layer[i].getCell().length; j++) {
//                        ((RecurrentHiddenCell)layer[i].getCell(j)).recurrentFeedForward();
//                    }
//                }
//            }

            for (int i = 1; i < layer.length; i++) {
                for (int j = 0; j < layer[i].getCell().length; j++) {
                    ((HiddenCell)layer[i].getCell(j)).feedForward();
                }
            }
            for (int i = 0; i < layer[layer.length-1].getCell().length; i++) {
                output[i] = layer[layer.length-1].getCell(i).getOutput();
            }
        }else{
            throw new IllegalArgumentException("The length of the input array does't match the amount of input cells.");
        }
    }

    /**
     * This method Calculates the error rate and the average error rate. <br>
     * It also corrects the weights of the cells by using gradients descent.
     * @param targetOuput The expected output array.
     */
    public void backPropogation(double[] targetOuput){
        this.targetOutput = targetOuput.clone();
        Layer outputLayer = layer[layer.length-1];
        error = 0;
        for (int i = 0; i < outputLayer.getCell().length; i++) {
            double delta = targetOuput[i] - outputLayer.getCell(i).getOutput();
            error += delta * delta;
        }

        error /= ((double)outputLayer.getCell().length);
        error = Math.sqrt(error);
        recentAverageError = (recentAverageError * recentAverageSmoothingFactor + error)/(recentAverageSmoothingFactor+1);

        for (int i = 0; i < outputLayer.getCell().length; i++) {
            ((OutputCell)outputLayer.getCell(i)).calcOutputGradient(targetOuput[i]);
        }

//        if(recurrent){
//            for (int i = layer.length-2; i > 0; --i) {
//                for (int j = 0; j < layer[i].getCell().length; j++) {
//                    ((RecurrentHiddenCell)layer[i].getCell(j)).calcRecurrentGradient();
//                }
//            }
//        }

        for (int i = layer.length-2; i > 0; --i) {
            for (int j = 0; j < layer[i].getCell().length; j++) {
                ((HiddenCell)layer[i].getCell(j)).calcHiddenGradient();
            }
        }

        for (int i = layer.length-1; i > 0 ; --i) {
            for (int j = 0; j < layer[i].getCell().length; j++) {
                ((HiddenCell)layer[i].getCell(j)).updateWeights();
            }
        }
    }

    /**
     * Gets the input array.
     * @return Input array.
     */
    public double[] getInput() {
        return input;
    }

    /**
     * Gets the output array.
     * @return Output array.
     */
    public double[] getOutput(){
        return output;
    }

    /**
     * Gets the target output array.
     * @return Target output array.
     */
    public double[] getTargetOutput() {
        return targetOutput;
    }

    /**
     * Gets the last error rate.
     * @return Last error rate.
     */
    public double getError() {
        return error;
    }

    /**
     * Gets the RecentAverageError.
     * @return RecentAverageError
     */
    public double getRecentAverageError() {
        return recentAverageError;
    }

    /**
     * Gets the recentAverageError Smoothing factor.
     * @return RecentAverageError Smoothing factor.
     */
    public double getRecentAverageSmoothingFactor() {
        return recentAverageSmoothingFactor;
    }

    /**
     * Sets the recentAverageError Smoothing factor.
     * @param recentAverageSmoothingFactor The new Factor between 0 and 1.
     */
    public void setRecentAverageSmoothingFactor(double recentAverageSmoothingFactor) {
        this.recentAverageSmoothingFactor = recentAverageSmoothingFactor;
    }

    /**
     * Gets the IO object of this net.
     * @return IO object.
     */
    public IO getIo() {
        return io;
    }

    /**
     * Gets an array of layers.
     * @return All layers.
     */
    public Layer[] getLayer() {
        return layer;
    }

    /**
     * Gets a certain layer.
     * @param index Index of the wanted layer.
     * @return Wanted layer.
     */
    public Layer getLayer(int index) {
        return layer[index];
    }

    public int[] getTopology() {
        return topology;
    }

    public int getTopology(int index) {
        return topology[index];
    }

    /**
     * Method to set a filePath to save the weights on.
     * @param filePath String of the path of the file.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @return Eta: The overall learning rate.
     */
    public double getEta() {
        return ((HiddenLayer)layer[1]).getCell(0).getEta();
    }

    /**
     * @return Alpha: The fraction of deltaWeight to be added to the new Weight when training.
     */
    public double getAlpha() {
        return ((HiddenLayer)layer[1]).getCell(0).getAlpha();
    }

    /**
     * Eta is the overall learning rate which is applied to the gradient.
     * @param eta Fraction between 0 and 1.
     */
    public void setEta(double eta) {
        for (int i = 1; i < layer.length; i++) {
            for (int j = 0; j < ((HiddenLayer)layer[i]).getCell().length; j++) {
                HiddenCell cell = ((HiddenLayer)layer[i]).getCell(j);
                cell.setEta(eta);
            }
        }
    }

    /**
     * Alpha is the fraction of the deltaWeight that is added to the new Weight when training.
     * @param alpha Fraction between 0 and 1.
     */
    public void setAlpha(double alpha) {
        for (int i = 1; i < layer.length; i++) {
            for (int j = 0; j < ((HiddenLayer)layer[i]).getCell().length; j++) {
                HiddenCell cell = ((HiddenLayer)layer[i]).getCell(j);
                cell.setAlpha(alpha);
            }
        }
    }
}
