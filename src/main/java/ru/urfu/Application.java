package ru.urfu;


import ru.urfu.network.Network;
import ru.urfu.transformation.TransformationImage;



public class Application {
    private static String pathToImages = "src/main/resources/images/letters";

    public static void main(String[] args) {
        transformationDataset();
    }

    private static void transformationDataset() {
        TransformationImage transformationImage = new TransformationImage(pathToImages);
//        transformationImage.generateBytesLetters();
        Network net = new Network(3, 4, 1, 3);
        net.setNumberNeuronsHiddenLayers(3);
        net.initializeWeights();
    }

}
