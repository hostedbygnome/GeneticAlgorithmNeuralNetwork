package ru.urfu.transformation;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public interface ConvertImage {
    void convertImageToPixels(BufferedImage image);
    void writeBytesFromPixels(Path outputFile);
}
