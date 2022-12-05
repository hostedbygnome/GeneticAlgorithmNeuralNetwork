package ru.urfu.training.genetic;

import ru.urfu.exceptions.InvalidLayersSize;

import java.util.ArrayList;
import java.util.List;

public class Genetic {
    public static List<List<List<Double>>> crossing(List<List<List<Double>>> firstParentWeights,
                                                    List<List<List<Double>>> secondParentWeights) {
        if (firstParentWeights.size() != secondParentWeights.size()) {
            throw new InvalidLayersSize("First parent weights must be the same size as second parent weights");
        }
        double rate = Math.random();
        final List<List<List<Double>>> afterCrossingWeights = new ArrayList<>();
        // (from the hidden layer)
        final int layers = firstParentWeights.size();
        for (int layer = 0; layer < layers; layer++) {
            List<List<Double>> layerWeights = new ArrayList<>();
            final int currLayerSize = firstParentWeights.get(layer).size();
            for (int currNeuronIndex = 0; currNeuronIndex < currLayerSize; currNeuronIndex++) {
                List<Double> neuronWeights = new ArrayList<>();
                final int prevLayerSize = firstParentWeights.get(layer).get(currNeuronIndex).size();
                for (int prevNeuronIndex = 0; prevNeuronIndex < prevLayerSize; prevNeuronIndex++) {
                    neuronWeights.add(firstParentWeights.get(layer).get(currNeuronIndex).get(prevNeuronIndex) * rate +
                            secondParentWeights.get(layer).get(currNeuronIndex).get(prevNeuronIndex) * (1 - rate));
                }
                layerWeights.add(new ArrayList<>(neuronWeights));
            }
            System.out.println("Layer: " + layer);
            System.out.println(layerWeights);
            afterCrossingWeights.add(new ArrayList<>(layerWeights));
        }
        return afterCrossingWeights;
    }

    public static List<List<List<Double>>> mutation(List<List<List<Double>>> weights) {
        List<List<List<Double>>> afterMutationWeights = new ArrayList<>(weights);
        int layer = (int) (Math.random() * weights.size());
        int currNeuronIndex = (int) (Math.random() * (weights.get(layer).size() - 1));
        int prevNeuronIndex = (int) (Math.random() * (weights.get(layer).get(currNeuronIndex).size() - 1));
        afterMutationWeights.get(layer).get(currNeuronIndex).set(prevNeuronIndex, Math.random() * 2 - 1);
        System.out.println(afterMutationWeights);
        return afterMutationWeights;
    }
}
