package ru.urfu;


import ru.urfu.training.genetic.ThreadsGenetic;
import ru.urfu.transformation.TransformationImage;


public class Application {
    private final static String pathToImages = "src/main/resources/images/letters";

    public static void main(String[] args) {
        transformationDataset();
    }

    private static void transformationDataset() {
        TransformationImage transformationImage = new TransformationImage(pathToImages);
//        transformationImage.generateBytesLetters();
        ThreadsGenetic threads = new ThreadsGenetic(4);
        threads.threadsTraining();
        threads.threadsTesting();
    }
}
