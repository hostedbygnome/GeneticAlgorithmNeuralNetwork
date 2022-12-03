package org.example;

import org.example.transformation.TransformationImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Application {
    private static String pathToImages = "src/main/resources/images/letters";

    public static void main(String[] args) {
        transformationDataset();
    }

    private static void transformationDataset() {
        TransformationImage transformationImage = new TransformationImage(pathToImages);
        transformationImage.generateBytesLetters();
    }
}
