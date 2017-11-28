package com.dimitri.nffnn.neural.layers;

import com.dimitri.nffnn.neural.Net;
import com.dimitri.nffnn.neural.cells.Cell;
import com.dimitri.nffnn.neural.cells.RecurrentHiddenCell;

public class RecurrentHiddenLayer extends Layer{

    private RecurrentHiddenCell[] cell;

    public RecurrentHiddenLayer(Net net, int layerIndex, int cellAmount) {
        super(net, layerIndex, cellAmount);
        cell = new RecurrentHiddenCell[cellAmount];
        for (int i = 0; i < cell.length; i++) {
            cell[i] = new RecurrentHiddenCell(this, i, net.getLayer(layerIndex-1).getCell().length);
        }
    }

    public RecurrentHiddenLayer(Net net, int layerIndex, double[][] weights, double[][] recurrentWeights) {
        super(net, layerIndex, weights.length);
        cell = new RecurrentHiddenCell[weights.length];
        for (int i = 0; i < cell.length; i++) {
            cell[i] = new RecurrentHiddenCell(this, i, net.getLayer(layerIndex-1).getCell().length, weights[i], recurrentWeights[i]);
        }
    }

    @Override
    public Cell[] getCell() {
        return cell;
    }

    @Override
    public Cell getCell(int index) {
        return cell[index];
    }
}
