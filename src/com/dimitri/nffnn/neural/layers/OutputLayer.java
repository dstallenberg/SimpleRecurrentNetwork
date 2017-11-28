package com.dimitri.nffnn.neural.layers;

import com.dimitri.nffnn.neural.Net;
import com.dimitri.nffnn.neural.cells.Cell;
import com.dimitri.nffnn.neural.cells.OutputCell;

public class OutputLayer extends Layer{

    private OutputCell[] cell;

    public OutputLayer(Net net, int layerIndex, int cellAmount) {
        super(net, layerIndex, cellAmount);
        cell = new OutputCell[cellAmount];
        for (int i = 0; i < cell.length; i++) {
            cell[i] = new OutputCell(this, i, net.getLayer(layerIndex-1).getCell().length);
        }
    }

    public OutputLayer(Net net, int layerIndex, double[][] weights) {
        super(net, layerIndex, weights.length);
        cell = new OutputCell[weights.length];
        for (int i = 0; i < cell.length; i++) {
            cell[i] = new OutputCell(this, i, net.getLayer(layerIndex-1).getCell().length, weights[i]);
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
