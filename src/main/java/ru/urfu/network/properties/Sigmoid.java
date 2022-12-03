package org.example.network.properties;

public class Sigmoid implements ActivationFunction {
    @Override
    public double outputValue(double totalizerValue) {
        return 1 / (1 + Math.exp(totalizerValue));
    }
}
