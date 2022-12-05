package ru.urfu.network;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.urfu.exceptions.BadNumberOfNeurons;
import ru.urfu.exceptions.InvalidLayersSize;
import ru.urfu.network.properties.Neuron;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Network {
    final int LAYERS;
    final int INPUT_LAYER_SIZE;
    final int HIDDEN_LAYERS_AMOUNT;
    final List<Integer> HIDDEN_LAYERS_SIZE = new ArrayList<>(List.of(64));
    final int OUTPUT_LAYER_SIZE;
    // LAYER (from the hidden layer) -> CURR NEURON -> PREV NEURON
    final List<List<List<Double>>> WEIGHTS = new ArrayList<>();

    List<List<Neuron>> NEURONS;

    double ERROR;

    final String TRAIN_DATASET_DIRECTORY;
    final String TEST_DATASET_DIRECTORY;

    public Network(int LAYERS, int INPUT_LAYER_SIZE, int HIDDEN_LAYERS_AMOUNT, int OUTPUT_LAYER_SIZE) {
        this.LAYERS = LAYERS;
        this.INPUT_LAYER_SIZE = INPUT_LAYER_SIZE;
        this.HIDDEN_LAYERS_AMOUNT = HIDDEN_LAYERS_AMOUNT;
        this.OUTPUT_LAYER_SIZE = OUTPUT_LAYER_SIZE;
        TRAIN_DATASET_DIRECTORY = "src/main/resources/bytes/letters/train";
        TEST_DATASET_DIRECTORY = "src/main/resources/bytes/letters/test";
    }

    public Network(int LAYERS, int INPUT_LAYER_SIZE, int HIDDEN_LAYERS_AMOUNT, int OUTPUT_LAYER_SIZE,
                   String TRAIN_DATASET_DIRECTORY, String TEST_DATASET_DIRECTORY) {
        this.LAYERS = LAYERS;
        this.INPUT_LAYER_SIZE = INPUT_LAYER_SIZE;
        this.HIDDEN_LAYERS_AMOUNT = HIDDEN_LAYERS_AMOUNT;
        this.OUTPUT_LAYER_SIZE = OUTPUT_LAYER_SIZE;
        this.TRAIN_DATASET_DIRECTORY = TRAIN_DATASET_DIRECTORY;
        this.TEST_DATASET_DIRECTORY = TEST_DATASET_DIRECTORY;
    }

    public void setNumberNeuronsHiddenLayers(int... numberNeuronsHiddenLayers) {
        HIDDEN_LAYERS_SIZE.clear();
        if (numberNeuronsHiddenLayers.length > HIDDEN_LAYERS_AMOUNT) {
            throw new BadNumberOfNeurons("The specified number of layers is less than transferred number of layers");
        }
        for (int numberNeuronsHidden : numberNeuronsHiddenLayers) {
            HIDDEN_LAYERS_SIZE.add(numberNeuronsHidden);
        }
        if (numberNeuronsHiddenLayers.length < HIDDEN_LAYERS_AMOUNT) {
            int discrepancyQuantity = HIDDEN_LAYERS_AMOUNT - numberNeuronsHiddenLayers.length;
            while (discrepancyQuantity > 0) {
                HIDDEN_LAYERS_SIZE.add(HIDDEN_LAYERS_SIZE.get(HIDDEN_LAYERS_SIZE.size() - 1));
                discrepancyQuantity--;
            }
        }
    }

    public void initializeWeights() {
        if (LAYERS < 3) throw new InvalidLayersSize("Layers must be greater than 2");
        while (HIDDEN_LAYERS_SIZE.size() < HIDDEN_LAYERS_AMOUNT) {
            HIDDEN_LAYERS_SIZE.add(HIDDEN_LAYERS_SIZE.get(HIDDEN_LAYERS_SIZE.size() - 1));
        }
        for (int layer = 0; layer < LAYERS - 1; layer++) {
            List<List<Double>> layerWeights = new ArrayList<>();
            final int currLayerSize = layer == LAYERS - 2 ? OUTPUT_LAYER_SIZE : HIDDEN_LAYERS_SIZE.get(layer);
            final int prevLayerSize = layer == 0 ? INPUT_LAYER_SIZE : HIDDEN_LAYERS_SIZE.get(layer - 1);
            for (int currNeuronIndex = 0; currNeuronIndex <= currLayerSize; currNeuronIndex++) {
                List<Double> neuronWeights = new ArrayList<>();
                for (int prevNeuronIndex = 0; prevNeuronIndex <= prevLayerSize; prevNeuronIndex++) {
                    neuronWeights.add(Math.random() * 2 - 1);
                }
                layerWeights.add(new ArrayList<>(neuronWeights));
            }
            System.out.println("Layer: " + layer);
            System.out.println(layerWeights);
            WEIGHTS.add(new ArrayList<>(layerWeights));
        }
    }

    public void traverseTrainDataset() {
        Path rootDir = Path.of(TRAIN_DATASET_DIRECTORY);
        if (Files.notExists(rootDir)) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                System.out.println("Can't read file: " + rootDir);
            }
        }


    }

    public void traverseTestDataset() {
    }

}
