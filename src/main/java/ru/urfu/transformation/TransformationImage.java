package org.example.transformation;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.imgscalr.Scalr.resize;

public class TransformationImage {
    private static int indexFile = 1;

    private static String lastLetterClass = "";
    private final int IMG_SIZE = 32;
    private String pathToBytes = "src/main/resources/bytes/letters";
    private File baseDir;

    public TransformationImage(String baseDirPath) {
        this.baseDir = new File(baseDirPath);
    }

    public void generateBytesLetters() {
        traverseDirectory(baseDir);
    }

    private void traverseDirectory(File file) {
        if (file.exists()) {
            File[] directoryFiles = file.listFiles();
            if (directoryFiles != null) {
                for (File child : directoryFiles) {
                    if (child.isDirectory()) {
                        traverseDirectory(child);
                    } else {
                        String path = child.getAbsolutePath();
                        if (path.endsWith(".DS_Store")) continue;
                        String purposeImage = path.contains("test") ? "test" : "train";
                        String letterClass = path.substring(path.lastIndexOf("/") - 1, path.lastIndexOf("/"));
                        if (!letterClass.equals(lastLetterClass)) {
                            lastLetterClass = letterClass;
                            indexFile = 1;
                        }
                        PixelsFromImage transformImage = new PixelsFromImage();
                        try {
                            BufferedImage inputImage = ImageIO.read(new File(path));
                            if (inputImage != null && inputImage.getWidth() != 32) {
//                                System.out.println(path);
                                transformImage.convertImageToPixels(resize(inputImage, Scalr.Method.ULTRA_QUALITY, IMG_SIZE, IMG_SIZE));
                                transformImage.writeBytesFromPixels(
                                        Paths.get(pathToBytes,
                                                purposeImage,
                                                letterClass,
                                                String.valueOf(indexFile++)));
                            }
                        } catch (IOException e) {
                            System.out.println("Can't read file: " + path);
                        }
                    }
                }
            }
        } else {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }
        }

    }
}
