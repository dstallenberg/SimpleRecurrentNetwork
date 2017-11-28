package com.dimitri.nffnn.neural;

public class ProcessorCell {

    public static double tanh(double input){
        return Math.tanh(input);
    }

    public static double derrivativeTanh(double input){
        double x = tanh(input);
        return 1.0 - x*x;
    }
}
