package ru.urfu.network;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.urfu.exceptions.BadNumberOfNeurons;
import ru.urfu.exceptions.InvalidLayersSize;
import ru.urfu.network.properties.Dataset;
import ru.urfu.network.properties.Neuron;
import ru.urfu.network.properties.Sigmoid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.urfu.network.properties.IdealAnswers.*;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = {"PERCENTAGE_CORRECT_ANSWERS", "ERROR"})
public class Network {
    final int LAYERS;
    int INPUT_LAYER_SIZE;
    final int HIDDEN_LAYERS_AMOUNT;
    final List<Integer> HIDDEN_LAYERS_SIZE = new ArrayList<>(List.of(64));
    int OUTPUT_LAYER_SIZE;
    // LAYER (from the hidden layer) -> CURR NEURON -> PREV NEURON
    @Setter
    List<List<List<Double>>> WEIGHTS = new ArrayList<>();

    final List<List<Neuron>> NEURONS = new ArrayList<>();

    double ERROR;
    double CORRECT_ANSWERS;

    double LETTERS_AMOUNT;

    double PERCENTAGE_CORRECT_ANSWERS;
    final String TRAIN_DATASET_DIRECTORY;
    final String TEST_DATASET_DIRECTORY;
    @Setter
    Dataset datasetType;
    List<Double> answer = new ArrayList<>();

    public Network(int LAYERS, int HIDDEN_LAYERS_AMOUNT, Dataset datasetType) {
        this.LAYERS = LAYERS;
        this.HIDDEN_LAYERS_AMOUNT = HIDDEN_LAYERS_AMOUNT;
        TRAIN_DATASET_DIRECTORY = "src/main/resources/bytes/letters/train";
        TEST_DATASET_DIRECTORY = "src/main/resources/bytes/letters/test";
        this.datasetType = datasetType;
    }

    public Network(int LAYERS, int HIDDEN_LAYERS_AMOUNT,
                   String TRAIN_DATASET_DIRECTORY, String TEST_DATASET_DIRECTORY) {
        this.LAYERS = LAYERS;
        this.HIDDEN_LAYERS_AMOUNT = HIDDEN_LAYERS_AMOUNT;
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

    public void traverseDataset() {
        Path rootDir = datasetType == Dataset.TRAIN ? Path.of(TRAIN_DATASET_DIRECTORY)
                : datasetType == Dataset.TEST ? Path.of(TEST_DATASET_DIRECTORY)
                : Path.of("");
        if (datasetType == Dataset.TEST) {
            ERROR = 0;
            CORRECT_ANSWERS = 0;
            LETTERS_AMOUNT = 0;
            PERCENTAGE_CORRECT_ANSWERS = 0;
        }
        if (Files.notExists(rootDir)) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                System.out.println("Directory: " + rootDir + " does not exist");
            }
        } else {
            File datasetDirectory = new File(rootDir.toString());
            if (Files.isDirectory(rootDir)) {
                OUTPUT_LAYER_SIZE = Objects.requireNonNull(datasetDirectory.listFiles()).length;
            } else {
                try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    System.out.println(rootDir + " is not a directory");
                }
            }
            traverseLetters(datasetDirectory);
        }
    }

    private void traverseLetters(File file) {
        if (file.exists()) {
            File[] letters = file.listFiles();
            assert letters != null;
            for (File letter : letters) {
                if (letter.isDirectory()) traverseLetters(letter);
                else {
                    getCurrentAnswer(letter.getPath().replaceAll("[^A-Z]", ""));
                    NEURONS.clear();
                    List<Neuron> inputLayerNeurons = new ArrayList<>();
                    List<String> values = new ArrayList<>();
                    try {
                        values = Files.readAllLines(Path.of(letter.getAbsolutePath()));
                    } catch (IOException e) {
                        System.out.println("Can't read file: " + letter.getAbsolutePath());
                    }
                    INPUT_LAYER_SIZE = (int) values.size();
                    for (int pixelIndex = 0; pixelIndex <= values.size(); pixelIndex++) {
                        Neuron currInputLayerNeuron;
                        if (pixelIndex < values.size()) {
                            Double pixelValue = Double.parseDouble(values.get(pixelIndex));
                            currInputLayerNeuron = new Neuron(pixelValue, pixelValue);
                        } else {
                            currInputLayerNeuron = new Neuron(1.0, 1.0);
                        }
                        inputLayerNeurons.add(currInputLayerNeuron);
                    }
                    NEURONS.add(new ArrayList<>(inputLayerNeurons));
                    inputLayerNeurons.clear();
                    if (WEIGHTS.size() != LAYERS - 1) initializeWeights();
                    generateNeurons();
                }
            }
        }
    }

    private void getCurrentAnswer(String letter) {
        switch (letter) {
            case "A" -> answer = A.getAnswer();
            case "B" -> answer = B.getAnswer();
            case "H" -> answer = H.getAnswer();
            case "L" -> answer = L.getAnswer();
            case "X" -> answer = X.getAnswer();
            default -> throw new IllegalArgumentException("Illegal letter: " + letter);
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
                neuronWeights.clear();
            }
            WEIGHTS.add(new ArrayList<>(layerWeights));
            layerWeights.clear();
        }
    }

    public void generateNeurons() {
        for (int layer = 0; layer < LAYERS - 1; layer++) {
            List<Neuron> layerNeurons = new ArrayList<>();
            final int currLayerSize = layer == LAYERS - 2 ? OUTPUT_LAYER_SIZE : HIDDEN_LAYERS_SIZE.get(layer);
            final int prevLayerSize = layer == 0 ? INPUT_LAYER_SIZE : HIDDEN_LAYERS_SIZE.get(layer - 1);
            for (int currNeuronIndex = 0; currNeuronIndex <= currLayerSize; currNeuronIndex++) {
                double totalizerValue = 0;
                for (int prevNeuronIndex = 0; prevNeuronIndex <= prevLayerSize; prevNeuronIndex++) {
                    totalizerValue += WEIGHTS.get(layer).get(currNeuronIndex).get(prevNeuronIndex) *
                            NEURONS.get(layer).get(prevNeuronIndex).getOutputValue();
                }
                layerNeurons.add(new Neuron(totalizerValue, Sigmoid.calcActivation(totalizerValue)));
            }
            NEURONS.add(new ArrayList<>(layerNeurons));
            layerNeurons.clear();
        }
        int currentAnswer = 0;
        double highestOutput = 0;
        for (int outputNeuron = 0; outputNeuron < OUTPUT_LAYER_SIZE; outputNeuron++) {
            double currOutputValue = NEURONS.get(LAYERS - 1).get(outputNeuron).getOutputValue();
            if (highestOutput < currOutputValue) {
                highestOutput = currOutputValue;
                currentAnswer = outputNeuron;
            }
            ERROR += Math.pow((answer.get(outputNeuron) - currOutputValue), 2);
        }
        LETTERS_AMOUNT++;
        if (answer.get(currentAnswer) == 1) {
            CORRECT_ANSWERS++;
        }
        PERCENTAGE_CORRECT_ANSWERS = CORRECT_ANSWERS / LETTERS_AMOUNT * 100;
    }
}