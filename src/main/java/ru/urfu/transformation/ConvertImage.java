package org.example.transformation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;

public interface ConvertImage {
    void convertImageToPixels(BufferedImage image);
    void writeBytesFromPixels(Path outputFile);
}
