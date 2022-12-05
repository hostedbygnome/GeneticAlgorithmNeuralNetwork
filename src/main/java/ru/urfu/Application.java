package ru.urfu;


import ru.urfu.network.Network;
import ru.urfu.training.genetic.Genetic;
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
        System.out.println("net\n");
        net.initializeWeights();
        System.out.println("crossing\n");
        Genetic.mutation(net.getWEIGHTS());
    }

}
