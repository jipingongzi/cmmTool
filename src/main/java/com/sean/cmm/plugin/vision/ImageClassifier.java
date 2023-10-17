package com.sean.cmm.plugin.vision;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.deeplearning4j.zoo.util.BaseLabels;
import org.deeplearning4j.zoo.util.Labels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.opencv.core.Mat;

import java.io.IOException;
import java.net.URL;

public class ImageClassifier {
    private ComputationGraph model;
    private Labels imageNetLabels;

    public ImageClassifier() {
        try {
            // Load the VGG16 pretrained model
            ZooModel zooModel = VGG16.builder().build();
            model = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);

            // Load ImageNet Labels
            imageNetLabels = new BaseLabels("imagenet") {
                @Override
                protected URL getURL() {
                    return null;
                }

                @Override
                protected String resourceName() {
                    return null;
                }

                @Override
                protected String resourceMD5() {
                    return null;
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String classifyImage(INDArray imageArray) {
        // Preprocess the image
        VGG16ImagePreProcessor preProcessor = new VGG16ImagePreProcessor();
        preProcessor.preProcess(imageArray);

        // Perform the classification
        INDArray output = model.outputSingle(imageArray);

        // Get the class label with highest probability and return its name
        int idx = output.argMax(1).getInt(0);
        return imageNetLabels.getLabel(idx);
    }
}
