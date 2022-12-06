package ru.urfu;


import ru.urfu.training.genetic.GeneticTraining;
import ru.urfu.transformation.TransformationImage;


public class Application {
    private static String pathToImages = "src/main/resources/images/letters";

    public static void main(String[] args) {
        transformationDataset();
    }

    private static void transformationDataset() {
        TransformationImage transformationImage = new TransformationImage(pathToImages);
        transformationImage.generateBytesLetters();
        GeneticTraining training = new GeneticTraining(200, 300, 50);
        training.training();
    }
}
