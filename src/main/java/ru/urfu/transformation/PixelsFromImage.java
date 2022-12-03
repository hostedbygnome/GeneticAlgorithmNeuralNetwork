package org.example.transformation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PixelsFromImage implements ConvertImage {
    private int[][] pixelsArray;

    @Override
    public void convertImageToPixels(BufferedImage image) {
        pixelsArray = new int[image.getHeight()][image.getWidth()];
        for (int i = 0; i < pixelsArray.length; i++) {
            for (int j = 0; j < pixelsArray[i].length; j++) {
                int pixel = image.getRGB(j, i);
                int a = (pixel >> 24) & 0xFF;
                pixelsArray[i][j] = a > 0 ? 1 : 0;
            }
        }
    }

    @Override
    public void writeBytesFromPixels(Path outputFile) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixelsArray.length; i++) {
            for (int j = 0; j < pixelsArray[i].length; j++) {
                sb.append(pixelsArray[i][j]);
            }
        }
        if (Files.notExists(outputFile.subpath(0, outputFile.getNameCount() - 1))) {
            try {
                Files.createDirectory(outputFile.subpath(0, outputFile.getNameCount() - 1));
            } catch (IOException e) {
                System.out.println("Can't create directory " + outputFile.subpath(0, outputFile.getNameCount() - 1));
            }
        }

        if (Files.notExists(outputFile)) {
            try {
                Files.writeString(outputFile, sb.toString());
            } catch (IOException e) {
                System.out.println("Can't write to " + outputFile);
            }
        }
    }
}
