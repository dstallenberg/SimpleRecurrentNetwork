package com.dimitri.nffnn.neural;

import com.dimitri.nffnn.neural.cells.HiddenCell;

import java.io.*;
import java.util.Scanner;

public class IO {

    private String filePath;
    private double[][][] weights;
    private double[][][] recurrentWeights;//FIX THIS
    // FIX FIX FIX

    public IO(String filePath){
        this.filePath = filePath;
    }

    public void Read() throws FileNotFoundException {
        File file = new File(filePath);
        weights = count(file);
        Scanner scanner = new Scanner(file);

        int layer = 0;
        int neuron = 0;
        int weight = 0;

        if(scanner.hasNext()){
            String current = scanner.next();
            while(scanner.hasNext()){
                current = scanner.next();
                while(!current.equals("Layer:") && scanner.hasNext()){
                    current = scanner.next();
                    while(!current.equals("Neuron:") && !current.equals("Layer:")){
                        weights[layer][neuron][weight] = Double.parseDouble(current);
                        weight++;
                        if(scanner.hasNext()){
                            current = scanner.next();
                        }else{
                            break;
                        }
                    }
                    weight = 0;
                    neuron++;
                }
                neuron = 0;
                layer++;
            }
        }

        scanner.close();
    }

    public void Write(Net net) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(filePath);
        printWriter.print("Layer:\n");
        for (int i = 0; i < net.getLayer(0).getCell().length; i++) {
            printWriter.print("\tNeuron:\n");
        }

        for (int i = 1; i < net.getLayer().length; i++) {
            printWriter.print("Layer:\n");
            for (int j = 0; j < net.getLayer(i).getCell().length; j++) {
                printWriter.print("\tNeuron:\n");
                for (int k = 0; k < ((HiddenCell)net.getLayer(i).getCell(j)).getConnection().length; k++) {
                    printWriter.print("\t\t" + ((HiddenCell)net.getLayer(i).getCell(j)).getConnection(k).getWeight() + "\n");
                }
            }
        }
        printWriter.close();
    }

    public double[][][] count(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String current;
        int layers = 0;
        while (scanner.hasNext()){
            current = scanner.next();
            if(current.equals("Layer:")){
                layers++;
            }
        }
        double[][][] weights = new double[layers][][];

        scanner = new Scanner(file);
        current = scanner.next();
        for (int i = 0; i < weights.length; i++) {

            int neurons = 0;
            current = scanner.next();
            while (!current.equals("Layer:")){
                if(current.equals("Neuron:")){
                    neurons++;
                }
                if(scanner.hasNext()){
                    current = scanner.next();
                }else{
                    break;
                }
            }

            weights[i] = new double[neurons][];
        }

        scanner = new Scanner(file);
        current = scanner.next();
        for (int i = 0; i < weights.length; i++) {
            current = scanner.next();
            for (int j = 0; j < weights[i].length; j++) {
                current = scanner.next();
                int weight = 0;
                while(!current.equals("Neuron:") && !current.equals("Layer:")) {
                    weight++;
                    if (scanner.hasNext()) {
                        current = scanner.next();
                    } else {
                        break;
                    }
                }
                weights[i][j] = new double[weight];
            }
        }

        return weights;
    }

    public double[][] getLayerWeights(int layerIndex) {
        return weights[layerIndex];
    }

    public double[][] getRecurrentLayerWeights(int layerIndex) {
        return recurrentWeights[layerIndex];
    }


    public double[][][] getWeights() {
        return weights;
    }

    public int[] getTopology() throws IOException {
        if(!weights.equals(null)){
            int[] topology =  new int[weights.length];

            for (int i = 0; i < weights.length; i++) {
                topology[i] = weights[i].length;
            }

            return topology;
        }else{
            throw new IOException("No topology has been derived yet");
        }
    }

}
