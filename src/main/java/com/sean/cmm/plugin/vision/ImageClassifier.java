package com.sean.cmm.plugin.vision;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Mat;

import java.io.IOException;

public class ImageClassifier {
    private ComputationGraph model;

    public ImageClassifier() {
        try {
            // Load the VGG16 pretrained model
            model = (ComputationGraph) VGG16.builder().build().initPretrained();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String classifyImage(Mat imageMat) {
        try {
            // Convert OpenCV Mat to INDArray
            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray imageArray = loader.asMatrix(imageMat);

            // Preprocess the image
            imageArray = TrainedModels.VGG16.preProcess(imageArray);

            // Perform the classification
            INDArray output = model.outputSingle(imageArray);

            // Get the class label with highest probability and return its name
            int idx = Nd4j.argMax(output, 1).getInt(0);
            return model.getLabels().get(idx);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
