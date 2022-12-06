package ru.urfu.network.properties;

public class Sigmoid implements ActivationFunction {
    public static Double calcActivation(Double totalizerValue) {
        return (1 / (1 + 0.6 * Math.exp(-totalizerValue)));
    }
}
