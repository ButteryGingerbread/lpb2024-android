package com.example.lpb2024;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

public class ImageClassifier {

    private Interpreter interpreter;

    // Update modelInputSize if needed
    public ImageClassifier(Context context, String modelFileName, int modelInputSize) {
        try {
            interpreter = new Interpreter(FileUtil.loadMappedFile(context, modelFileName));
        } catch (Exception e) {
            throw new RuntimeException("Error initializing TensorFlow!", e);
        }
    }

    public float[] classifyImage(Bitmap bitmap) {
        int imageSizeX = 224;
        int imageSizeY = 224;

        bitmap = Bitmap.createScaledBitmap(bitmap, imageSizeX, imageSizeY, true);

        // Input and output arrays
        float[][][][] input = new float[1][imageSizeX][imageSizeY][3];
        float[][] output = new float[1][20]; // Update output size to match model's output shape

        // Convert the bitmap to float arrays
        for (int x = 0; x < imageSizeX; x++) {
            for (int y = 0; y < imageSizeY; y++) {
                int pixel = bitmap.getPixel(x, y);
                input[0][x][y][0] = ((pixel >> 16) & 0xFF) / 255.0f; // Red
                input[0][x][y][1] = ((pixel >> 8) & 0xFF) / 255.0f;  // Green
                input[0][x][y][2] = (pixel & 0xFF) / 255.0f;         // Blue
            }
        }

        // Run the model
        interpreter.run(input, output);

        return output[0];
    }
}
