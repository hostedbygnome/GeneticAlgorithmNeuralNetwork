package ru.urfu.network.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum IdealAnswers {
    A,
    B,
    H,
    L,
    X;

    public List<Double> getAnswer() {
        switch (this) {
            case A -> {
                return new ArrayList<>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0));
            }
            case B -> {
                return new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0, 0.0, 0.0));
            }
            case H -> {
                return new ArrayList<>(Arrays.asList(0.0, 0.0, 1.0, 0.0, 0.0));
            }
            case L -> {
                return new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 1.0, 0.0));
            }
            case X -> {
                return new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.0));
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }
}
